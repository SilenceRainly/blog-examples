package com.example.media.ffmpeg.hls.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.example.media.ffmpeg.hls.processor.FFmpegProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author : xuansy
 * @version : 1.0
 * @date : 2021/5/21 22:45
 * @project_name: ffmpeg-demo
 * @package_name : com.example.ffmpeg.demo.controller
 * @name: TestController
 * @email: 1292798418@qq.com
 * @description :
 */
@RestController
public class M3u8Controller {

    /**
     * 目录路径
     */
    private static final String PATH = "D:\\test\\";

    @PostMapping("uploadToM3u8")
    public void uploadToM3u8() throws Exception {
        FileInputStream inputStream = new FileInputStream(PATH + "test.mp4");
        String m3u8Url = "http://localhost:8080/upload/test.m3u8";
        String infoUrl = "http://localhost:8080/preview/test.info";
        FFmpegProcessor.convertMediaToM3u8ByHttp(inputStream, m3u8Url, infoUrl);
    }

    @PostMapping("upload/{fileName}")
    public void upload(HttpServletRequest request, @PathVariable("fileName") String fileName) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        FileWriter writer = new FileWriter(PATH + fileName);
        writer.writeFromStream(inputStream);
        IoUtil.close(inputStream);
    }

    /**
     * 预览加密文件
     */
    @PostMapping("preview/{fileName}")
    public void preview(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        FileReader fileReader = new FileReader(PATH + fileName);
        fileReader.writeToStream(response.getOutputStream());
    }

    /**
     * 预览加密文件
     */
    @GetMapping("download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        FileReader fileReader = new FileReader(PATH + fileName);
        fileReader.writeToStream(response.getOutputStream());
    }

}
