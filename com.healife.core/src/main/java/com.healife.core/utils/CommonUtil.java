package com.healife.core.utils;

import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 该类用于实现模块中的公用方法
 * Created by wilson on 2016/12/7.
 */
public class CommonUtil {

    /**
     * java 根据某个集合生成excel工具类（用户指定显示那几个字段作为表中的标题）
     * 所用工具类：阿帕奇的POI jar包依赖
     * 该方法实现java读取数据库中的数据导出到excel中
     * @param sheetName -----------（对应表名，用户自定义的）
     * @param headersName -----------一个String数组（对应表中的title ，前台页面传过来的）
     * @param list -----------一个泛型集合数据（对应原始表中所有数据）
     */
    public static <T> String genExcelByUser(String sheetName , String[] headersName , List<T> list){

            //最终返回的excel生成的地址
            String fileAbsPath;
            // 声明一个工作薄
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);
            sheet.setDefaultColumnWidth((short)25);

            // 生成一个样式
            HSSFCellStyle style = wb.createCellStyle();//单元格样式
            HSSFRow row = sheet.createRow(0);//第一行表格标题列

            //样式设置
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

            //声明一个单元格
            HSSFCell cell;

            //定义表格title ,将所有的title中的字段放进一个map中先存起来
            Map<Integer , String> map = new HashMap<Integer , String>();

            int key = 0;
            for(int i = 0 ; i < headersName.length ; i++){
                if(!headersName[i].equals(null)){
                    map.put(key , headersName[i]);

                }
                key++;
            }

            //获取所有title中要显示的字段值
            Collection c = map.values();
            //表格标题创建 遍历表头字段值====================================
            Iterator<String> it = c.iterator();
            short size = 0;
            while(it.hasNext()){
                cell = row.createCell(size);
                cell.setCellValue(it.next().toString());
                cell.setCellStyle(style);
                size++;
            }
            //表格标题创建结束===============================================

            //表格字段值创建====================================================
            Collection<String> zdC = map.values();
            //获取表中展示字段值的迭代器
            Iterator<T> labIt = list.iterator();
            int zdRow = 0;
            while(labIt.hasNext()){
                int zdCell = 0;//表中内容行的单元格下标
                zdRow++;//表中行下标
                row = sheet.createRow(zdRow);//创建第二行（即表中内容开始的行）

                T t = (T)labIt.next();
                //利用java反射实现动态获取该T对象的的属性，动态调用方法，获取属性值
                Field[] fields = t.getClass().getDeclaredFields();
                Iterator<String> zdIt = zdC.iterator();//获取所要显示的字段值的迭代器
                while(zdIt.hasNext()){

                    String zdItName = zdIt.next();

                    for(short i = 0 ; i < fields.length ;i ++){
                        Field field = fields[i];
                        String fieldName = field.getName();
                        //logger.info(fieldName+":=================================" );
                        //对比
                        if(zdItName.equals(fieldName)){
                            //获取完整方法名：getXxx();
                            StringBuilder getMethodName = new StringBuilder();
                            getMethodName.append("get");
                            getMethodName.append(fieldName.substring(0,1).toUpperCase());
                            getMethodName.append(fieldName.substring(1));

                            //logger.info(getMethodName.toString());
                            //准备执行方法获取对象的属性值
                            Class tcls = t.getClass();
                            try{
                                //获取到方法
                                Method getMethod = tcls.getMethod(getMethodName.toString() , new Class[]{});
                                //开始执行
                                Object val = getMethod.invoke(t,new Object[]{});
                                //logger.info(val);
                                String textVal = null;
                                if(val !=null){
                                    textVal = val.toString();
                                }
                                //创建相同属性名（即用户自定义的字段）的但与个的值
                                row.createCell((short)zdCell).setCellValue(textVal);
                                zdCell++;
                            }catch (NoSuchMethodException e){
                                e.printStackTrace();
                            }catch (IllegalAccessException e){
                                e.printStackTrace();
                            }catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            //表格字段值创建结束=======================================================

            //接下来进行导出工作
            fileAbsPath = "C:\\Users\\healife-605\\Desktop\\实验室.xls";//此处定义的是死的windows系统的地址，需要修改为服务器的地址
            try {
                FileOutputStream xls = new FileOutputStream(fileAbsPath);
                wb.write(xls);
                xls.close();
                //JOptionPane.showMessageDialog(null, "导出成功!");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "导出失败!");
                e.printStackTrace();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "导出失败!");
                e.printStackTrace();
            }
        return fileAbsPath;
    }


    /**
     * 该方法实现导出下载excel表格的功能
     * 可以在控制层将生成excel和下载excel放在一起，生成的同时就自动下载了
     */
    public static void downLoadExcel(String filePath , HttpServletResponse response){

        File file = new File(filePath);
        String fileName = file.getName();//获取文件名

        //拿到文件的后缀名
        String end = fileName.substring(fileName.lastIndexOf(".")+1).toUpperCase();
        //下载文件
        InputStream fileInputStream = null;
        try {
            fileInputStream = new BufferedInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[0];
        try {
            buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.reset();//清空response;
        //设置response的header
        response.addHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes()));
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = null;
        try {
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
