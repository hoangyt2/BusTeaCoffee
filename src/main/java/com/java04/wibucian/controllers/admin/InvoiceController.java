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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Validated
@Controller
@RequestMapping("admin/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;



    @GetMapping(value = {"/detail/{id}"})
    public String detail(@PathVariable String id, ModelMap modelMap) {
        modelMap.addAttribute("products", invoiceService.findAllProduct());
        modelMap.addAttribute("invoice", invoiceService.findById(id));
        return "/admin/invoice/detail";
    }

    @RequestMapping(value = {"store-one/", "store-one"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> storeOne(@Valid @RequestBody InvoiceVO vO) {
        vO.setIdEmployee("Employee00001");
        return ResponseEntity.ok().body(invoiceService.save_one(vO));
    }

    @DeleteMapping(value = {"/{id}"})
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable String id) {
        try {
            return ResponseEntity.ok().body(invoiceService.delete(id));
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", false);
            map.put("value", "Lỗi");
            return ResponseEntity.ok().body(map);
        }
    }

    @PutMapping(value = {"/{id}"})
    @ResponseBody
    public ResponseEntity<Object> update(@PathVariable String id, @Valid @RequestBody InvoiceUpdateVO vO) {
        try {
            System.out.println(vO.getStatus());
            invoiceService.update(id, vO);
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", true);
            map.put("value", "Cập nhật thành công");
            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", false);
            map.put("value", "Lỗi");
            return ResponseEntity.ok().body(map);
        }
    }
    //paginate page
    @GetMapping(value = {"/{page}", "/", ""})
    public String paginate(@PathVariable(required = false) String page, ModelMap modelMap, @RequestParam(defaultValue = "10") int limit) {
        //check page != null
        if (page == null) {
            page = "1";
        }
        int pageInt;
        try {
            pageInt = Integer.parseInt(page);
        } catch (Exception e) {
            return "redirect:/invoice";
        }
        Pageable pageable = PageRequest.of(pageInt - 1, limit);
        int allPage = invoiceService.getTotalPage(limit);
        int count = invoiceService.getCount();
        System.out.println(pageInt);
        if (limit > count || limit<=0) {
            limit = 10;
        }
        if (pageInt > allPage) {
            return "redirect:/invoice/paginate/";
        }
        if (pageInt < 1 ) {
            return "redirect:/invoice/paginate/";
        }
        modelMap.addAttribute("currentPage", pageInt);
        modelMap.addAttribute("allPage", allPage);
        modelMap.addAttribute(("limit"), limit);
        modelMap.addAttribute("listInvoice", invoiceService.getPage(pageable));
        return "/admin/invoice/index";
    }
    @RequestMapping(value = {"/statistical"}, method = RequestMethod.GET)
    public String statistical(ModelMap modelMap) {
        return "/admin/invoice/statistical";
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> payment(ModelMap modelMap, @RequestBody InvoiceUpdateVO invoiceVO) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            //vnp_Command = querydr
            String vnp_TxnRef = invoiceVO.getIdInvoice();
            String vnp_TmnCode = "TF7L8JRY";
            String vnp_IpAddr = "127.0.0.1";

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "querydr");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//            vnp_Params.put("vnp_Amount", String.valueOf(Integer.parseInt(String.valueOf(invoiceVO.getToltalMoney())) * 100));
            vnp_Params.put("vnp_Amount", String.valueOf(invoiceVO.getToltalMoney().intValue() * 100));
//            vnp_Params.put("vnp_BankCode", "VNPAYQR");
            String vnp_CreateDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_OrderInfo", "Kiem tra ket qua GD OrderId:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", "billpayment");
            vnp_Params.put("vnp_ReturnUrl", "http//localhost:8080/admin/invoice/updateTransaction");
            vnp_Params.put("vnp_ExpireDate", LocalDateTime.now().plusMinutes(15).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

            //Build data to hash and querystring
            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = CryptoUtils.hmacSHA512(CryptoUtils.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html" + "?" + queryUrl;

            map.put("check", true);
            map.put("value", "Thanh toán thành công");
            map.put("paymentUrl", paymentUrl);
            System.out.println("Payment URL: " + paymentUrl);

            String idInvoice = invoiceVO.getIdInvoice();
            invoiceVO.setStatus(2);
            invoiceService.update(idInvoice, invoiceVO);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            System.out.println("Err: " + e.getMessage());
            map.put("check", false);
            map.put("value", "Thanh toán thất bại");
            return ResponseEntity.badRequest().body(map);
        }

    }
}
