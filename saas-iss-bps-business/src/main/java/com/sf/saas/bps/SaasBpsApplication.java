package com.sf.saas.bps;

import cn.hutool.core.net.NetUtil;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.sf.saas.bps.core.service.annotation.EnableMybatisPlus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 01407460
 * @date 2022/9/2 18:52
 */
@Slf4j
@EnableSwagger2
@EnableMybatisPlus
@EnableScheduling
@EnableApolloConfig
@SpringBootApplication
@EnableAsync
public class SaasBpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaasBpsApplication.class, args);
        String localHostName = NetUtil.getLocalHostName();
        log.info("springboot run successful... HostName is {}",localHostName);
    }
}
