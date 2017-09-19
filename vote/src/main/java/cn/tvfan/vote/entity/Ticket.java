package cn.tvfan.vote.entity;

import cn.tvfan.vote.util.HTTPClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="t_ticket")
public class Ticket {

    @Id
    int id;

    String ticket;

    int expires;

    Date updatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public static Ticket newTicketObject() throws Exception {
        Ticket ticket = new Ticket();
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx354f2992fee4718b&secret=012e57aee5310572326eb46b5c4a3536";
        HTTPClient client = new HTTPClient(url, 15000, 15000);
        int status = client.sendGet("utf-8");
        if (200 == status) {
            ObjectMapper objectMapper = new ObjectMapper();
            AccessToken token = objectMapper.readValue(client.getResult(), AccessToken.class);
            String url1 = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + token.getAccess_token();
            client = new HTTPClient(url1, 15000, 15000);
            if(200 == client.sendGet("utf-8")) {
                TicketInfo info = objectMapper.readValue(client.getResult(), TicketInfo.class);
                ticket.setTicket(info.getTicket());
                ticket.setExpires(info.getExpires_in());
                ticket.setUpdatetime(new Date());
            }
        }

        return ticket;
    }
}

class AccessToken {

    String access_token;

    int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}

class TicketInfo {

    int errcode;

    String errmsg;

    int expires_in;

    String ticket;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}