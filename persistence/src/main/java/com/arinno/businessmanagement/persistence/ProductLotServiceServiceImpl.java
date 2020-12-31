package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Product;
import com.arinno.businessmanagement.model.ProductLot;
import com.arinno.businessmanagement.services.IProductLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductLotServiceServiceImpl implements IProductLotService {

    @Autowired
    private IProductLotDAO productLotDAO;

    @Override
    @Transactional(readOnly = true)
    public List<ProductLot> findByProductAndCompany(Product product, Company company) {
        return productLotDAO.findByProductAndCompany(product, company);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductLot> findByCompany(Pageable pageable, Company company) {
        return productLotDAO.findByCompany(pageable, company);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductLot> findByCompanyOrderByProduct(Pageable pageable, Company company) {
        return productLotDAO.findByCompanyOrderByProduct(pageable, company);
    }

    @Override
    @Transactional
    public ProductLot save(ProductLot productLot) {
        return productLotDAO.save(productLot);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductLot findByProductAndLotAndCompany(Product product, String lot, Company company) {
        return productLotDAO.findByProductAndLotAndCompany(product, lot, company);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productLotDAO.deleteById(id);
    }


}
