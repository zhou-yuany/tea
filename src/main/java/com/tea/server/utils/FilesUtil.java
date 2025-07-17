package com.tea.server.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FilesUtil {
    /**
     * 创建文件夹和文件 例如：C:/aaa/bbb/ccc.txt,若此路径存在则创建ccc.txt<br>
     * 若此路径不存在则先创建文件夹再创建文件。<br>
     * @param file
     * @throws IOException
     */
    public static boolean createDirectoryAndFile(File file) throws IOException {
        String path = file.getPath();
        String name = file.getName();
        if(name.indexOf(".") > -1){
            String directoryStr = path.substring(0, path.length()-name.length());
            File directory = new File(directoryStr);
            if(!directory.exists()){
                if(directory.mkdirs() && FilesUtil.createFile(file)){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 用来创建文件<br>
     * 在文件的文件夹路径存在时使用，<br>
     * 文件夹路径不存在时会抛异常<br>
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean createFile(File file) throws IOException {
        RandomAccessFile accessFile = null;
        try {
            if (!file.exists()) {
                accessFile = new RandomAccessFile(file, "rw");
                return true;
            }else{
                if(file.isDirectory()){
                    throw new IOException(file.getPath()+" 已存在, 但是它是文件夹");
                }else{
                    return true;
                }
            }
        } catch (Exception e) {
            throw e;
        } finally{
            if(accessFile != null){
                accessFile.close();
            }
        }
    }


}
