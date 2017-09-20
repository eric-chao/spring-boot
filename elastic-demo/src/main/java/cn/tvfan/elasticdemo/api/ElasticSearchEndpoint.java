package cn.tvfan.elasticdemo.api;

import cn.tvfan.elasticdemo.entity.Branch;
import cn.tvfan.elasticdemo.entity.Employee;
import cn.tvfan.elasticdemo.repository.BranchRepository;
import cn.tvfan.elasticdemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ElasticSearchEndpoint {

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @RequestMapping("/branch")
    public List<Branch> soBranch(
            @RequestParam(value = "q", defaultValue = "") String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        //String queryString = "springboot";//搜索关键字
        //QueryStringQueryBuilder builder = new QueryStringQueryBuilder(queryString);
        Pageable pageable = PageRequest.of(page, size);
        Page<Branch> branchList = branchRepository.findByCity(q, pageable);
        return branchList.getContent();
    }

    @RequestMapping("/all/employee")
    public List<Employee> soEmployee(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeeList = employeeRepository.findAll(pageable);
        return employeeList.getContent();
    }

    @RequestMapping("/employee")
    public Employee soEmployee(@RequestParam(value = "id") String id) {
        Employee employee = employeeRepository.findById(id);
        return employee;
    }

}
