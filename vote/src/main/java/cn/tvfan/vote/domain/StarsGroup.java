package cn.tvfan.vote.domain;

import cn.tvfan.vote.entity.StarInfo;

import java.util.ArrayList;
import java.util.List;

public class StarsGroup {

    String group = "";

    long total = 0l;

    List<StarInfo> list = new ArrayList<>();

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<StarInfo> getList() {
        return list;
    }

    public void setList(List<StarInfo> list) {
        this.list = list;
    }

}
