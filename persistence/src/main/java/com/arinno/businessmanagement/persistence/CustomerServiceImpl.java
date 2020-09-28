package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Invoice;
import com.arinno.businessmanagement.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by aarinopu on 11/12/2019.
 */

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerDAO customerDAO;

    @Autowired
    private IInvoiceDAO invoiceDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return (List<Customer>) customerDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findByIdAndCompany(Long id, Company company) {
        return customerDAO.findByIdAndCompany(id, company);
    }


    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        customerDAO.deleteById(id);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return customerDAO.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findByCompany(Pageable pageable, Company company) {
        return customerDAO.findByCompany(pageable, company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findByCompany(Company company) {
        return customerDAO.findByCompany(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findByNameContainingIgnoreCaseAndCompany(String term, Company company) {
        return customerDAO.findByNameContainingIgnoreCaseAndCompany(term, company);
    }

    @Override
    public List<Customer> findByCodeContainingIgnoreCaseAndCompany(String term, Company company) {
        return customerDAO.findByCodeContainingIgnoreCaseAndCompany(term, company);
    }

    @Override
    public List<Customer> findByCodeStartingWithIgnoreCaseAndCompany(String term, Company company) {
        return customerDAO.findByCodeStartingWithIgnoreCaseAndCompany(term, company);
    }

    @Override
    public void saveAll(List<Customer> customers) {
        customerDAO.saveAll(customers);
    }



}
