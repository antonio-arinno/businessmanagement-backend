package com.arinno.businessmanagement.persistence;


import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Customer;
import com.arinno.businessmanagement.model.Order;
import com.arinno.businessmanagement.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDAO orderDAO;



    @Override
    @Transactional(readOnly = true)
    public Order findByIdAndCompany(Long id, Company company) {
        return orderDAO.findByIdAndCompany(id, company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCompany(Company company) {
        return orderDAO.findByCompany(company);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Order> findByCompany(Pageable pageable, Company company) {
        return orderDAO.findByCompany(pageable, company);
    }

    @Override
    @Transactional
    public Order save(Order order) {
        Long number = orderDAO.nextOrderNumber(order.getCompany());
        if(number!=null){
            order.setNumber(number);
        }else {
            order.setNumber((long) 1);
        }
        return orderDAO.save(order);
    }

    @Override
    @Transactional
    public Order update(Order order) {
        return orderDAO.save(order);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderDAO.deleteById(id);
    }

    @Override
    public List<Order> findByInvoiceIsNullAndCompanyOrderByCustomer(Company company) {
        return orderDAO.findByInvoiceIsNullAndCompanyOrderByCustomer(company);
    }

    @Override
    public List<Customer> findCustomerDistinctByInvoiceIsNullAndCompany(Company company) {
        return orderDAO.findCustomerDistinctByInvoiceIsNullAndCompany(company);
    }

    @Override
    public List<Order> findByInvoiceIsNullAndCustomer(Customer customer) {
        return orderDAO.findByInvoiceIsNullAndCustomer(customer);
    }
}
