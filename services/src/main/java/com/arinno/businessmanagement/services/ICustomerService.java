package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Invoice;

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

    public List<Customer> findByCompany(Company company);

    List<Customer> findByNameContainingIgnoreCaseAndCompany(String term, Company company);

    List<Customer> findByCodeContainingIgnoreCaseAndCompany(String term, Company company);

    public void saveAll(List<Customer> customers);


/*
    public Invoice findInvoiceById(Long id);

    public Invoice saveInvoice(Invoice invoice);

    public void deleteInvoiceById(Long id);

    public Invoice findInvoiceByIdAndCompany(Long id, Company company);

    public List<Invoice> findInvoicesByCompany(Company company);

    public Customer findByCodeAndCompany(Long code, Company company);

 */
}
