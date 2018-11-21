package online.xrhub.xrhubstore.controller;

import online.xrhub.xrhubstore.client.AssetDataClient;
import online.xrhub.xrhubstore.client.AssetHeaderClient;
import online.xrhub.xrhubstore.client.MetaClient;
import online.xrhub.xrhubstore.entity.AssetReference;
import online.xrhub.xrhubstore.service.AssetReferenceService;
import online.xrhub.xrhubstore.utils.ArrayUtils;
import online.xrhub.xrhubstore.utils.FileUtils;
import online.xrhub.xrhubstore.utils.RestResponse;
import online.xrhub.xrhubstore.utils.XrmUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @AUTHOR soft
 * @DATE 2018/11/12 20:02
 * @DESCRIBE 资源控制层
 */
@RestController
@RequestMapping("/store")
public class AssetController {

    @Autowired
    private MetaClient metaClient;

    @Autowired
    private AssetDataClient dataClient;

    @Autowired
    private AssetHeaderClient headerClient;

    @Autowired
    private AssetReferenceService referenceService;

    @Value("${upload.path}")
    private String upload;

    /**
     * 保存文件到本地
     * @param part 文件
     */
    public String saveFile(MultipartFile part) throws IOException {
        if (part != null && part.getSize() > 1) {
            String filename = FileUtils.randomFilename(part.getOriginalFilename());
            File target = new File(upload, filename);
            if (FileUtils.createFile(target)) {
                part.transferTo(target);
                return "/asset/image/" + Base64Utils.encodeToString(filename.getBytes());
            }
        }
        return "";
    }

    /**
     * 上传glb模型文件并保存
     * @param files 文件列表
     * @param info  附带信息 用户等信息
     * @return 保存的资源头信息
     */
    @PostMapping("/build")
    public RestResponse build(HttpServletRequest request,
                              @RequestParam(name = "file") MultipartFile[] files,
                              @RequestParam(name = "image") MultipartFile image,
                              @RequestParam Map<String, Object> info) throws IOException {
        if (StringUtils.isEmpty(info.get("thumbnailId"))) {  // 设置缩略图
            String path = saveFile(image);
            if (StringUtils.isEmpty(path)) {
                path = "/static/pp.png";
            }
            info.put("thumbnailId", FileUtils.getHost(request) + path);
        }
        List<String> metaIds = new LinkedList<>();
        // 1.上传glb文件到mongo文件系
        XrmUtils.XRMJson xrmJson = this.buildXrm(files, metaIds);
        if (null != xrmJson) {
            info.put("size", this.filesSize(files)); // 保存文件大小
            // 2.构建xrm资源体描述文件
            RestResponse dataResponse = dataClient.upload(xrmJson.toBytes());
            if (RestResponse.isOk(dataResponse)) {
                String dataId = (String) dataResponse.getData("id");
                info.put("dataId", dataId);  // 将资源体放入请求参数

                // 3.保存资源头信息
                RestResponse headerResponse = headerClient.add(info);
                if (RestResponse.isOk(headerResponse)) {
                    String headerId = (String) headerResponse.getPayload();

                    // 创建资源引用索引
                    for (String metaId : metaIds) {
                        AssetReference assetReference = new AssetReference(headerId, dataId, metaId);
                        referenceService.add(assetReference);
                    }
                    return headerResponse;
                }
            }
        }
        return RestResponse.fail();
    }

    @PostMapping("/delete")
    public RestResponse delete_(@RequestParam String id) {
        return this.delete(id);
    }

    /**
     * 根据资源头id删除资源
     * 会根据引用判断是否要删除元数据
     * @param headerId 资源头id
     */
    @PostMapping("/delete/{headerId}")
    public RestResponse delete(@PathVariable String headerId) {
        List<AssetReference> assetReferences = referenceService.byHeaderId(headerId);
        if (!CollectionUtils.isEmpty(assetReferences)) {
            for (AssetReference assetReference : assetReferences) {
                String metaId = assetReference.getMetaId();
                if (!referenceService.metaIsUsed(metaId)) {
                    metaClient.delete(metaId);  // 1.删除meta元数据 如果没有在被使用
                }
            }

            String       dataId = assetReferences.get(0).getDataId();
            RestResponse delete = dataClient.delete(dataId);  // 2.删除资源体
            if (RestResponse.isOk(delete)) {
                RestResponse delete1 = headerClient.delete(headerId);// 3.删除资源头
                if (RestResponse.isOk(delete1)) {
                    try {
                        String thumbnailId = (String) delete1.getData("thumbnailId");
                        String base = thumbnailId.substring(thumbnailId.lastIndexOf("/") + 1);
                        String filename = new String(Base64Utils.decodeFromString(base));
                        new File(upload, filename).delete(); // 删除缩略图文件
                    } catch (Exception ignored) {}
                    referenceService.deleteByHeaderId(headerId); // 删除当前资源头引用
                    return delete1;
                }
            }
            return RestResponse.fail("被占用 无法删除！");
        }
        return RestResponse.fail("资源头不存在！");
    }

