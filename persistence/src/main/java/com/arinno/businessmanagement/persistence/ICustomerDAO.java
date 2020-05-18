package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by aarinopu on 10/12/2019.
 */
public interface ICustomerDAO extends CrudRepository<Customer, Long> {

    public Customer findByIdAndCompany(Long id, Company company);

    public List<Customer> findByCompany(Company company);

    public Customer findByCodeAndCompany(Long code, Company company);
}
