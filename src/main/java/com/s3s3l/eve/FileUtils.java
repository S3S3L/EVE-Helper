package com.s3s3l.eve;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.google.common.io.Resources;

/**
 * 文件操作工具类 <br>
 * ClassName: FileUtils <br>
 * date: 2015年12月21日 上午11:49:14 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public class FileUtils {
    public static final String CLASS_PATH_PRIFIX = "classpath:";
    public static final String FILE_PATH_PRIFIX = "file:";

    /**
     * 
     * Get full path from relative path or class path.
     * 
     * @param path
     *            Path string with prifix {@value #CLASS_PATH_PRIFIX} for
     *            classpath or {@value #FILE_PATH_PRIFIX} for relative path or
     *            non prifix for both (will first check relative path. if not
     *            exist, then return classpath).
     * @return full path string.
     * @since JDK 1.8
     */
    public static String getFullFilePath(String path) {
        if (path.startsWith(CLASS_PATH_PRIFIX)) {
            return mapLocalFullPath(path.replaceFirst(CLASS_PATH_PRIFIX, "")).toString();
        } else if (path.startsWith(FILE_PATH_PRIFIX)) {
            return new File(path.replaceFirst(FILE_PATH_PRIFIX, "")).getAbsolutePath();
        } else {
            File classpath = mapLocalFullPath(path).toFile();
            File filepath = new File(path);
            if (filepath.exists()) {
                return filepath.getAbsolutePath();
            } else {
                return classpath.getAbsolutePath();
            }
        }
    }

    /**
     * 
     * Get full path from relative path or class path.
     * 
     * @param paths
     *            Path string with prifix {@value #CLASS_PATH_PRIFIX} for
     *            classpath or {@value #FILE_PATH_PRIFIX} for relative path or
     *            non prifix for both (will first check relative path. if not
     *            exist, then return classpath).
     * @return full path string.
     * @since JDK 1.8
     */
    public static File getFirstExistFile(String... paths) {
        for (String path : paths) {
            File file = new File(getFullFilePath(path));
            if (file.exists()) {
                return file;
            }
        }

        return null;
    }

    /**
     * 
     * 读取整个文件内容
     * 
     * @author kehw_zwei
     * @param filepath
     *            路径
     * @param charset
     *            编码
     * @return 文件的文本内容
     * @throws IOException
     *             Signals that an I/O exception of some sort has occurred. This
     *             class is the general class of exceptions produced by failed
     *             or interrupted I/O operations.
     * @since JDK 1.8
     */
    public static String readToEnd(String filepath, Charset charset) throws IOException {
        StringBuilder buf = new StringBuilder();
        try (Stream<String> fileStream = Files.lines(Paths.get(filepath), charset)) {
            fileStream.forEach(line -> buf.append(line));
        }
        return buf.toString();
    }

    /**
     * 
     * 读取整个文件内容
     * 
     * @author kehw_zwei
     * @param filepath
     *            路径
     * @param charset
     *            编码
     * @return 文件的文本内容
     * @throws IOException
     *             Signals that an I/O exception of some sort has occurred. This
     *             class is the general class of exceptions produced by failed
     *             or interrupted I/O operations.
     * @since JDK 1.8
     */
    public static String readToEnd(File File, Charset charset) throws IOException {
        StringBuilder buf = new StringBuilder();
        try (Stream<String> fileStream = Files.lines(File.toPath(), charset)) {
            fileStream.forEach(line -> buf.append(line));
        }
        return buf.toString();
    }

    /**
     * 
     * 相对路径转化为绝对路径
     * 
     * @author kehw_zwei
     * @param path
     *            相对路径
     * @return 绝对路径
     * @throws FileNotFoundException
     * @since JDK 1.8
     */
    public static Path mapLocalFullPath(String path) {
        String pathStr = FileUtils.class.getResource("/").getPath().concat(path);
        String OS = System.getProperty("os.name").toLowerCase();
        if ((OS.indexOf("win") >= 0)) {
            pathStr = pathStr.substring(1);
        }
        Path localPath = Paths.get(pathStr);

        return localPath;
    }

    /**
     * 
     * 获取资源文件的绝对路径
     * 
     * @author kehw_zwei
     * @param path
     *            相对路径
     * @return 绝对路径
     * @throws IllegalArgumentException
     *             - if the resource is not found
     * @since JDK 1.8
     */
    public static String mapResource(String path) throws IllegalArgumentException {
        return Resources.getResource(path).getPath();
    }
}
