package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Product;
import com.arinno.businessmanagement.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by aarinopu on 22/01/2020.
 */

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDAO productDAO;

    @Override
    @Transactional(readOnly = true)
    public Product findByIdAndCompany(Long id, Company company) {
        return productDAO.findByIdAndCompany(id, company);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productDAO.deleteById(id);

    }

    @Override
    public Product save(Product product) {
        return productDAO.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCompany(Company company) {
        return productDAO.findByCompany(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByDescriptionContainingIgnoreCase(String term) {
        return productDAO.findByDescriptionContainingIgnoreCase(term);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByDescriptionContainingIgnoreCaseAndCompany(String term, Company company) {
        return productDAO.findByDescriptionContainingIgnoreCaseAndCompany(term, company);
    }


}
