package cn.tvfan.vote.api;

import cn.tvfan.vote.domain.DataResult;
import cn.tvfan.vote.domain.ShareInfo;
import cn.tvfan.vote.domain.StarsGroup;
import cn.tvfan.vote.entity.Ticket;
import cn.tvfan.vote.entity.VoteRecord;
import cn.tvfan.vote.repository.TicketRepository;
import cn.tvfan.vote.service.VoteService;
import cn.tvfan.vote.util.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/vote")
public class VoteEndpoint {

    final static Logger logger = LoggerFactory.getLogger(VoteEndpoint.class);

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    VoteService voteService;

    @RequestMapping(value = "/stars", method = RequestMethod.GET)
    public DataResult<StarsGroup> getStarsList(
            HttpServletRequest request,
            @RequestParam(value = "tag", defaultValue = "out") String tag,
            @RequestHeader(value="user-agent", defaultValue="android") String useragent) {
        String ip = this.getIp(request);
        DataResult<StarsGroup> result = new DataResult<>();

        //add to list
        List<StarsGroup> dataList = new ArrayList<>();
        dataList.add(getStarsGroup("男"));
        dataList.add(getStarsGroup("女"));
        result.setData(dataList);
        result.setTotal(dataList.get(0).getTotal() + dataList.get(1).getTotal());
        logger.info("[starsinfo]: tag=" + tag + ", ip" + ip + ", ua=" + useragent);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public DataResult<StarsGroup> vote(
            HttpServletRequest request,
            @PathParam("id") int id,
            @RequestParam(value = "tag", defaultValue = "out") String tag,
            @RequestHeader(value="user-agent", defaultValue="android") String useragent) throws Exception {
        DataResult<StarsGroup> result = new DataResult<>();
        String ip = this.getIp(request), md5 = Security.md5(useragent), sex = (id >= 10? "女": "男");
        List<VoteRecord> recordList = voteService.getCurrentVoteRecord(ip, md5);
        int votesLimit = getVotesLimit(tag), voted = 0;
        if(recordList == null || recordList.isEmpty()) {
            VoteRecord record = new VoteRecord();
            record.setIp(ip);
            record.setVotes(1);
            record.setMd5(md5);
            record.setVotedate(new Date());
            voteService.persist(id, record);
            result.setMessage(String.valueOf(votesLimit - 1));
            //return result;
        } else if (recordList.size() > 0) {
            VoteRecord record = recordList.get(0);
            voted = record.getVotes();
            if(voted >= votesLimit) {
                result.setCode(10001);
                result.setMessage("0");
                return result;
            }
            voteService.updateRecordVotesById(id, record.getId());
        }

        //add to list
        List<StarsGroup> dataList = new ArrayList<>();
        dataList.add(getStarsGroup(sex));
        result.setMessage(String.valueOf(votesLimit - 1 - voted));
        result.setData(dataList);
        result.setTotal(voteService.getTotalVotes());
        logger.info("[vote]: tag=" + tag + ", ip" + ip +", ua=" + useragent);
        return result;
    }

    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    public ShareInfo getTicketInfo(HttpServletRequest request) throws Exception {
        List<Ticket> list = ticketRepository.findAll();
        Ticket ticket = null;
        if(list == null || list.isEmpty()) {
            ticket = Ticket.newTicketObject();
            ticketRepository.save(ticket);
        } else {
            ticket = list.get(0);
            if(System.currentTimeMillis() >= (ticket.getExpires() * 1000 + ticket.getUpdatetime().getTime())) {
                Ticket newObj = Ticket.newTicketObject();
                ticket.setExpires(newObj.getExpires());
                ticket.setTicket(newObj.getTicket());
                ticket.setUpdatetime(newObj.getUpdatetime());
                ticketRepository.save(ticket);
            }
        }

        //build for share info object
        ShareInfo info = new ShareInfo();
        //String url = request.getRequestURL().toString() + ((request.getQueryString() == null || request.getQueryString().isEmpty()) ? "" : ("?" + request.getQueryString()));
        logger.info("url: " + request.getParameter("url"));
        info.setUrl(request.getParameter("url"));
        info.setSignature(Security.getSha1("jsapi_ticket="+ticket.getTicket()+"&noncestr="+info.getNoncestr()+"&timestamp="+info.getTimestamp()+"&url="+info.getUrl()));
        return info;
    }

    private StarsGroup getStarsGroup(String sex) {
        StarsGroup group = new StarsGroup();
        group.setGroup(sex == "男"? "male" : "female");
        group.setTotal(voteService.getTotalVotes(sex));
        group.setList(voteService.getStarInfoList(sex));
        return group;
    }

    private int getVotesLimit(String tag) {
        boolean isInner = tag.equals("tvfan");
        if(isInner) {
            return 10;
        }
        return 3;
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }

}
