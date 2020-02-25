package com.xiaoshu.test;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestPOI {

    @Test
    public void testOut(){

        List<Student> students=new ArrayList<Student>();
        students.add(new Student(1,"aa"));
        students.add(new Student(2,"bb"));
        students.add(new Student(3,"cc"));
        students.add(new Student(4,"dd"));

        //创建一个WorkBook对象
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        //创建一个Sheet
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //创建标题页
        HSSFRow titlerow = sheet.createRow(0);

        titlerow.createCell(0).setCellValue("id");
        titlerow.createCell(1).setCellValue("姓名");

        for (Student student : students) {
            //获取最后一行的行号
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(student.getId());
            dataRow.createCell(1).setCellValue(student.getName());
        }
        //按照文件输出
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\poiDaoChu\\student.xls");
            hssfWorkbook.write(fileOutputStream);
            hssfWorkbook.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIn(){
        try {
            //读取文件
            FileInputStream fileInputStream = new FileInputStream("D:\\poiDaoChu\\student.xls");
            //创建一个WorkBook对象
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
            //获取sheet
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            //获取总行数
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                HSSFRow dataRow = sheet.getRow(i);
                int id=(int)dataRow.getCell(0).getNumericCellValue();
                String name = dataRow.getCell(1).getStringCellValue();
//                Student student = new Student();
                System.out.println(id);
                System.out.println(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
