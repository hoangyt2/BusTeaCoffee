package com.java04.wibucian.vos;


import com.java04.wibucian.validations.annotation.UpdateExistPhoneEmailEmployee;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@UpdateExistPhoneEmailEmployee
public class EmployeeUpdateVO extends EmployeeVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    public String toString (){
        return super.toString();
    }

}