package com.arinno.businessmanagement.controllers;


import com.arinno.businessmanagement.model.ProductLot;
import com.arinno.businessmanagement.services.IProductLotService;
import com.arinno.businessmanagement.util.IUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"https://businessmanagement-7b334.web.app", "http://localhost:4200", "*"})
@RestController
@RequestMapping("/api")
public class StockRestController {

    @Autowired
    private IProductLotService productLotService;

    @Autowired
    private IUtil util;

    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/products-lots/page/{page}")
    public Page<ProductLot> getProductsLots(@PathVariable Integer page, Authentication authentication){
        Pageable pageable = PageRequest.of(page, 5);
        return productLotService.findByCompanyOrderByProduct(pageable, util.getCompany(authentication));
    }

}
