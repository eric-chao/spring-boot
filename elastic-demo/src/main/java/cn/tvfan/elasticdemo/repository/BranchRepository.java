package cn.tvfan.elasticdemo.repository;

import cn.tvfan.elasticdemo.entity.Branch;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BranchRepository extends Repository<Branch, String> {

    List<Branch> findByCity(String city);
}
