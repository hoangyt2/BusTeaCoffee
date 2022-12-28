package com.java04.wibucian.services;

import com.java04.wibucian.dtos.EmployeeDTO;
import com.java04.wibucian.models.Account;
import com.java04.wibucian.models.Employee;
import com.java04.wibucian.repositories.AccountRepository;
import com.java04.wibucian.repositories.EmployeeRepository;
import com.java04.wibucian.security.CustomPasswordEncoder;
import com.java04.wibucian.vos.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public String save(EmployeeAddVO vO) {
        try {
            Employee bean = new Employee();
            BeanUtils.copyProperties(vO, bean);
            Account account = new Account();
            account.setPassword(customPasswordEncoder.encode(vO.getPassword()));
            if (vO.getIdEmployee() != null) {
                bean.setId(vO.getIdEmployee());
            }
            bean = employeeRepository.save(bean);
            account.setEmployee(bean);
            accountRepository.saveNative("Employee" + String.format("%05d", Integer.parseInt(bean.getId())), account.getPassword(), 0);
            return bean.getId();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<Employee> findAll() {
        return this.employeeRepository.findAll();
    }

    public boolean delete(String id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            return false;
        }
        String idAccount = employee.getAccount().getId();
        accountRepository.deleteById(idAccount);
        employeeRepository.deleteById(id);
        return true;
    }

    public void update(String id, EmployeeUpdateVO vO) {
        Employee bean = requireOne(id);
        String oldSrc = bean.getSrcEmployee();
        BeanUtils.copyProperties(vO, bean);
        if (vO.getSrcEmployee().equals("")) {
            bean.setSrcEmployee(oldSrc);
        }
        employeeRepository.save(bean);
    }

    // update bằng employee object, sẽ được nhân viên sử dụng để update thông tin cá nhân
    public void update(Employee employee, StaffEmployeeUpdateVO employeeUpdateVO) {
        BeanUtils.copyProperties(employeeUpdateVO, employee);
        employee.setBirthDay(employeeUpdateVO.getBirthday());
        employeeRepository.save(employee);
    }

//    public List<Employee> findAll() {
//        return employeeRepository.findAll();
//    }

    public EmployeeDTO getDTOById(String id) {
        Employee original = requireOne(id);
        return toDTO(original);
    }

    public Employee getById(String id) {
        return requireOne(id);
    }

    public Page<EmployeeDTO> query(EmployeeQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    public EmployeeDTO toDTO(Employee original) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(original, employeeDTO);
        return employeeDTO;
    }

    private Employee requireOne(String id) {
        return employeeRepository.findById(id)
                                 .orElseThrow(() -> new NoSuchElementException(
                                         "Resource not found: " + id));
    }

    public boolean checkExistPhoneOrEmail(String phone, String email) {
        List<Employee> optEmployee = employeeRepository.findAllByPhoneNumberOrEmail(phone, email);
        return !optEmployee.isEmpty();
    }

    public boolean checkExistPhoneOrEmail(String phone, String email, String id) {
        List<Employee> optEmployee = employeeRepository.findAllByPhoneNumberOrEmail(phone, email);

        if (!optEmployee.isEmpty() && optEmployee.stream().anyMatch(e -> !id.equals(e.getId()))) {
            return true;
        }
        return false;
    }
}
