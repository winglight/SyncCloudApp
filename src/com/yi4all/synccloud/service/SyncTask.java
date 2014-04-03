package com.yi4all.synccloud.service;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.yi4all.synccloud.db.ClientModel;
import com.yi4all.synccloud.db.dto.SyncParam;
import com.yi4all.synccloud.utils.Utils;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SyncTask extends AsyncTask<SyncParam, Integer, Object>{

	private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(40);
    
    private static final ThreadFactory  sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    
    public static final Executor THREAD_POOL_EXECUTOR
    = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
    TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory,
    new ThreadPoolExecutor.DiscardOldestPolicy());
    
    public static final Executor SERIAL_EXECUTOR = Utils.hasHoneycomb() ? new SerialExecutor() :
        Executors.newSingleThreadExecutor(sThreadFactory);
    
    private Long smId;
    
	@Override
	protected Object doInBackground(SyncParam... params) {
		final SyncParam param = params[0];
		
		this.smId = param.sm.getId();
		
		//get dbservice
    	DBService dbService = DBService.getInstance(param.context);
		
		final Message msg = param.handler.obtainMessage();
		
		SyncHttpClient syncClient = new SyncHttpClient() {
			
			@Override
			public String onRequestFailed(Throwable error, String str) {
				msg.arg1 = 1;// error
                Bundle bundle = new Bundle();
                bundle.putString("error", str);
                msg.setData(bundle);
                param.handler.sendMessage(msg);
				return null;
			}
		};
		
		//1.register client, if not client 
        ClientModel cm = dbService.getClient4Server(param.sm);
        if(cm == null){
        	if(isCancelled()) return null;
        	
        	//register this client
        	syncClient.get(HttpUtils.getAbsoluteUrl(param.sm, "/ws/client/register"));
        }
		
		if(isCancelled()) return null;
                //2.refresh local files
		
		if(isCancelled()) return null;        
                //3.get the update list of files from server
                
		if(isCancelled()) return null;
                //4.download or delete files one by one from server
                
		if(isCancelled()) return null;
                //5.upload or delete files one by one to server
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		cleanTask();
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		cleanTask();
	}
	
	private void cleanTask(){
		if(BackService.syncTasks.containsKey(this.smId)){
			BackService.syncTasks.remove(this.smId);
		}
	}
	
	@TargetApi(11)
    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }
}
