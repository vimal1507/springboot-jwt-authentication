package com.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.entity.Employee;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.service.impl.EmployeeServiceImpl;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private EmployeeServiceImpl service;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<String> createEmployee(@RequestBody Employee emp){
		Long id = service.createEmployee(emp);
		String msg = "Employee with id "+id+" is created";
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/find/{id}")
	public ResponseEntity<?> getEmployee(@PathVariable Long id){
		ResponseEntity<?> res = null;
		try {
			Employee e = service.getEmployee(id);
			res = new ResponseEntity<Employee>(e,HttpStatus.OK);
		}catch(EmployeeNotFoundException e){
			throw e;
		}
		
		return res;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/all")
	public ResponseEntity<List<Employee>> getAllEmployee(){
		List<Employee> emp = service.getAllEmployee();
		return new ResponseEntity<List<Employee>>(emp,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable Long id){
		ResponseEntity<String> res = null;
		try {
			service.deleteEmployee(id);
			res = new ResponseEntity<String>("Employee with id "+id+" is deleted",HttpStatus.OK);
		}catch(EmployeeNotFoundException e) {
			throw e;
		}
		return res;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public ResponseEntity<String> updateEmployee(@RequestBody Employee e){
		ResponseEntity<String> res = null;
		try {
			service.updateEmployee(e);
			res = new ResponseEntity<String>("Employee with id "+e.getEmpId()+" is updated",HttpStatus.OK);
		}catch(EmployeeNotFoundException enf) {
			throw enf;
		}
		return res;
	}
}
