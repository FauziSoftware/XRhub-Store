package online.xrhub.xrhubstore.client;

import online.xrhub.xrhubstore.utils.ClientUtils;
import online.xrhub.xrhubstore.utils.RestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @AUTHOR soft
 * @DATE 2018/11/12 21:27
 * @DESCRIBE
 */
@Component
public class AssetHeaderClient {
    @Value("${client.asset-header}")
    private String header;

    public RestResponse add(Map<String, Object> query) {
        return ClientUtils.post(bApi("build"), query);
    }

    public RestResponse delete(String id) {
        return ClientUtils.post(bApi("delete/" + id));
    }

    public RestResponse change(String id, Map<String, Object> query) {
        return ClientUtils.post(bApi("change/" + id), query);
    }

    public RestResponse getOne(String id) {
        return ClientUtils.post(bApi("get/" + id));
    }

    public RestResponse page(Integer pageNo, Map<String, Object> query) {
        return ClientUtils.post(bApi("page/" + pageNo), query);
    }

    private String bApi(String api) {
        return header + api;
    }
}
