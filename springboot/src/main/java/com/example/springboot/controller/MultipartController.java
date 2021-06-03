package com.example.springboot.controller;

import cn.hutool.core.io.file.FileWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author : xiaoyu
 * @version : 1.0
 * @date : 2021/6/3 14:58
 * @project_name: blog-examples
 * @package_name : com.example.springboot.controller
 * @name: MuController
 * @description :
 */
@Slf4j
@RestController
@RequestMapping("file")
@AllArgsConstructor
public class MultipartController {

    ThreadPoolTaskExecutor executor;

    /**
     * 不会报错,因为Servlet流程没走完
     */
    @PostMapping("upload")
    public void upload(final MultipartFile file) throws IOException {
        FileWriter writer = new FileWriter("file/" + file.getOriginalFilename());
        writer.writeFromStream(file.getInputStream());
    }

    /**
     * 不会报错,因为file写入文件的时候，Servlet还没回收file
     */
    @PostMapping("upload1")
    public void upload1(final MultipartFile file) {
        executor.submit(new Runnable() {
            public void run() {
                try {
                    FileWriter writer = new FileWriter("file/" + file.getOriginalFilename());
                    writer.writeFromStream(file.getInputStream());
                } catch (IOException e) {
                    log.error("e:{}", e.getMessage());
                }
            }
        });
    }

    /**
     * 会报错,因为file的流随着Servlet回收而回收
     */
    @PostMapping("upload2")
    public void upload2(final MultipartFile file) {
        executor.submit(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10 * 1000L);
                    FileWriter writer = new FileWriter("file/" + file.getOriginalFilename());
                    writer.writeFromStream(file.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 不会报错,因为file.getInputStream() 是新创了个流对象,不会随着Servlet回收而回收
     */
    @PostMapping("upload3")
    public void upload3(final MultipartFile file) throws IOException {
        final InputStream inputStream = file.getInputStream();
        executor.submit(new Runnable() {
            @SneakyThrows
            public void run() {
                Thread.sleep(10 * 1000L);
                FileWriter writer = new FileWriter("file/" + file.getOriginalFilename());
                writer.writeFromStream(inputStream);
            }
        });
    }

}
