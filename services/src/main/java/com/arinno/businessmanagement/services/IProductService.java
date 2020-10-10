package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by aarinopu on 22/01/2020.
 */
public interface IProductService {

    Product findByIdAndCompany(Long id, Company company);

    public Page<Product> findByCompany(Pageable pageable, Company company);

    void delete(Long id);

    Product save(Product product);

    public List<Product> findByCompany(Company company);

    public List<Product> findByDescriptionContainingIgnoreCase(String term);

    public List<Product> findByDescriptionContainingIgnoreCaseAndCompany(String term, Company company);

    public void saveAll(List<Product> products);

}
