package com.yi4all.synccloud;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import  com.yi4all.synccloud.db.ServerModel;
import com.yi4all.synccloud.db.dto.ServerStatus;
import com.yi4all.synccloud.fragment.ServerFragment;
import com.yi4all.synccloud.service.BackService;
import com.yi4all.synccloud.service.DBService;
import com.yi4all.synccloud.utils.DateUtils;
import com.yi4all.synccloud.widget.ProgressWheel;

public class MainActivity extends FragmentActivity{

    private GridView gridView;
    private List<ServerModel> servers;
    private DBService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        servers = new ArrayList<ServerModel>();

        gridView = (GridView) findViewById(R.id.serverGrid);

        gridView.setAdapter(new ServerAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            	//TODO:flip over or pop up editor of server
            	if(position == servers.size()){
            		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            	    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            	    if (prev != null) {
            	        ft.remove(prev);
            	    }
            	    ft.addToBackStack(null);

            	    // Create and show the dialog.
            	    ServerFragment newFragment = ServerFragment.newInstance(null);
            	    newFragment.show(ft, "dialog");
            	}
            }
        });

    dbService = DBService.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshGrid();
    }
    
    public void refreshGrid(){
    	servers = dbService.getServers();
        
        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class ServerAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

        public ServerAdapter(Context context){
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return servers.size()+1;
        }

        @Override
        public ServerModel getItem(int i) {
        	if(i < servers.size()){
            return servers.get(i);
        	}else{
        		return null;
        	}
        }

        @Override
        public long getItemId(int i) {
        	if(i < servers.size()){
            return servers.get(i).getId();
        	}else{
        		return -1;
        	}
        }

        @Override
        public View getView(int position, View convertView, ViewGroup vg) {
            if (servers == null || position < 0 || position > servers.size())
                return null;

            if(position == servers.size()){
            	return mInflater.inflate(R.layout.list_add_view, null);
            	
            }else{
            final View row = mInflater.inflate(R.layout.list_item_view, null);

            ViewHolder holder = (ViewHolder) row.getTag();
            if (holder == null) {
                holder = new ViewHolder(row);
                row.setTag(holder);
            }
            
            final ViewHolder holder2 = holder;
            
            holder.parent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// flip over
					if(holder2.isShowPB){
						//confirm to cancel the current sync task
					}else{
						//execute the sync task
					}
					((ViewFlipper)v).showNext();
					holder2.isShowPB = !holder2.isShowPB;
					
					if(holder2.isShowPB){
						holder2.currentProgressPB.spin();
					}else{
						holder2.currentProgressPB.stopSpinning();
					}
				}
			});

            // other normal row
            final ServerModel sm = servers.get(position);

            // set name to label
           holder.serverNameTxt.setText(sm.getName());
           holder.serverNameTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        	    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        	    if (prev != null) {
        	        ft.remove(prev);
        	    }
        	    ft.addToBackStack(null);

        	    // Create and show the dialog.
        	    ServerFragment newFragment = ServerFragment.newInstance(sm);
        	    newFragment.show(ft, "dialog");
				
			}
		});
           holder.serverIPTxt.setText(sm.getIp());
           holder.serverPortTxt.setText(String.valueOf(sm.getPort()));
           holder.serverLastSyncTimeTxt.setText(DateUtils.calculateElapsTime(sm.getLastSyncTime(), MainActivity.this));

           showStatusTxt(ServerStatus.CONNECT, holder.serverStatusTxt);

            final ViewHolder finalHolder = holder;
            BackService.checkServerStatus(sm, new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(finalHolder.serverStatusTxt != null){
                    if (msg.arg1 == 0) {
                    	showStatusTxt(ServerStatus.ONLINE, finalHolder.serverStatusTxt);
                    }else {
                    	showStatusTxt(ServerStatus.OFFLINE, finalHolder.serverStatusTxt);
                    }
                    }
                }
            });

            return (row);
            }
        }
    }

    class ViewHolder {
    	boolean isShowPB;
    	
    	//default side
    	ViewFlipper parent = null;
        TextView serverNameTxt = null;
        TextView serverStatusTxt = null;
        TextView serverIPTxt = null;
        TextView serverPortTxt = null;
        TextView serverLastSyncTimeTxt = null;
        //other side
        ProgressWheel currentProgressPB = null;
        TextView currentProgressTxt = null;
        TextView currentSpeedTxt = null;

        ViewHolder(View base) {
        	this.parent = (ViewFlipper) base;
            this.serverNameTxt = (TextView) base.findViewById(R.id.serverNameTxt);
            this.serverStatusTxt = (TextView) base.findViewById(R.id.serverStatusTxt);
            this.serverIPTxt = (TextView) base.findViewById(R.id.serverIPTxt);
            this.serverPortTxt = (TextView) base.findViewById(R.id.serverPortTxt);
            this.serverLastSyncTimeTxt = (TextView) base.findViewById(R.id.serverLastSyncTimeTxt);
            
            this.currentProgressPB = (ProgressWheel) base.findViewById(R.id.currentProgressPB);
            this.currentProgressTxt = (TextView) base.findViewById(R.id.currentProgressTxt);
            this.currentSpeedTxt = (TextView) base.findViewById(R.id.currentSpeedTxt);
        }
    }
    
    private void showStatusTxt(ServerStatus status, TextView v){
    	switch(status){
    	case OFFLINE:{
    		v.setCompoundDrawables(getResources().getDrawable(R.drawable.light_offline), null, null, null);
    		v.setText(R.string.offline);
    		break;
    	}
    	case ONLINE:{
    		v.setCompoundDrawables(getResources().getDrawable(R.drawable.light_online), null, null, null);
    		v.setText(R.string.online);
    		break;
    	}
    	case SYNC:{
    		v.setCompoundDrawables(getResources().getDrawable(R.drawable.light_sync), null, null, null);
    		v.setText(R.string.sync);
    		break;
    	}
    	case CONNECT:{
    		v.setCompoundDrawables(getResources().getDrawable(R.drawable.light_connect), null, null, null);
    		v.setText(R.string.connect);
    		break;
    	}
    	}
    	v.invalidate();
    }
}
