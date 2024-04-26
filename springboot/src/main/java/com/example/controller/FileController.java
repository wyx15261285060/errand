package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    // 文件上传存储路径
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${server.port:9090}")
    private String port;

    @Value("${ip:localhost}")
    private String ip;

    /**
     * 文件上传
     */
    /**
     * 文件上传接口
     *
     * @param file 用户上传的文件
     * @return 返回文件的访问URL
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String flag;
        // 使用同步代码块确保线程安全地生成标志符
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L); // 模拟处理时间
        }
        String fileName = file.getOriginalFilename(); // 获取文件原名
        try {
            // 检查文件存储目录是否存在，不存在则创建
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // 拼接文件存储路径：时间戳-文件名
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            System.out.println(fileName + "--上传成功");

        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        // 构造文件访问的HTTP URL
        String http = "http://" + ip + ":" + port + "/files/";
        return Result.success(http + flag + "-" + fileName);
    }


    /**
     * 获取文件
     *
     * @param flag
     * @param response
     */
    /**
     * 根据提供的标志(flag)获取对应的头像路径，并将其作为附件下载。
     *
     * @param flag 用于标识特定头像的字符串。
     * @param response 用于向客户端发送响应的HttpServletResponse对象。
     *
     * 注意：该方法不返回任何内容，而是将指定的头像文件以下载的方式响应给客户端。
     */
    @GetMapping("/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        try {
            // 如果标志非空，准备下载头像文件
            if (StrUtil.isNotEmpty(flag)) {
                // 设置响应头，指定文件下载名称和类型
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(flag, "UTF-8"));
                response.setContentType("application/octet-stream");
                // 读取并准备发送的头像文件内容
                byte[] bytes = FileUtil.readBytes(filePath + flag);
                os = response.getOutputStream();
                // 以字节流的方式向客户端输出文件内容
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            // 处理异常，打印文件下载失败信息
            System.out.println("文件下载失败");
        }
    }

    /**
     * 删除文件
     *
     * @param flag
     */
    @DeleteMapping("/{flag}")
    public void delFile(@PathVariable String flag) {
        FileUtil.del(filePath + flag);
        System.out.println("删除文件" + flag + "成功");
    }


}
