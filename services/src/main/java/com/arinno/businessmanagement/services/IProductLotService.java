package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Product;
import com.arinno.businessmanagement.model.ProductLot;

import java.util.List;

public interface IProductLotService {

    public List<ProductLot> findByProductAndCompany(Product product, Company company);

    public ProductLot save(ProductLot productLot);

    public ProductLot findByProductAndLotAndCompany(Product product, String lot, Company company);

    public void deleteById(Long id);
}
