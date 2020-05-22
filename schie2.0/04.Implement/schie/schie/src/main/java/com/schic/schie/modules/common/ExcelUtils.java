/**
 *
 */
package com.schic.schie.modules.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leo
 * @description 工具类
 * @date 2020/3/13 14:53
 **/
public class ExcelUtils {

    /**
     * 扫描指定盘符下的文件
     * 将xlsx和xls结尾的文件绝对路径装入list返回
     * @param diskPath 磁盘绝对路径
     * @return 文件绝对路径 集合
     */
    public static List searchPath(String diskPath){
        File file = new File(diskPath);
        File[] files = file.listFiles();
        List list = new ArrayList();
        if (null!=files){
            for (File file1 : files) {
                //如果是目录
                if (file1.isDirectory()){
                    List list1 = searchPath(file1.getPath());
                    for (Object o : list1) {
                        list.add(o.toString());
                    }
                }
                //如果是文件
                if (file1.isFile() && file1.getName().endsWith(".xls")){
                    list.add(file1.getPath());
                }
                if (file1.isFile() && file1.getName().endsWith(".xlsx")){
                    list.add(file1.getPath());
                }
            }
        }
        return list;
    }

    /**
     * 根据具体的文件生成不同的工作对象workBook
     * @param filePath 文件的绝对路径
     * @return 工作对象wb
     */
    public static Workbook creatWorkBook(String filePath) throws IOException {
        if (filePath.contains("~$")){
            filePath = filePath.replace("~$", "");
        }
        File file = new File(filePath);
        Workbook wb = null;
        try (FileInputStream fis = new FileInputStream(file);   //文件流对象
        ){
            //判断文件是否存在  也可用 以xls结尾
            if (file.isFile() && file.exists()) {
                String[] split = file.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                if ( "xls".equals(split[1])){
                    try {
                        wb = new HSSFWorkbook(fis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if ("xlsx".equals(split[1])) {
                    try {
                        wb = new XSSFWorkbook(new BufferedInputStream(fis));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    System.out.println("文件类型错误!");
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 根据工作对象wb  获得该表格的sheet的数量
     * @param wb 工作对象
     * @return sheet数量
     */
    public static Integer getNumOfSheet(Workbook wb){
        int numOfSheet = 0;
        if (wb != null) {
            numOfSheet = wb.getNumberOfSheets();
        }
        return numOfSheet;
    }
}
