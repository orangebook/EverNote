package com.liaobb.evernote.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by S.King on 2015/10/14.
 */
public class CacheUtils {
    /***
     * 获取/data/data/files目录
     */
    public static File getFileDirectory(Context context) {
        File appCacheDir = null;
        if (appCacheDir == null) {
            appCacheDir = context.getFilesDir();
        }
        if (appCacheDir == null) {
            String cachDirPath = "/data/data/" + context.getPackageName() + "/files/";
            appCacheDir = new File(cachDirPath);
        }

        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context, String dirName) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Andorid"), "data");
        File appCacheDir2 = new File(new File(dataDir, context.getPackageName()), "File");
        File appCacheDir = new File(appCacheDir2, dirName);

        if (!appCacheDir.exists()) {
            return null;
        } else {
            try {
                new File(appCacheDir, ".files").createNewFile();

            } catch (IOException e) {
                Log.i(TAG, "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return  appCacheDir;
    }

    private static final String TAG = "CacheUtils";
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTENAL_STORAGE";

    private static boolean hasExernalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
