package com.java04.wibucian.vos;


import com.java04.wibucian.validations.annotation.AddExistPhoneEmailEmployee;
import com.java04.wibucian.validations.annotation.PasswordMatch;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatch
@AddExistPhoneEmailEmployee
public class EmployeeAddVO extends EmployeeVO {
    @NotEmpty(message = "password can not empty")
    private String password;
    @NotEmpty(message = "confirm password can not empty")
    private String confirmPassword;
    public String toString (){
        return super.toString();
    }

}