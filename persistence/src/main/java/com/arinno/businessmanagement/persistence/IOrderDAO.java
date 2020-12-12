package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface IOrderDAO extends CrudRepository<Order, Long> {

    Order findByIdAndCompany(Long id, Company company);

    @Query("select max(ord.number) + 1 from Order ord where ord.company = ?1")
    Long nextOrderNumber(Company company);

    List<Order> findByCompany(Company company);

    List<Order> findByInvoiceIsNullAndCompanyOrderByCustomer(Company company);

    @Query("select distinct ord.customer from Order ord where ord.company = ?1 and ord.invoice = null")
    List<Customer> findCustomerDistinctByInvoiceIsNullAndCompany(Company company);

    List<Order> findByInvoiceIsNullAndCustomer(Customer customer);

    Page<Order> findByCompany(Pageable pageable, Company company);

    List<Order> findByInvoiceIsNullAndCustomerAndCreateAtAfterAndCreateAtBefore(Customer customer, Date fromDate, Date toDate);

    @Query("select distinct ord.customer from Order ord where ord.invoice = null and ord.createAt between ?1 and ?2 and ord.company = ?3")
    List<Customer> findCustomerDistinctByInvoiceIsNullAndCreateAtBetweenAndCompany(Date fromDate, Date toDate, Company company);

    List<Order> findByInvoiceIsNullAndCustomerAndCreateAtBetween(Customer customer, Date fromDate, Date toDate);
}
