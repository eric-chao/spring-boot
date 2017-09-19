package cn.tvfan.elasticdemo.repository;

import cn.tvfan.elasticdemo.entity.Branch;
import cn.tvfan.elasticdemo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, String> {

    Page<Employee> findAll(Pageable pageable);

    List<Employee> findByBranch(Branch branch);
}
