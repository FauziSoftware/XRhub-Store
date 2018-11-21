package online.xrhub.xrhubstore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @AUTHOR soft
 * @DATE 2018/11/14 18:57
 * @DESCRIBE 测试视图
 */
@Slf4j
@Controller
@RequestMapping("/asset")
public class ViewController {

    @Value("${upload.path}")
    private String upload;

    @GetMapping("/create")
    public String create() {
        return "create";
    }

    @GetMapping("/model")
    public String model() {
        return "create_model";
    }

    @GetMapping("/scene")
    public String scene() {
        return "create_scene";
    }

    @GetMapping("/skybox")
    public String skybox() {
        return "create_skybox";
    }

    @GetMapping("/material")
    public String material() {
        return "create_material";
    }

    /**
     * 图片下载
     * @param filename 要下载的图片名
     */
    @RequestMapping("/image/{filename}")
    public ResponseEntity<byte[]> image(@PathVariable String filename) {
        String s = new String(Base64Utils.decodeFromString(filename));
        File file = new File(upload, s);
        if (file.exists()) {
            try {
                String contentType = Files.probeContentType(file.toPath());
                FileInputStream stream = new FileInputStream(file);
                byte[] bytes = StreamUtils.copyToByteArray(stream);
                stream.close();
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + s)
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .body(bytes);
            } catch (IOException e) {
                log.error("图片下载失败！{}", s);
            }
        }
        return ResponseEntity.status(400).body("asset not exits".getBytes());
    }
}
