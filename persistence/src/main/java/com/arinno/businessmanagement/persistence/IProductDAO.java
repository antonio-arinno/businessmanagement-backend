package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by aarinopu on 09/01/2020.
 */
public interface IProductDAO extends CrudRepository<Product, Long> {

    public Product findByIdAndCompany(Long id, Company company);

    public List<Product> findByCompany(Company company);

    public List<Product> findByDescriptionContainingIgnoreCase(String term);

    public List<Product> findByDescriptionContainingIgnoreCaseAndCompany(String term, Company company);




}
