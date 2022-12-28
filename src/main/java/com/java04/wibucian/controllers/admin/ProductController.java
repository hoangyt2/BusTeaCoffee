package com.java04.wibucian.controllers.admin;

import com.java04.wibucian.dtos.ProductDTO;
import com.java04.wibucian.models.DetailProduct;
import com.java04.wibucian.models.Product;
import com.java04.wibucian.models.Sale;
import com.java04.wibucian.services.DetailProductService;
import com.java04.wibucian.services.ProductService;
import com.java04.wibucian.services.SaleService;
import com.java04.wibucian.services.TypeProductService;
import com.java04.wibucian.vos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.HashMap;

//@Validated
@Controller
@RequestMapping("admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    private TypeProductService typeProductService;


    private SaleService saleService;
    public static final int limit = 5;


    public ProductController(TypeProductService typeProductService,
                             SaleService saleService) {
        this.typeProductService = typeProductService;
        this.saleService = saleService;
    }

    @GetMapping("/")
    public String Home(ModelMap modelMap) throws
            Exception {
        int page = 1;
        Pageable pageable = PageRequest.of(page - 1, limit);
        int totalPage = productService.getTotalPage(limit);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("totalPage", totalPage);
        modelMap.addAttribute("product", productService.findAll(pageable));
        //modelMap.addAttribute("getlist", productService.listAll());
        return "admin/product/index";
    }

    @GetMapping("/page/{page}")
    public String HomePage(ModelMap modelMap,
                           @Valid @NotNull @PathVariable("page") int page) throws
            Exception {
        int totalPage = productService.getTotalPage(limit);
        page = (page < 1)
                ? 1
                : page;
        page = (page > totalPage)
                ? totalPage
                : page;
        Pageable pageable = PageRequest.of(page - 1, limit);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("totalPage", totalPage);
        modelMap.addAttribute("product", productService.findAll(pageable));
        return "admin/product/index";
    }

    //Hàm tạo create:
    @GetMapping("/create")
    public String createProductPage(ModelMap modelMap) throws
            Exception {
        modelMap.addAttribute("typeProduct", this.typeProductService.findAll());
        modelMap.addAttribute("sale", this.saleService.findAll());
        modelMap.addAttribute("productVO", new ProductVO());
        return "admin/product/create";
    }

    @GetMapping("/edit/{id}")
    public String HomeEdit(ModelMap modelMap,
                           @Valid @NotNull @PathVariable("id") String id) throws
            Exception {
        Product product = productService.findById(id);
        //Sale sale = saleService.findById(id);
        modelMap.addAttribute("typeProduct", typeProductService.findAll());
        modelMap.addAttribute("product", product);
        modelMap.addAttribute("productUpdateVO", new ProductUpdateVO());
        // modelMap.addAttribute("sale", sale);

        return "admin/product/edit";
    }


    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST,
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String update(ModelMap modelMap, @Valid @ModelAttribute(name = "productUpdateVO") ProductUpdateVO productUpdateVO,
                         BindingResult bindingResult,
                         @PathVariable("id") String idProduct,
                         @RequestParam MultiValueMap<String, String> formData) throws
            Exception {
        System.out.println(productUpdateVO);
        try {
            if (bindingResult.hasErrors()) {
                Product product = productService.findById(idProduct);
                modelMap.addAttribute("typeProduct", typeProductService.findAll());
                modelMap.addAttribute("product", product);
                return "admin/product/edit";
            }
            String fileName = StringUtils.cleanPath(productUpdateVO.getAvatar()
                                                                   .getOriginalFilename());
            ClassLoader classLoader = getClass().getClassLoader();
            if (!fileName.equals("")) {
                String filePath =
                        String.valueOf(System.currentTimeMillis()) + "." + fileName.split(
                                "\\.")[1];
                File file = new File(classLoader.getResource(".")
                                                .getFile()
                                             + "static/admin/assets/file-upload/"
                                             + filePath);
                productUpdateVO.getAvatar()
                               .transferTo(file);
                productUpdateVO.setSrcImage(filePath);
            }
            //            System.out.println(employeeUpdateVO.getAvatar());
            this.productService.update(idProduct, productUpdateVO,
                                       productUpdateVO.getIdProductType());
            return "redirect:/admin/product/";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "Lỗi tè le";
        }
    }

    //detail
    @GetMapping("/detail/{id}")
    public String viewProductPage(ModelMap modelMap,
                                  @Valid @NotNull @PathVariable("id") String id) throws
            Exception {
        modelMap.addAttribute("id", id);
        Product product = productService.findById(id);

        //System.out.println(id)
        modelMap.addAttribute("product", product);

        return "admin/product/detail";
    }


    //detail

    // delete

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // @GetMapping("/create")
    public String createProductPage(ModelMap modelMap, @Valid @ModelAttribute("productVO") ProductVO productVO, BindingResult bindingResult) throws
            Exception {

        try {
            if (bindingResult.hasErrors()) {
                modelMap.addAttribute("typeProduct", this.typeProductService.findAll());
                modelMap.addAttribute("sale", this.saleService.findAll());
                return "admin/product/create";
            }
            String fileName = StringUtils.cleanPath(productVO.getAvatar()
                                                             .getOriginalFilename());
            ClassLoader classLoader = getClass().getClassLoader();
            if (!fileName.equals("")) {
                String filePath =
                        String.valueOf(System.currentTimeMillis()) + "." + fileName.split(
                                "\\.")[1];
                File file = new File(classLoader.getResource(".")
                                                .getFile()
                                             + "static/admin/assets/file-upload/"
                                             + filePath);
                productVO.getAvatar()
                         .transferTo(file);

                productVO.setSrcImage(filePath);
            }

            String idProduct = this.productService.save(productVO);
            return "redirect:/admin/product/";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "Lỗi tè le";
        }


    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET,
                    produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        try {
            productService.delete(id);
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", true);
            map.put("value", "test");
            return ResponseEntity.ok()
                                 .body(map);
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> map = new HashMap<>();
            map.put("check", false);
            map.put("value", "test");
            return ResponseEntity.ok()
                                 .body(map);
        }
    }


    //delete


    @PutMapping("/{id}")
    public void update(@Valid @NotNull @PathVariable("id") String id,
                       @Valid @RequestBody ProductUpdateVO vO) {
        productService.update(id, vO);
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@Valid @NotNull @PathVariable("id") String id) {
        return productService.getById(id);
    }

    @GetMapping
    public Page<ProductDTO> query(@Valid ProductQueryVO vO) {
        return productService.query(vO);
    }
}
