package com.topdon.remoteservice.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogUtil {
    private static final int LEVEL_FILE = 0x2;

    public static void writerLog(String tag, String msg) {
//        writerLog(LEVEL_FILE, tag, msg);
    }

    /**
     * 路径 "/storage/emulated/0/lowTemperatureTest"
     *
     * @param msg 需要打印的内容
     */
    public static void writerLog(int logLevel, String tag, String msg) {
//        if (LEVEL_FILE == logLevel) {
//            //保存到的文件路径
////            AppUtils.mApplication.getExternalFilesDir("lo")
//            final String filePath = AppUtils.mApplication.getExternalFilesDir("log");
//
//            FileWriter fileWriter;
//            BufferedWriter bufferedWriter = null;
//
//            try {
//                //创建文件夹
//                File dir = new File(filePath, "lowTemperatureTest");
//                if (!dir.exists()) {
//                    dir.mkdir();
//                }
//                //创建文件
//                File file = new File(dir, "lowTemperature.txt");
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//                //写入日志文件
//                fileWriter = new FileWriter(file, true);
//                bufferedWriter = new BufferedWriter(fileWriter);
//                bufferedWriter.write(getCurrentTime() + "____" + tag + ":  " + msg);
//                bufferedWriter.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (bufferedWriter != null) {
//                    try {
//                        bufferedWriter.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        } else {
//            Log.d("lowTemperature", msg + "");
//        }
//

    }

    private static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

}
