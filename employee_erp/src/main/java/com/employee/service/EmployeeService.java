package com.employee.service;

import java.util.List;

import com.employee.entity.Employee;

public interface EmployeeService {

	Long createEmployee(Employee emp);
	
	Employee getEmployee(Long id);
	
	List<Employee> getAllEmployee();
	
	void deleteEmployee(Long id);
	
	void updateEmployee(Employee e);
}
