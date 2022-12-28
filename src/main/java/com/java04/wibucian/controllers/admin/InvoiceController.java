package com.java04.wibucian.controllers.admin;

import com.java04.wibucian.services.InvoiceService;
import com.java04.wibucian.utils.CryptoUtils;
import com.java04.wibucian.vos.InvoiceUpdateVO;
import com.java04.wibucian.vos.InvoiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Validated
@Controller
@RequestMapping("admin/invoice")
public class InvoiceController {
    @RequestMapping(value = {"/statistical"}, method = RequestMethod.GET)
    public String statistical(ModelMap modelMap) {
        return "/admin/invoice/statistical";
    }
}
