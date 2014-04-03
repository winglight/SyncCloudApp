package com.yi4all.synccloud.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yi4all.synccloud.db.ClientModel;
import com.yi4all.synccloud.db.ServerModel;
import com.yi4all.synccloud.db.dto.SyncParam;

/**
 * Created by ChenYu on 13-8-8.
 */
public class BackService {
	
	public static Map<Long, SyncTask> syncTasks = Collections.synchronizedMap(new HashMap<Long, SyncTask>());

    public static void checkServerStatus(final ServerModel sm, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Message msg = handler.obtainMessage();

                HttpUtils.get(sm, "/ws/checkStatus",null, new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(String response) {
                        msg.arg1 = 0;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(Throwable throwable, String s) {
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }
    
    public static void syncServer(final Context context, final ServerModel sm, final Handler handler){
    	//get dbservice
        	if(syncTasks.containsKey(sm.getId())){
        		SyncTask task = syncTasks.get(sm.getId());
        		task.cancel(true);
        	}else{
        		//execute a task
        		SyncTask task = new SyncTask();
        		syncTasks.put(sm.getId(), task);
        		SyncParam sp = new SyncParam(context, sm, handler);
        		task.executeOnExecutor(SyncTask.SERIAL_EXECUTOR, sp);
        	}
        
    }
    
    private static boolean checkSyncTaskRunning() {
    	return false;
    }
}
