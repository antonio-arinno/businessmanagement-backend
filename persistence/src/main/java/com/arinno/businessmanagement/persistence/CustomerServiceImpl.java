package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Invoice;
import com.arinno.businessmanagement.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Customer> findByCompany(Company company) {
        return customerDAO.findByCompany(company);
    }

/*
    @Override
    @Transactional(readOnly = true)
    public Invoice findInvoiceById(Long id) {
        return invoiceDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Invoice saveInvoice(Invoice invoice) {
        Long number = invoiceDAO.nextInvoiceNumber(invoice.getCompany());
        if(number!=null){
            invoice.setNumber(number);
        }else {
            invoice.setNumber((long) 1);
        }
        return invoiceDAO.save(invoice);
    }

    @Override
    @Transactional
    public void deleteInvoiceById(Long id) {
        invoiceDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice findInvoiceByIdAndCompany(Long id, Company company) {
        return invoiceDAO.findByIdAndCompany(id, company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> findInvoicesByCompany(Company company) {
        return invoiceDAO.findByCompany(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findByCodeAndCompany(Long code, Company company) {
        return customerDAO.findByCodeAndCompany(code, company);
    }
*/

}
