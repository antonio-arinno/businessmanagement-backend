package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Partner;

public interface IPartnerService {

    public Partner findByIdAndCompany(Long id, Company company);

    public Partner save(Partner partner);

    public void delete(Long id);

}
