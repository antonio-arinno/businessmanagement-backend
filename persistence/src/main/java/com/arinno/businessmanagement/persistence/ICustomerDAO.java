package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

/**
 * Created by aarinopu on 10/12/2019.
 */
public interface ICustomerDAO extends JpaRepository<Customer, Long> {

    public Customer findByIdAndCompany(Long id, Company company);

    public List<Customer> findByCompany(Company company);

    public Customer findByCodeAndCompany(Long code, Company company);

    List<Customer> findByNameContainingIgnoreCaseAndCompany(String term, Company company);

    List<Customer> findByCodeContainingIgnoreCaseAndCompany(String term, Company company);

    List<Customer> findByCodeStartingWithIgnoreCaseAndCompany(String term, Company company);

    Page<Customer> findByCompany(Pageable pageable, Company company);
}
