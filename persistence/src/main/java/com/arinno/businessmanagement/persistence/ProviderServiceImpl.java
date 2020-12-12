package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Provider;
import com.arinno.businessmanagement.services.IProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProviderServiceImpl implements IProviderService {

    @Autowired
    private IProviderDAO providerDAO;

    @Override
    @Transactional(readOnly = true)
    public Provider findByIdAndCompany(Long id, Company company) {
        return providerDAO.findByIdAndCompany(id, company);
    }

    @Override
    public Provider findByCodeAndCompany(String code, Company company) {
        return providerDAO.findByCodeAndCompany(code, company);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Provider> findByCompany(Pageable pageable, Company company) {
        return providerDAO.findByCompany(pageable, company);
    }

    @Override
    @Transactional
    public Provider save(Provider provider) {
        return providerDAO.save(provider);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        providerDAO.deleteById(id);
    }

    @Override
    public List<Provider> findByNameStartingWithIgnoreCaseAndCompany(String term, Company company) {
        return providerDAO.findByNameStartingWithIgnoreCaseAndCompany(term, company);
    }
}
