package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {

    Order findByIdAndCompany(Long id, Company company);

    List<Order> findByCompany(Company company);

    Order save(Order order);

    Order update(Order order);

    void deleteById(Long id);

    List<Order> findByInvoiceIsNullAndCompanyOrderByCustomer(Company company);

    List<Customer> findCustomerDistinctByInvoiceIsNullAndCompany(Company company);

    List<Order> findByInvoiceIsNullAndCustomer(Customer customer);

    Page<Order> findByCompany(Pageable pageable, Company company);
}
