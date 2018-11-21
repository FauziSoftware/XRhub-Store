package online.xrhub.xrhubstore.utils;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

/**
 * @AUTHOR soft
 * @DATE 2018/10/30 14:55
 * @DESCRIBE 统一的Json返回类
 */
@Data
public class RestResponse<T> {
    private T payload;

    private boolean success;
    private String  msg;
    private int     code = 0;
    private long    timestamp;

    public RestResponse() {
        this.timestamp = nowUnix();
    }

    public RestResponse(boolean success) {
        this.timestamp = nowUnix();
        this.success = success;
    }

    public RestResponse(boolean success, T payload) {
        this.timestamp = nowUnix();
        this.success = success;
        this.payload = payload;
    }

    /**
     * 从数据payload中获取值
     * @param key 键
     */
    public Object getData(String key) {
        Map<String, Object> data = (Map<String, Object>) this.payload;
        return data.get(key);
    }

    public static boolean isOk(RestResponse restResponse) {
        return restResponse != null && restResponse.isSuccess();
    }

    public RestResponse<T> success(boolean success) {
        this.success = success;
        return this;
    }

    public RestResponse<T> payload(T payload) {
        this.payload = payload;
        return this;
    }

    public RestResponse<T> code(int code) {
        this.code = code;
        return this;
    }

    public RestResponse<T> message(String msg) {
        this.msg = msg;
        return this;
    }

    public static <T> RestResponse<T> ok() {
        return new RestResponse<T>().success(true).code(200);
    }

    public static <T> RestResponse<T> ok(T payload) {
        return new RestResponse<T>().success(true).payload(payload).code(200);
    }

    public static <T> RestResponse ok(T payload, int code) {
        return new RestResponse<T>().success(true).payload(payload).code(code);
    }

    public static <T> RestResponse<T> fail() {
        return new RestResponse<T>().success(false).code(404);
    }

    public static <T> RestResponse<T> fail(String message) {
        return new RestResponse<T>().success(false).message(message).code(404);
    }

    public static <T> RestResponse fail(int code, String message) {
        return new RestResponse<T>().success(false).message(message).code(code);
    }

    private long nowUnix() {
        return Instant.now().getEpochSecond();
    }
}
