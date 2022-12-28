package com.java04.wibucian.vos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class ProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "Product type can not null")
    private String idProductType;

    private MultipartFile avatar;

    @NotEmpty(message = "Describe can not null")
    private String describe;

//    @NotNull(message = "srcImage can not null")
    private String srcImage;

    @NotEmpty(message = "productName can not null")
    private String productName;

    private String idSale;

}
