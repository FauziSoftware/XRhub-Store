package online.xrhub.xrhubstore.client;

import online.xrhub.xrhubstore.utils.ClientUtils;
import online.xrhub.xrhubstore.utils.RestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR soft
 * @DATE 2018/11/12 20:04
 * @DESCRIBE 元数据客户端
 */
@Component
public class MetaClient {

    @Value("${client.meta}")
    private String meta;

    public RestResponse upload(byte[] bytes, String filename) {
        return ClientUtils.upload(bApi("upload"), bytes, filename);
    }

    public RestResponse delete(String id) {
        return ClientUtils.post(bApi("delete/" + id));
    }

    public RestResponse page(String pageNo, String size) {
        Map<String, Object> query = new HashMap<>();
        query.put("size", size);
        return ClientUtils.post(bApi("page/" + pageNo), query);
    }

    public RestResponse getOne(String id) {
        return ClientUtils.post(bApi("get/" + id));
    }

    private String bApi(String api) {
        return meta + api;
    }
}
