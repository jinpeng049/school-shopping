package com.zj.shop.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by LiZhengYong on 2016/6/17.
 */
@Configuration
@AutoConfigureAfter(Configurator.class)
public class MyBatisMapperScannerConfigurer {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.zj.school.sh.dao,com.zj.school.sh.mapper.inter");
        return mapperScannerConfigurer;
    }

}
