package com.tungnvan.godear.components;

import java.util.Date;

public class CallStateHolder {

    private static CallStateHolder instance = null;
    private boolean is_last_call_incoming_call = false;
    private String call_number = null;
    private Date start_time = null;

    public static CallStateHolder getInstance() {
        if (instance == null) instance = new CallStateHolder();
        return instance;
    }

    private CallStateHolder() {}

    public void setIsIncomingCall(boolean is_incoming_call) {
        is_last_call_incoming_call = is_incoming_call;
    }

    public void setNumber(String number) {
        call_number = number;
    }

    public void setStartTime(Date start) {
        start_time = start;
    }

    public boolean getIsIncomingCall() {
        return is_last_call_incoming_call;
    }

    public String getNumber() {
        return call_number;
    }

    public Date getStartTime() {
        return start_time;
    }

}
