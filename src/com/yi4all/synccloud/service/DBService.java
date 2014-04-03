package com.yi4all.synccloud.service;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.yi4all.synccloud.db.ClientModel;
import com.yi4all.synccloud.db.FileModel;
import com.yi4all.synccloud.db.FilesDBOpenHelper;
import com.yi4all.synccloud.db.ServerModel;
import com.yi4all.synccloud.utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ChenYu on 13-8-8.
 */
public class DBService {

    private static final String LOG_TAG = "DBService";

    private FilesDBOpenHelper helper;
    private Context context;

    private DBService(Context context) {
    	this.context = context;
        this.helper = FilesDBOpenHelper.getHelper(context);
    }

    public static DBService getInstance(Context context) {
        return new DBService(context);
    }

    public void close() {
        if (helper != null) {
            OpenHelperManager.releaseHelper();
            helper = null;
        }
    }

    public List<ServerModel> getServers(){
        List<ServerModel> res = new ArrayList<ServerModel>();

        try {
            Dao<ServerModel, Integer> dao = helper.getServerDao();

            res = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  res;
    }

    public boolean createServer(ServerModel sm){
        try {
            Dao<ServerModel, Integer> dao = helper.getServerDao();

            int res = dao.create(sm);

            return  res == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean updateServer(ServerModel sm){
        try {
            Dao<ServerModel, Integer> dao = helper.getServerDao();

            int res = dao.update(sm);

            return  res == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean deleteServer(ServerModel sm){
        try {
            Dao<ServerModel, Integer> dao = helper.getServerDao();

            int res = dao.delete(sm);

            return  res == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

	public void updateFiles(List<FileModel> files, ClientModel cm) {
		try {
            Dao<FileModel, Integer> dao = helper.getFileDao();

		  String root = Utils.getClientFolder(context, cm.getName());
		  
	      for(FileModel fm: files){
	    	  fm.setPath(fm.getPath().replace(root, ""));
	    	  
	    	   List<FileModel> tmpList = dao.queryForEq(FileModel.PATH, fm.getPath());
	    	  if(tmpList == null || tmpList.size() == 0){
	    		  tmpList = dao.queryForEq(FileModel.PATH, Utils.getParentFolderPath(fm.getPath()));
	    		  if(tmpList != null && tmpList.size() > 0){
	    			  FileModel parent = tmpList.get(0);
	    			  fm.setParentId(parent.getId());
	    		  }
				  fm.setUpdatedAt(new Date());
	    		  dao.create(fm);

	    	  }else{
	    		  FileModel fm2 = tmpList.get(0);
	    		  
	    		  fm2.setDeleteFlag(false);
	    		  if(fm.getUpdatedAt().after(fm2.getUpdatedAt())){
	    			  fm2.setUpdatedAt(fm.getUpdatedAt());
	    		  }
	    		  dao.update(fm2);
	    	  }
	      }
	      
	      //update delete flag for files already not in the files
	      List<FileModel> list = dao.queryForEq(FileModel.DELETE_FLAG, false);
	      for(FileModel fm : list){
	    	  String path = root + fm.getPath();
	    	  if(!new File(path).exists()){
	    		  fm.setDeleteFlag(true);
	    		  fm.setUpdatedAt(new Date());
	    		  dao.update(fm);
	    	  }
	      }
		} catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	//query client for a certain server
	public ClientModel getClient4Server(ServerModel sm){
		try {
            Dao<ClientModel, Integer> dao = helper.getClientDao();

            List<ClientModel> list = dao.queryForEq(ClientModel.SERVER, sm);
            
            if(list != null && list.size() > 0){
            	return list.get(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
		return null;
	}
}
