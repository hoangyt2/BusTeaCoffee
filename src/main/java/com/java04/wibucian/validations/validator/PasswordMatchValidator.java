package com.java04.wibucian.validations.validator;

import com.java04.wibucian.validations.annotation.PasswordMatch;
import com.java04.wibucian.vos.EmployeeAddVO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, EmployeeAddVO> {

	@Override
	public void initialize(PasswordMatch p) {

	}

	public boolean isValid(EmployeeAddVO employeeAddVO, ConstraintValidatorContext c) {
		String plainPassword = employeeAddVO.getPassword();
		String repeatPassword = employeeAddVO.getConfirmPassword();

		if(plainPassword == null || !plainPassword.equals(repeatPassword)) {
			return false;
		}

		return true;
	}

}
