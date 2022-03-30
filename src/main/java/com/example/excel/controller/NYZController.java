package com.example.excel.controller;

import com.example.excel.bean.DailyHQL;
import com.example.excel.service.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@RestController
@Slf4j
public class NYZController {
    @Autowired
    private ExcelReader excelReader;

    @Value("${spring.datasource.driver-class-name}")
    private String JDBC_DRIVER;

    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${filepath}")
    private String filepath;

    @PostMapping("read")
    public void read(@RequestParam("file") MultipartFile file) throws IOException, SQLException, ClassNotFoundException, ParseException, InvalidFormatException {
        File targetFile = new File(filepath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(filepath + file.getOriginalFilename());){
            out.write(file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败!");
        }
        log.info("文件上传成功!");
        String str=filepath+file.getOriginalFilename();
        List<DailyHQL> dataList = excelReader.read(str);
        System.out.println(dataList);
        excelReader.write2db(dataList);
        File file1 = new File(str);
        if (file1!=null){
            file1.delete();
        }
    }
}
