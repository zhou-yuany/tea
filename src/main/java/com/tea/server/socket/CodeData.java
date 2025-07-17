package com.tea.server.socket;

public class CodeData {

    String code;
    String message;

    Object data;

    public CodeData(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CodeData(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
