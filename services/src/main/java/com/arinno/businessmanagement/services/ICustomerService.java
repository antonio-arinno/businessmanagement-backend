package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by aarinopu on 10/12/2019.
 */
public interface ICustomerService {

    public List<Customer> findAll();

    public Customer findByIdAndCompany(Long id, Company company);

    public Customer findById(Long id);

    public void delete(Long id);

    public Customer save(Customer customer);

    public Page<Customer> findByCompany(Pageable pageable, Company company);

    public List<Customer> findByCompany(Company company);

    List<Customer> findByNameContainingIgnoreCaseAndCompany(String term, Company company);

    List<Customer> findByCodeContainingIgnoreCaseAndCompany(String term, Company company);

    List<Customer> findByCodeStartingWithIgnoreCaseAndCompany(String term, Company company);

    public void saveAll(List<Customer> customers);

}
