package com.java04.wibucian.vos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import net.bytebuddy.utility.nullability.UnknownNull;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


@Data
public class EmployeeVO implements Serializable {
    private static final long serialVersionUID = 1L;

//    @NotNull(message = "id can not null")
//    private Integer id;

//    @NotNull(message = "idEmployee can not null")
    private String idEmployee;

    @NotEmpty(message = "Name can not empty")
    private String name;

    @NotEmpty(message = "Address can not empty")
    private String address;

    private String srcEmployee;

    private MultipartFile avatar;

    @NotEmpty(message = "Email can not empty")
    private String email;

    @NotEmpty(message = "PhoneNumber can not empty")
    private String phoneNumber;

    @NotEmpty(message = "Gender can not null")
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String birthDay;

    @NotEmpty(message = "MaritalStatus can not null")
    private String maritalStatus;

    private Float coefficientsSalary;

    public Date getBirthDay() {
        System.out.println(this.birthDay);
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(this.birthDay);
        } catch(ParseException e) {
            throw new RuntimeException("Ngày không hợp lệ");
        }
    }
}
