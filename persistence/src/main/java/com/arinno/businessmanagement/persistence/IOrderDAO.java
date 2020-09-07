package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IOrderDAO extends CrudRepository<Order, Long> {

    Order findByIdAndCompany(Long id, Company company);

    @Query("select max(ord.number) + 1 from Order ord where ord.company = ?1")
    Long nextOrderNumber(Company company);

    List<Order> findByCompany(Company company);

    List<Order> findByInvoiceIsNullAndCompanyOrderByCustomer(Company company);

//    List<Customer> findCustomerDistinctByInvoiceIsNullAndCompany(Company company);

    @Query("select distinct ord.customer from Order ord where ord.company = ?1 and ord.invoice = null")
    List<Customer> findCustomerDistinctByInvoiceIsNullAndCompany(Company company);

    List<Order> findByInvoiceIsNullAndCustomer(Customer customer);

}
