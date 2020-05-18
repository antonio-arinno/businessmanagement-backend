package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by aarinopu on 09/01/2020.
 */
public interface IInvoiceDAO extends CrudRepository<Invoice, Long> {

    Invoice findByIdAndCompany(Long id, Company company);

    List<Invoice> findByCompany(Company company);

    @Query("select max(inv.number) + 1 from Invoice inv where inv.company = ?1")
    Long nextInvoiceNumber(Company company);



}
