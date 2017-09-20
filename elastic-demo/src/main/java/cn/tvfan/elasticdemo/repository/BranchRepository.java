package cn.tvfan.elasticdemo.repository;

import cn.tvfan.elasticdemo.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface BranchRepository extends Repository<Branch, String> {

    Page<Branch> findByCity(String city, Pageable pageable);
}
