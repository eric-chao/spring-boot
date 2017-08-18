package cn.mine.vote.repository;

import cn.mine.vote.entity.StarInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StarInfoRepository extends JpaRepository<StarInfo, Integer> {

    List<StarInfo> findBySexOrderByVotesDesc(String sex);

    @Query(value = "SELECT sum(votes) FROM t_star_info t", nativeQuery = true)
    long findTotalVotes();


    @Query(value = "SELECT sum(votes) FROM t_star_info t where t.sex=?1", nativeQuery = true)
    long findTotalVotes(String sex);

    @Modifying
    @Query(value = "update t_star_info t set t.votes = t.votes + 1 where t.id=?1", nativeQuery = true)
    void updateStarsVotesById(int id);

}

