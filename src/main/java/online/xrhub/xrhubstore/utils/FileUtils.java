package online.xrhub.xrhubstore.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

/**
 * @AUTHOR soft
 * @DATE 2018/11/19 19:05
 * @DESCRIBE 文件工具类
 */
public class FileUtils {

    public static String randomFilename(String filename) {
        String type = filename.substring(filename.lastIndexOf("."));
        long second = Instant.now().getEpochSecond();
        Double v = Math.random() * 1000;
        int value = v.intValue();
        return "" + value + second + type;
    }

    public static String getHost(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        return url.replace(uri, "");
    }

    public static boolean createFile(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file.createNewFile();
    }
}
