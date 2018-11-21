package online.xrhub.xrhubstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"online.xrhub.xrhubstore.mapper"})
@EnableTransactionManagement
public class XrhubStoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(XrhubStoreApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(XrhubStoreApplication.class);
    }
}
