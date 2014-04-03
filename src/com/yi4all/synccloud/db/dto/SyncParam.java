package com.yi4all.synccloud.db.dto;

import com.yi4all.synccloud.db.ServerModel;

import android.content.Context;
import android.os.Handler;

public class SyncParam {

	public Context context;
	public ServerModel sm;
	public Handler handler;
	
	public SyncParam(final Context context, final ServerModel sm, final Handler handler){
		this.context = context;
		this.sm = sm;
		this.handler = handler;
	}
}
