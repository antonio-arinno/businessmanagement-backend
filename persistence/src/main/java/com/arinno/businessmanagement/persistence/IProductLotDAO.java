package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Product;
import com.arinno.businessmanagement.model.ProductLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductLotDAO extends JpaRepository<ProductLot, Long> {

    public List<ProductLot> findByProductAndCompany(Product product, Company company);

    public ProductLot findByProductAndLotAndCompany(Product product, String lot, Company company);
}
