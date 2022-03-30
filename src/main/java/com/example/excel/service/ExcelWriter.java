package com.example.excel.service;

import com.example.excel.bean.DailyHQL;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {
    public void write(String fileName, List<DailyHQL> list)  {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("DailyHQL");

        // 创建Excel标题行，第一行
        HSSFRow headRow = sheet.createRow(0);
        headRow.createCell(0).setCellValue("UpdateTime");
        headRow.createCell(1).setCellValue("ZhanName");
        headRow.createCell(2).setCellValue("HQL");

        // 往Excel表中遍历写入数据
        for (DailyHQL dailyHQL : list) {
            createCell(dailyHQL, sheet);
        }

        File xlsFile = new File(fileName);
        try {
            // 或者以流的形式写入文件 workbook.write(new FileOutputStream(xlsFile));
            workbook.write(xlsFile);
        } catch (IOException e) {
            // TODO
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                // TODO
            }
        }
    }

    // 创建Excel的一行数据。
    private void createCell(DailyHQL dailyHQL, HSSFSheet sheet) {
        HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
        dataRow.createCell(0).setCellValue(dailyHQL.getUpdateTime());
        dataRow.createCell(1).setCellValue(dailyHQL.getZhanName());
        dataRow.createCell(2).setCellValue(dailyHQL.getHQL());
    }
}
