package online.xrhub.xrhubstore.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

/**
 * @AUTHOR soft
 * @DATE 2018/11/12 20:24
 * @DESCRIBE 客户端工具
 */
public class ClientUtils {

    public static RestResponse upload(String url, byte[] bytes, String filename, String key) {
        MultiValueMap<String, Object> fd = new LinkedMultiValueMap<>();
        ByteArrayResource resource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        fd.put(key, Collections.singletonList(new HttpEntity<>(resource, headers)));
        return restPost(url, fd);
    }

    public static RestResponse upload(String url, byte[] bytes, String filename) {
        return upload(url, bytes, filename, "file");
    }

    public static RestResponse post(String url, Map<String, Object> query) {
        MultiValueMap<String, Object> queryMap = new LinkedMultiValueMap<>();
        if (query != null) {
            query.forEach((k, v)-> queryMap.put(k, Collections.singletonList(v)));
        }
        return restPost(url, queryMap);
    }

    public static RestResponse post(String url) {
        return post(url, null);
    }

    private static RestResponse restPost(String url, Object object) {
        UTF8RestTemplate template = new UTF8RestTemplate();
        return template.postForObject(url, object, RestResponse.class);
    }

    static class UTF8RestTemplate extends RestTemplate {
        UTF8RestTemplate() {
            super();
            for (HttpMessageConverter<?> converter : super.getMessageConverters()) {
                if (converter instanceof AbstractHttpMessageConverter) {
                    ((AbstractHttpMessageConverter<?>) converter).setDefaultCharset(Charset.forName("UTF-8"));
                }
            }
        }
    }
}
