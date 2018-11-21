package online.xrhub.xrhubstore.client;

import online.xrhub.xrhubstore.utils.ClientUtils;
import online.xrhub.xrhubstore.utils.RestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @AUTHOR soft
 * @DATE 2018/11/12 21:02
 * @DESCRIBE 资源体客户端
 */
@Component
public class AssetDataClient {

    @Value("${client.asset-data}")
    private String base;

    public RestResponse upload(byte[] bytes) {
        long second = Instant.now().getEpochSecond();
        return this.upload(bytes, second + ".xrm");
    }

    public RestResponse upload(byte[] bytes, String filename) {
        return ClientUtils.upload(bApi("upload"), bytes, filename);
    }

    public RestResponse delete(String id) {
        return ClientUtils.post(bApi("delete/" + id));
    }

    public RestResponse change(String id, byte[] bytes) {
        long second = Instant.now().getEpochSecond();
        return this.change(id, bytes, second + ".xrm");
    }

    public RestResponse change(String id, byte[] bytes, String filename) {
        return ClientUtils.upload(bApi("change/f/" + id), bytes, filename);
    }

    public RestResponse getOne(String id) {
        return ClientUtils.post(bApi("get/" + id));
    }

    private String bApi(String api) {
        return base + api;
    }
}
