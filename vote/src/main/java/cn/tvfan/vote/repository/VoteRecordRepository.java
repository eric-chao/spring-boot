package cn.tvfan.vote.repository;

import cn.tvfan.vote.entity.VoteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRecordRepository extends JpaRepository<VoteRecord, Long> {

    @Query(value = "SELECT * FROM t_vote_record t WHERE t.votedate=curdate() and t.ip=?1 and t.md5=?2", nativeQuery = true)
    List<VoteRecord> findCurrentRecordByIp(String ip, String md5);

    @Modifying
    @Query(value = "update t_vote_record t set t.votes = t.votes + 1 where t.id=?1", nativeQuery = true)
    void updateRecordVotesById(long id);

}
