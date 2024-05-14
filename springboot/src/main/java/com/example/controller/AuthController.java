package com.example.controller;/*
 *@author yviang wi
 *@date  2024/5/12 21:44
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.common.Result;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Resource
    private DefaultKaptcha captchaProducer;
    @Resource
    RedisTemplate<String, String> redisTemplate;
    /*
     *获取验证码端口
     * */
    @GetMapping("/captcha")
    public Result getKaptcha() throws Exception {
        //生成验证码
        String key = captchaProducer.createText();
        //生成图片
        BufferedImage image = captchaProducer.createImage(key);
        // 将验证码转换为Base64编码
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);
        //读出转换为Base64编码的值
        String base64Code = Base64.getEncoder().encodeToString(stream.toByteArray());
        stream.close();
        Map<String, String> kaptchaVoMap = new HashMap<>();
        //生成一个随机的key，到时候前端验证时候带这个key过来查询验证码是否正确
        String uuid = UUID.randomUUID().toString();
        kaptchaVoMap.put("uuid", uuid);
        kaptchaVoMap.put("code", "data:image/png;base64," + base64Code);
        //将验证码放入到redis中去
        redisTemplate.opsForValue().set(uuid, key, 60L, TimeUnit.SECONDS);
        return Result.success(kaptchaVoMap);
    }
}
