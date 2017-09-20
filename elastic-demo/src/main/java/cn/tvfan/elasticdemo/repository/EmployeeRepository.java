package cn.tvfan.elasticdemo.repository;

import cn.tvfan.elasticdemo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface EmployeeRepository extends Repository<Employee, String> {

    Employee findById(String id);

    Page<Employee> findAll(Pageable pageable);

}
