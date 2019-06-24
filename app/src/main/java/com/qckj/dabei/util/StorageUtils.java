package com.qckj.dabei.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class StorageUtils {
    /**
     * 检查是否有Sdcard
     *
     * @return
     */
    public static Boolean checkSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SDCard目录
     *
     * @return
     */
    public static String getSdCardDir() {
        String path = null;
        if (checkSdcard()) {
            path = Environment.getExternalStorageDirectory().getPath();
        }

        return path;
    }

    /**
     * SDCard剩余空间
     *
     * @param a_lFileLength
     * @return boolean
     */
    public static boolean isSDCardAvailableBlocks(long a_lFileLength) {
        File l_path = Environment.getExternalStorageDirectory();
        StatFs l_statfs = new StatFs(l_path.getPath());
        long llAvailaBlock = l_statfs.getAvailableBlocks();
        long llBlockSize = l_statfs.getBlockSize();

        return a_lFileLength < (llAvailaBlock * llBlockSize);
    }

    /**
     * 关闭流
     *
     * @param closeable
     */
    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
