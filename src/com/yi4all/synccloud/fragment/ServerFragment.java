package com.yi4all.synccloud.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yi4all.synccloud.MainActivity;
import com.yi4all.synccloud.R;
import com.yi4all.synccloud.db.ServerModel;
import com.yi4all.synccloud.service.DBService;

/**
 * Created by ChenYu on 13-8-8.
 */
public class ServerFragment extends DialogFragment  {

    public final static String SM_DATA = "server";

    private boolean isNew;

    private ServerModel sm;
    
    private EditText nameTxt;
    private EditText ipTxt;
    private EditText portTxt;
    
    private DBService dbService;

    public ServerFragment() {
    }

    public static ServerFragment newInstance(ServerModel sm) {
        final ServerFragment f = new ServerFragment();

        final Bundle args = new Bundle();
        args.putSerializable(SM_DATA, sm);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        sm = getArguments() != null ? (ServerModel) getArguments().getSerializable(SM_DATA) : null;

        isNew =  (sm == null);
        
        dbService = DBService.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_server, container, false);
        
        nameTxt = (EditText) v.findViewById(R.id.serverNameEditor);
        ipTxt = (EditText) v.findViewById(R.id.serverIPEditor);
        portTxt = (EditText) v.findViewById(R.id.serverPortEditor);
        
        Button saveBtn = (Button) v.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isNew){
					sm = new ServerModel();
				}
				sm.setName(nameTxt.getText().toString());
				sm.setIp(ipTxt.getText().toString());
				sm.setName(nameTxt.getText().toString());
				if(isNew){
					dbService.createServer(sm);
				}else{
					dbService.updateServer(sm);
				}
				
				ServerFragment.this.dismiss();
				
				((MainActivity)getActivity()).refreshGrid();
			}
		});
        
        Button deleteBtn = (Button) v.findViewById(R.id.deleteBtn);
        if(isNew){
        	deleteBtn.setVisibility(View.GONE);
        }
        deleteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(R.string.delete)
		        .setMessage(R.string.confirm_delete)
		        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {

		                dbService.deleteServer(sm);
		                
		                ServerFragment.this.dismiss();
		                
		                ((MainActivity)getActivity()).refreshGrid();
		            }

		        })
		        .setNegativeButton(android.R.string.no, null)
		        .show();
				
			}
		});

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if(!isNew){
        	nameTxt.setText(sm.getName());
        	ipTxt.setText(sm.getIp());
        	portTxt.setText(String.valueOf(sm.getPort()));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
