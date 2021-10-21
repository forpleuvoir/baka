package forpleuvoir.baka.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.util
 * @class_name FileUtil
 * @create_time 2020/11/9 0:27
 */

public class FileUtil {

    /**
     * 将文件中的内容读取到字符串中
     *
     * @param file 文件对象
     * @return 内容
     * @throws IOException {@link FileNotFoundException} 文件未找到
     */
    public static String loadFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("file not found");
        }
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            result.append(System.lineSeparator()).append(s);
        }
        bufferedReader.close();
        return result.toString();
    }

    public static boolean checkFile(File file) {
        return file.exists();
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @param name 文件名
     * @return 成功创建的文件对象
     * @throws IOException 文件创建失败
     */
    public static File createFile(String path, String name) throws IOException {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(path + "/" + name);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param file 文件对象
     * @return 成功创建的文件对象
     * @throws IOException 文件创建失败
     */
    public static void createFile(File file) throws IOException {
        if (file.isDirectory()) {
           file.mkdirs();
        }else{
            File dir = new File(file.getParent());
            if (!dir.exists())
                dir.mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
        }
    }


    /**
     * 将字符串写入文件
     *
     * @param file    文件对象
     * @param content 写入的内容
     * @param append  是否从最后添加
     * @return 文件对象
     * @throws Exception
     */
    public static void writeFile(File file, String content, boolean append) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("file not found");
        }
        FileWriter fileWriter = new FileWriter(file, append);
        fileWriter.write(content);
        fileWriter.close();
    }

}
