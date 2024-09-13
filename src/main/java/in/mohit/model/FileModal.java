package in.mohit.model;

import java.io.Serializable;
import java.util.Date;

public class FileModal implements Serializable {

    private String name;
    private Long size;
    private String date;

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

    public FileModal(String name, Long size, String date) {
        this.name = name;
        this.size = size;
        this.date = date;
    }

    public FileModal(){}
}
