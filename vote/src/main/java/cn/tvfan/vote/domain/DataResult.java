package cn.tvfan.vote.domain;

import java.util.ArrayList;
import java.util.List;

public class DataResult<T> {

    private int code = 10000;

    private long total = 0l;

    private String message = "";

    private List<T> data = new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
