package com.example.excel.service;

import com.example.excel.bean.DailyHQL;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExcelReader {

    @Value("${spring.datasource.driver-class-name}")
    private String JDBC_DRIVER;

    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    private static Connection conn;
    private static PreparedStatement pst;

    public List<DailyHQL> read(String fileName) throws EncryptedDocumentException, IOException, ParseException, InvalidFormatException {
        if (fileName == null) return null;

        File xlsFile = new File(fileName);
        if (!xlsFile.exists()) return null;

        // 工作表
        Workbook workbook = WorkbookFactory.create(xlsFile);
        // 表个数
        int numberOfSheets = workbook.getNumberOfSheets();
        if (numberOfSheets <= 0) return null;

        List<DailyHQL> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        int rowNumbers = sheet.getLastRowNum() + 1;
        DailyHQL dailyhql;
        for (int row = 1; row < rowNumbers; row++) {
            Row r = sheet.getRow(row);
            //我们只需要前两列
            if (r.getPhysicalNumberOfCells() >= 3) {
                long datems  = r.getCell(0).getDateCellValue().getTime();
                java.sql.Date  sqlDate = new java.sql.Date(datems);
                dailyhql = new DailyHQL(sqlDate, r.getCell(1).toString(),Float.parseFloat(r.getCell(2).toString()));
                list.add(dailyhql);
            }
        }
        workbook.close();
        return list;
    }

    public void write2db(List<DailyHQL> dailyhqls) throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, user, password);
        for(int i=0;i<dailyhqls.size();i++){
            String sql1="delete from nyzhql where ZhanName='"+dailyhqls.get(i).getZhanName()+"' and UpdateTime='"+dailyhqls.get(i).getUpdateTime()+"'";
            pst = conn.prepareStatement(sql1);
            pst.executeUpdate(sql1);
            String sql2 = "insert into nyzhql(UpdateTime,ZhanName,HQL) VALUES('"+dailyhqls.get(i).getUpdateTime()+"','"+dailyhqls.get(i).getZhanName()+"',"+dailyhqls.get(i).getHQL()+")";
            pst = conn.prepareStatement(sql2);
            pst.executeUpdate(sql2);
        }
    }

    public static void main(String[] args) throws Exception{
        // 第一步：读取数据
        List<DailyHQL> dataList = new ExcelReader().read("D://test/excel/test1.xlsx");
        System.out.println(dataList);
        new ExcelReader().write2db(dataList);
        // 第二步：排序
//        Collections.sort(dataList);
//        System.out.println(dataList);
        // 第三部：写入数据
//      new ExcelWriter().write("output.xls", dataList);
    }
}