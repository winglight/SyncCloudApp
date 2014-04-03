package com.yi4all.synccloud.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "server")
public class ServerModel implements Serializable{

    public  static final String NAME = "NAME";
    public  static final String IP = "IP";
    public  static final String PORT = "PORT";
    public  static final String ALLOW_DELETE_SERVER = "ALLOW_DELETE_SERVER";
    public  static final String ALLOW_DELETE_CLIENT = "ALLOW_DELETE_CLIENT";
    public  static final String LAST_SYNC_TIME = "LAST_SYNC_TIME";

    @DatabaseField(generatedId = true)
	private Long id;

    @DatabaseField(columnName = NAME)
	private String name;//client's machine name

    @DatabaseField(columnName = IP)
	private String ip;

    @DatabaseField(columnName = PORT)
    private int port = 9000;

    @DatabaseField(columnName = ALLOW_DELETE_SERVER)
	private boolean allowDeleteServer =  true;

    @DatabaseField(columnName = ALLOW_DELETE_CLIENT)
	private boolean allowDeleteClient = true;

    @DatabaseField(columnName = LAST_SYNC_TIME)
	private Date lastSyncTime;// last sync time

	public ServerModel(){
		
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isAllowDeleteServer() {
        return allowDeleteServer;
    }

    public void setAllowDeleteServer(boolean allowDeleteServer) {
        this.allowDeleteServer = allowDeleteServer;
    }

    public boolean isAllowDeleteClient() {
        return allowDeleteClient;
    }

    public void setAllowDeleteClient(boolean allowDeleteClient) {
        this.allowDeleteClient = allowDeleteClient;
    }

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }
}
