package com.yi4all.synccloud.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "file")
public class FileModel implements Serializable{

    public  static final String FILE_NAME = "FILE_NAME";
    public  static final String PATH = "PATH";
    public  static final String PARENT_ID = "PARENT_ID";
    public  static final String IS_DIR = "IS_DIR";
    public  static final String DELETE_FLAG = "DELETE_FLAG";
    public  static final String UPDATED_AT = "UPDATED_AT";

    @DatabaseField(generatedId = true)
	private Long id;

    @DatabaseField(columnName = FILE_NAME)
    private String fileName;

    @DatabaseField(index = true, columnName = PATH)
    private String path; //relative path based on client's root

    @DatabaseField(columnName = PARENT_ID)
    private Long parentId;

    @DatabaseField(columnName = IS_DIR)
	private boolean isDir;

    @DatabaseField(columnName = DELETE_FLAG)
	private boolean deleteFlag; //

    @DatabaseField(columnName = UPDATED_AT)
	private Date updatedAt;
	
    public FileModel(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
