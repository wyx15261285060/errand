package com.example.common.config;/*
 *@author yviang wi
 *@date  2024/5/12 21:12
 *@version 1.0
 *@email：3060491854@qq.com
 */


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");//是否有边框 yes有 no没有
        properties.setProperty("kaptcha.border.color", "34,114,200");//验证码字体颜色
        properties.setProperty("kaptcha.image.width", "200");//验证码图片的宽
        properties.setProperty("kaptcha.image.height", "40"); //验证码图片的高
        //properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");//验证码位数
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Arial Narrow,Serif,Helvetica,Tahoma,Times New Roman,Verdana"); //验证码字体类型
        properties.setProperty("kaptcha.textproducer.font.size", "38");//验证码字体大小

        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