    @PostMapping("/change")
    public RestResponse change_(@RequestParam String id,
                               @RequestParam(value = "file", required = false) MultipartFile[] files,
                               @RequestParam Map<String, Object> info) throws IOException {
        return this.change(id, files, info);
    }

    /**
     * 根据id修改资源头
     * 1、判断是否新上传了元数据
     * 2、是否要重构资源体
     * 3、进行修改
     * @param headerId 资源头id
     * @param files 元数据
     * @param info  附带信息
     */
    @PostMapping("/change/{headerId}")
    public RestResponse change(@PathVariable String headerId,
                               @RequestParam(value = "file", required = false) MultipartFile[] files,
                               @RequestParam Map<String, Object> info) throws IOException {
        RestResponse response = headerClient.getOne(headerId); // 获取要修改的资源头信息
        if (RestResponse.isOk(response)) {
            String newDataId = null;
            List<String>     metaIds = new LinkedList<>();
            XrmUtils.XRMJson xrmJson = this.buildXrm(files, metaIds);
            if (xrmJson != null) { // 修改了元数据 需要重构资源体
                info.put("size", this.filesSize(files)); // 保存文件大小
                String dataId = (String) response.getData("dataId");
                // 重构资源体
                RestResponse change = dataClient.change(dataId, xrmJson.toBytes());
                if (RestResponse.isOk(change)) {
                    newDataId = (String) change.getData("id");
                }
                List<AssetReference> assetReferences = referenceService.byHeaderId(headerId);
                List<String> oldMetaIds = assetReferences.stream()
                                            .map(AssetReference::getMetaId)
                                            .collect(Collectors.toList());
                this.clearRefAndBuild(metaIds, oldMetaIds, headerId, newDataId);  // 清理和重建引用
            }
            if (!StringUtils.isEmpty(newDataId)) { // 将重构的资源体id和资源头关联
                info.put("dataId", newDataId);
            }

            return headerClient.change(headerId, info); // 提交修改
        }
        return RestResponse.fail("资源头不存在!");
    }

    @PostMapping("/get")
    public RestResponse get_(@RequestParam String id) {
        return this.get(id);
    }

    @PostMapping("/get/{headerId}")
    public RestResponse get(@PathVariable String headerId) {
        return headerClient.getOne(headerId);
    }

    @PostMapping("/page")
    public RestResponse page_(@RequestParam Integer pageNo,
                              @RequestParam Map<String, Object> query) {
        return this.page(pageNo, query);
    }

    @PostMapping("/page/{pageNo}")
    public RestResponse page(@PathVariable Integer pageNo,
                             @RequestParam Map<String, Object> query) {
        return headerClient.page(pageNo, query);
    }

    /**
     * 清理旧的引用和重建新引用
     * @param newMetaIds 所有新的元数据id
     * @param oldMetaIds 所有老的元数据id
     * @param headerId   资源头id
     * @param dataId     资源体id
     */
    private void clearRefAndBuild(List<String> newMetaIds, List<String> oldMetaIds,
                                          String headerId, String dataId) {
        referenceService.deleteByHeaderId(headerId);  // 删除以前引用
        for (String newMetaId : newMetaIds) {  // 新建引用
            referenceService.add(new AssetReference(headerId, dataId, newMetaId));
        }

        for (String oldMetaId : oldMetaIds) { // 元数据无引用则删除
            if (!newMetaIds.contains(oldMetaId) && !referenceService.metaIsUsed(oldMetaId)) {
                metaClient.delete(oldMetaId);
            }
        }
    }

    /**
     * 根据上传的元数据文件 构建xrm格式描述文件信息
     * @param files 元数据文件
     * @param metaIds 存放元数据id
     */
    private XrmUtils.XRMJson buildXrm(MultipartFile[] files, List<String> metaIds) throws IOException {
        if (!ArrayUtils.isEmpty(files)) {
            XrmUtils.XRMJson xrmJson = XrmUtils.baseXrm();

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];

                byte[] bytes = file.getBytes();
                RestResponse response = metaClient.upload(bytes, file.getOriginalFilename());
                if (RestResponse.isOk(response)) {
                    String id = (String) response.getData("id");
                    if (!StringUtils.isEmpty(id)) {
                        metaIds.add(id);
                        xrmJson.addMetaIds(id).setInfo(i, "name", file.getOriginalFilename());
                    }
                }
            }
            return xrmJson;
        }
        return null;
    }

    private long filesSize(MultipartFile[] files) {
        long size = 0;
        for (MultipartFile file : files) {
            size += file.getSize();
        }
        return size;
    }
}
