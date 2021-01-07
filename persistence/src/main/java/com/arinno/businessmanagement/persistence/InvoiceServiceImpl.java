package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Invoice;
import com.arinno.businessmanagement.services.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class InvoiceServiceImpl implements IInvoiceService {

    @Autowired
    private IInvoiceDAO invoiceDAO;

    @Override
    @Transactional(readOnly = true)
    public Invoice findByIdAndCompany(Long id, Company company) {
        return invoiceDAO.findByIdAndCompany(id, company);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        invoiceDAO.deleteById(id);
    }

    @Override
    @Transactional
    public Invoice save(Invoice invoice) {
        Long number = invoiceDAO.nextInvoiceNumber(invoice.getCompany());
        if(number!=null){
            invoice.setNumber(number);
        }else {
            invoice.setNumber((long) 1);
        }
        return invoiceDAO.save(invoice);
    }

    @Override
    public List<Invoice> findByCompany(Company company) {
        return invoiceDAO.findByCompany(company);
    }

    @Override
    public Page<Invoice> findByCompany(Pageable pageable, Company company) {
        return invoiceDAO.findByCompany(pageable, company);
    }
}
