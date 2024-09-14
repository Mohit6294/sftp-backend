package in.mohit.model;

import java.io.Serializable;
import java.util.Date;

public class FileModal implements Serializable {


    private String name;
    private boolean isFolder;
    private Long size;
    private String date;////8/5/2024, 9:30:00 AM

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public FileModal(String name, Long size, String date, boolean isFolder) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.isFolder = isFolder;
    }

    public FileModal(){}
}
