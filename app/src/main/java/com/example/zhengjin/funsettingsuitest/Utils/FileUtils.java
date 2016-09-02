package com.example.zhengjin.funsettingsuitest.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.TestApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by zhengjin on 2016/8/26.
 *
 * Include utils for file handler.
 */
public final class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();
    private static final TestApplication CONTEXT = TestApplication.getInstance();

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private static File getExternalStorageDir() {
        return isExternalStorageAvailable() ? Environment.getExternalStorageDirectory() : null;
    }

    public static String getExternalStoragePath() {
        File file = getExternalStorageDir();
        if (file == null) {
            Log.e(TAG, "The external storage (sdcard) is not available!");
            return "";
        }
        return file.getAbsolutePath();
    }

    public static String readFileSdcard(String filePath) {
        BufferedReader br = null;
        String data = "";

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            data = sb.toString();
        } catch (IOException e) {
            CONTEXT.logException(TAG, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    CONTEXT.logException(TAG, e);
                }
            }
        }

        return data.substring(0, (data.length() - 1));
    }

    public static boolean writeFileSdcard(String filePath, String fileContent) {
        return writeFileSdcard(filePath, fileContent, false);
    }

    public static boolean writeFileSdcard(String filePath, String fileContent, boolean flagAppend) {
        boolean ret = false;
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    new File(filePath), flagAppend)));
            bw.write(fileContent);
            ret = true;
        } catch (IOException e) {
            CONTEXT.logException(TAG, e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    CONTEXT.logException(TAG, e);
                }
            }
        }

        return ret;
    }

    public static long getDirTotalSize(String dirPath) {
        if (StringUtils.isEmpty(dirPath) || !isFileExist(new File(dirPath))) {
            return 0L;
        }
        StatFs sf = new StatFs(dirPath);
        // bytes to Mb
        return (sf.getBlockCountLong() * sf.getBlockSizeLong() / 1024L / 1024L);
    }

    public static long getDirFreeSize(String dirPath) {
        if (StringUtils.isEmpty(dirPath) || !isFileExist(new File(dirPath))) {
            return 0L;
        }
        StatFs sf = new StatFs(dirPath);
        // bytes to Mb
        return (sf.getAvailableBlocksLong() * sf.getBlockSizeLong() / 1024L / 1024L);
    }

    private static boolean isFileExist(File file) {
        if (file.exists()) {
            return true;
        } else {
            Log.w(TAG, String.format(CONTEXT.mLocale,
                    "The file (%s) is not found!", file.getAbsolutePath()));
            return false;
        }
    }

    public static boolean deleteFile(File file) {
        boolean ret = false;
        if (isFileExist(file)) {
            if (file.delete()) {
                ret = true;
            } else {
                Log.d(TAG, String.format(CONTEXT.mLocale,
                        "File (%s) delete failed!", file.getAbsolutePath()));
            }
        }

        return ret;
    }

}
