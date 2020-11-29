package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Partner;
import com.arinno.businessmanagement.services.IPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartnerServiceImpl implements IPartnerService {

    @Autowired
    private IPartnerDAO partnerDAO;

    @Override
    @Transactional(readOnly = true)
    public Partner findByIdAndCompany(Long id, Company company) {
        return partnerDAO.findByIdAndCompany(id, company);
    }

    @Override
    @Transactional
    public Partner save(Partner partner) {
        return partnerDAO.save(partner);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        partnerDAO.deleteById(id);
    }
}
