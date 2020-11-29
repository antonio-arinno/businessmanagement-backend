package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPartnerDAO extends JpaRepository<Partner, Long> {

    public Partner findByIdAndCompany(Long id, Company company);

}
