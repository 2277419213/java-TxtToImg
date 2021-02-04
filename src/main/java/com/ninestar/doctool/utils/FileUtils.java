package com.ninestar.doctool.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FileUtils {

    private static final String defaultPath = "C:/downloadCode/";//下载路径
    private static final String needFileName[] = {"components","https","router","store","util","views"};//需要生成图片的文件名称，目前只支持src文件下

    public static String getTimename(){//获取当前时间作为名字
        Date nowdate = new Date();
        SimpleDateFormat timename = new SimpleDateFormat("HH:mm:ss");
        return timename.format(nowdate).replaceAll(":","_");
    }

    public static String downloadPath(){
        Calendar now = Calendar.getInstance();
        int YY = now.get(Calendar.YEAR);
        int HH = now.get(Calendar.MONTH) + 1;
        int DD = now.get(Calendar.DAY_OF_MONTH);
        String filename = YY + "_" + (HH < 10 ? "0" + HH : HH) + "_" + (DD < 10 ? "0" + DD : DD);
        String downloadPath = defaultPath + filename;
        File file =new File(downloadPath);//如果文件夹不存在则创建
        if  (!file .exists()  && !file .isDirectory()){
            file.mkdir();
            System.out.println("文件夹不存在，已创建文件夹");
        }
        return downloadPath + "/";
    }

    public static String uncompress(String zippath) throws Exception {
        String filepath = zippath.replaceAll(".zip","")+"/";
        File file =new File(filepath);//如果文件夹不存在则创建
        if  (!file.exists() && !file.isDirectory()){
            file.mkdir();
        }
        ZipFile zFile = new ZipFile(new File(zippath));// 首先创建ZipFile指向磁盘上的.zip文件
        zFile.extractAll(filepath);// 将文件抽出到解压目录
        return convertImage(filepath);
    }

    public static String convertImage(String filePath) throws Exception {
        String srcFilePath = filePath+"src/";
        List<String> namelist = new ArrayList<>();//用来存文件路径名称
        for (int i = 0;i<needFileName.length;i++){
            File file =new File(srcFilePath + needFileName[i]);//如果文件夹不存在则创建
            if  (file.exists() && file.isDirectory()){
                List<String> childNamelList = FixFileName(file,"src/"+needFileName[i],filePath);
                namelist.addAll(childNamelList);
//                System.out.println(needFileName[i]+"文件改名");
            }
        }
        if(namelist.size() > 0){
            //如果有内容就生成图片
            return generateImage(filePath,namelist);
        }
        System.out.println("读取完成"+namelist);
        return "读取完成,没有指定内容";
    }

    private static String generateImage(String filePath, List<String> nameList) throws Exception {
        //先在同级创建一个文件夹存放图片
        String imgFolderPath = filePath+getTimename()+"_CaptureImages/";
        File imgFolder = new File(imgFolderPath);
        imgFolder.mkdir();
        writeFileContext(nameList,(imgFolderPath + "nameList.txt"));
        for(int i = 0;i<nameList.size();i++){
            String childname = nameList.get(i).toString();
            String firshname = childname.substring(0,childname.lastIndexOf("."));
            String childPath = filePath+firshname+".txt";
            File textFile = new File(childPath);
            File imageFile  = new File(imgFolderPath+firshname.replaceAll("/","_")+".png");
            TextToImage texttoimage = new TextToImage(textFile, imageFile);
            texttoimage.convert();
        }
        return compress(imgFolderPath);
    }

    private static String compress(String path) throws ZipException {
        String zipPath = path.substring(0,path.lastIndexOf("/")) + ".zip";
        ZipFile zipFile = new ZipFile(zipPath);
        File folder = new File(path);
        File[] folders = folder.listFiles();
        for(File file : folders){
            zipFile.addFile(file);
        }
        return "生成压缩文件:"+zipPath;
    }

    static void writeFileContext(List<String>  strings, String path) throws Exception {
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String l:strings){
            writer.write(l + "\r\n");
        }
        writer.close();
    }

    private static List<String> FixFileName(File file, String fathername, String fatherpath){
        List<String> thisnamelist = new ArrayList<>();//这个文件夹的文件路径名称
        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
            String childpath = fathername+"/"+filelist[i];
            File childFile = new File(fatherpath + childpath);
            if(childFile.isDirectory()) {
                List<String> childNamelList = FixFileName(childFile,childpath,fatherpath);
                thisnamelist.addAll(childNamelList);
            }else{
                thisnamelist.add(childpath);
//                System.out.println(childpath);
                String newFilePath = childpath.substring(0, childpath.lastIndexOf("."));
                File nf = new File(fatherpath + newFilePath + ".txt");
                childFile.renameTo(nf);
            }
        }
        return thisnamelist;
    }
}
