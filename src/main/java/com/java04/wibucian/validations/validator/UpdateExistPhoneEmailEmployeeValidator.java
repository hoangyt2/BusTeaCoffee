package com.java04.wibucian.validations.validator;

import com.java04.wibucian.services.EmployeeService;
import com.java04.wibucian.validations.annotation.UpdateExistPhoneEmailEmployee;
import com.java04.wibucian.vos.EmployeeUpdateVO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UpdateExistPhoneEmailEmployeeValidator implements ConstraintValidator<UpdateExistPhoneEmailEmployee, EmployeeUpdateVO> {
	@Autowired
	private EmployeeService employeeService;

	@Override
	public void initialize(UpdateExistPhoneEmailEmployee p) {

	}

	public boolean isValid(EmployeeUpdateVO employeeVO, ConstraintValidatorContext c) {
		String phone = employeeVO.getPhoneNumber();
		String email = employeeVO.getEmail();
		String id = employeeVO.getId();

		if (employeeService.checkExistPhoneOrEmail(phone, email, id)) {
			return false;
		}

		return true;
	}

}
