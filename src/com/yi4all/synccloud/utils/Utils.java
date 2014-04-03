package com.yi4all.synccloud.utils;

import java.io.File;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.yi4all.synccloud.db.ClientModel;

public class Utils {
	
	public final static String spliter = File.separator;

	public static String getClientFolder(Context context, String name){
		File file = getDiskCacheDir(context, name);
		if(!file.exists()){
			file.mkdirs();
		}
		String res = file.getAbsolutePath();
		if(!res.endsWith(spliter)){
			res += spliter;
		}
		return res;
	}
	
	public static String getParentFolderPath(String subPath){
		String path = spliter;
		int position = subPath.lastIndexOf(spliter);
		if(position > 0){
			path = subPath.substring(0, position);
		}
		
		return path;
	}
	
	public static String convertLocalPath(String path){
		return path.replace("\\\\", spliter);
	}
	
	public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir() :
                                context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }
	
	@TargetApi(9)
    public static boolean isExternalStorageRemovable() {
        if (Utils.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
	
	@TargetApi(8)
    public static String getExternalCacheDir() {
        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/synccloud/";
        return Environment.getExternalStorageDirectory().getPath() + cacheDir;
    }
	
	public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
    
    public static String getKBSize(long size){
    	if(size < 1024){
    		return size + "KB";
    	}else if(size >= 1024 && size < 1024*1024){
    		return size/1024 + "MB";
    	}else{
    		return size/(1024*1024) + "GB";
    	}
    }
}
