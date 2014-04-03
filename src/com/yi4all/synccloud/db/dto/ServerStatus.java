package com.yi4all.synccloud.db.dto;

public enum ServerStatus {

	OFFLINE("离线"),
	ONLINE("在线"),
	SYNC("正在同步"),
	CONNECT("连接中");

	private final String displayName;

	ServerStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
