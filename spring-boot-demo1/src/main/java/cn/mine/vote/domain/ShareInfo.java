package cn.mine.vote.domain;

import java.util.UUID;

public class ShareInfo {

    //String appid = "wxcfaa020ee248a2f2";
    String appid = "wx354f2992fee4718b";

    String noncestr = UUID.randomUUID().toString();//Sixty.toSixty(System.currentTimeMillis());

    String url = "http://mp.weixin.qq.com?params=value";

    //单位：秒
    long timestamp = System.currentTimeMillis() / 1000;

    String signature;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("appid=").append(appid);
        sb.append(", noncestr=").append(noncestr);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", url=").append(url);
        return sb.toString();
    }
}
