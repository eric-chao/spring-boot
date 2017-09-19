package cn.tvfan.vote.test;

import cn.tvfan.vote.domain.ShareInfo;
import cn.tvfan.vote.util.Security;

public class SecurityTest {

    public static void main(String args[]) {
        ShareInfo info = new ShareInfo();
        System.out.println(info);

        String param = "jsapi_ticket=bxLdikRXVbTPdHSM05e5u7-koz4mwHQV0i9GUgy3NCTdAmG8UCC5djoiPlkVcmpCJj3bK-R9XYU-zCkxj62YPA&noncestr="+
                info.getNoncestr()+"&timestamp="+info.getTimestamp()+"&url="+info.getUrl();
        System.out.println(Security.getSha1(param));
        System.out.println(Security.sign1(param));
    }

}
