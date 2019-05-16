package com.tungnvan.godear.utils;

public class Record {
    private int ID;
    private String record_name;
    private String record_max_time;
    private boolean is_selected = false;

    public Record(int ID, String record_name, String record_max_time){
        this.ID = ID;
        this.record_name = record_name;
        this.record_max_time = record_max_time;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRecord_name() {
        return record_name;
    }

    public void setRecord_name(String record_name) {
        this.record_name = record_name;
    }

    public String getRecord_max_time() {
        return record_max_time;
    }

    public void setRecord_max_time(String record_max_time) {
        this.record_max_time = record_max_time;
    }

    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
