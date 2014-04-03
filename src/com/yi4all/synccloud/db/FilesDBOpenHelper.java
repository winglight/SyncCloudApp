package com.yi4all.synccloud.db;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FilesDBOpenHelper extends OrmLiteSqliteOpenHelper {
	
	public  static final int DATABASE_VERSION = 1;
    
	public static final String DATABASE_NAME = "files.db";
	
	// we do this so there is only one helper
		private static FilesDBOpenHelper helper = null;
		
		private Dao<ClientModel, Integer> clientDao;
	private Dao<FileModel, Integer> fileDao;
	private Dao<ServerModel, Integer> serverDao;

    public FilesDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized FilesDBOpenHelper getHelper(Context context) {
		if (helper == null) {
			helper = new FilesDBOpenHelper(context);
			helper.getWritableDatabase();
		}
		return helper;
	}
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    	try {
			Log.i(FilesDBOpenHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, ClientModel.class);
			TableUtils.createTable(connectionSource, FileModel.class);
			TableUtils.createTable(connectionSource, ServerModel.class);

		} catch (SQLException e) {
			Log.e(FilesDBOpenHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
        
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(FilesDBOpenHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, ClientModel.class, true);
			TableUtils.dropTable(connectionSource, FileModel.class, true);
			TableUtils.dropTable(connectionSource, ServerModel.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(FilesDBOpenHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
		
	}
	
	public Dao<FileModel, Integer> getFileDao() throws SQLException{
    	if(fileDao == null){
    		fileDao = getDao(FileModel.class);
    	}
    	return fileDao;
    }
    
    public Dao<ServerModel, Integer> getServerDao() throws SQLException{
    	if(serverDao == null){
    		serverDao = getDao(ServerModel.class);
    	}
    	return serverDao;
    }
    
    public Dao<ClientModel, Integer> getClientDao() throws SQLException{
    	if(clientDao == null){
    		clientDao = getDao(ClientModel.class);
    	}
    	return clientDao;
    }
    
    @Override
	public void close() {
		super.close();
		fileDao = null;
		serverDao = null;
		clientDao = null;
	}

}
