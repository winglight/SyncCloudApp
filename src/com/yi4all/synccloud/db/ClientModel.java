package com.yi4all.synccloud.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "client")
public class ClientModel implements Serializable{

    public  static final String NAME = "NAME";
    public  static final String DEVICE_ID = "DEVICE_ID";
    public  static final String DELETE_FLAG = "DELETE_FLAG";
    public  static final String SYNC_FOLDER = "SYNC_FOLDER";
    public  static final String LAST_SYNC_TIME = "LAST_SYNC_TIME";
    public  static final String UPDATED_AT  = "UPDATED_AT";
    public  static final String SERVER  = "SERVER";
    
    @DatabaseField(generatedId = true)
	private Long id;
    
    @DatabaseField(columnName = NAME)
    private String name;//client's machine name

    @DatabaseField(columnName = DEVICE_ID)
	private String deviceId;//device ID

    @DatabaseField(columnName = DELETE_FLAG)
	private boolean deleteFlag;

    @DatabaseField(columnName = SYNC_FOLDER)
	private String syncFolder;//relative path to sync folder on the server, like "/client1/"

    @DatabaseField(columnName = UPDATED_AT )
	private Date updatedAt;
    
    @DatabaseField(foreignAutoRefresh = true, foreign = true, columnName = SERVER)
    private ServerModel server;
	
	public ClientModel(){
		
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getSyncFolder() {
        return syncFolder;
    }

    public void setSyncFolder(String syncFolder) {
        this.syncFolder = syncFolder;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

	public ServerModel getServer() {
		return server;
	}

	public void setServer(ServerModel server) {
		this.server = server;
	}
}
