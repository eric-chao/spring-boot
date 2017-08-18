package cn.mine.vote.service;

import cn.mine.vote.entity.StarInfo;
import cn.mine.vote.entity.VoteRecord;
import cn.mine.vote.repository.StarInfoRepository;
import cn.mine.vote.repository.VoteRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class VoteService {


    @Autowired
    StarInfoRepository starInfoRepository;

    @Autowired
    VoteRecordRepository voteRecordRepository;

    /**
     * 通过性别获取投票列表
     * @param sex
     * @return
     */
    public List<StarInfo> getStarInfoList(String sex) {
        return starInfoRepository.findBySexOrderByVotesDesc(sex);
    }

    /**
     * 获取投票总数
     * @return
     */
    public long getTotalVotes(){
        return starInfoRepository.findTotalVotes();
    }


    /**
     * 获取投票总数
     * @param sex
     * @return
     */
    public long getTotalVotes(String sex){
        return starInfoRepository.findTotalVotes(sex);
    }

//    @Transactional
//    public void updateStarVotesById(int id) {
//        starInfoRepository.updateStarsVotesById(id);
//    }

    /**
     * 获取当前投票记录
     * @param ip
     * @return
     */
    public List<VoteRecord> getCurrentVoteRecord(String ip, String md5) {
        return voteRecordRepository.findCurrentRecordByIp(ip, md5);
    }

    @Transactional
    public void persist(int starid, VoteRecord record) {
        starInfoRepository.updateStarsVotesById(starid);
        voteRecordRepository.save(record);
    }

    @Transactional
    public void updateRecordVotesById(int starid, long id) {
        starInfoRepository.updateStarsVotesById(starid);
        voteRecordRepository.updateRecordVotesById(id);
    }

}
