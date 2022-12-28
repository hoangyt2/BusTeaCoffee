package com.java04.wibucian.validations.validator;

import com.java04.wibucian.services.EmployeeService;
import com.java04.wibucian.validations.annotation.AddExistPhoneEmailEmployee;
import com.java04.wibucian.vos.EmployeeAddVO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddExistPhoneEmailEmployeeValidator implements ConstraintValidator<AddExistPhoneEmailEmployee, EmployeeAddVO> {
	@Autowired
	private EmployeeService employeeService;

	@Override
	public void initialize(AddExistPhoneEmailEmployee p) {

	}

	public boolean isValid(EmployeeAddVO employeeVO, ConstraintValidatorContext c) {
		String phone = employeeVO.getPhoneNumber();
		String email = employeeVO.getEmail();

		if (employeeService.checkExistPhoneOrEmail(phone, email)) {
			return false;
		}

		return true;
	}

}
