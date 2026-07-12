package com.employee.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.entity.Employee;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.repo.EmployeeRepository;
import com.employee.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository repo;

	public Long createEmployee(Employee emp) {
		emp = repo.save(emp);
		return emp.getEmpId();
	}

	
	public Employee getEmployee(Long id) {
		Optional<Employee> e = repo.findById(id);
		if(e.isPresent()) {
			return e.get();
		}else {
			throw new EmployeeNotFoundException("Employee with id "+id+" not exsit");
		}
	}
	
	public List<Employee> getAllEmployee(){
		List<Employee> emp = repo.findAll();
		return emp;
	}
	
	public void deleteEmployee(Long id){
		repo.delete(getEmployee(id));
	}
	
	public void updateEmployee(Employee e) {
		Long id = e.getEmpId();
		if(id != null && repo.existsById(id)) {
			repo.save(e);
		}else {
			throw new EmployeeNotFoundException("Employee with id "+e.getEmpId()+" not exsit");
		}
	}

}
