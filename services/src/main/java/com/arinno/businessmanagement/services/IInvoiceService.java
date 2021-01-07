package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Invoice;
import com.arinno.businessmanagement.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInvoiceService {

    public Invoice findByIdAndCompany(Long id, Company company);

    public void deleteById(Long id);

    public Invoice save(Invoice invoice);

    public List<Invoice> findByCompany(Company company);

    Page<Invoice> findByCompany(Pageable pageable, Company company);
}
