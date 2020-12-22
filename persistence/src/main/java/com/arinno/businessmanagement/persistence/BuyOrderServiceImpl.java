package com.arinno.businessmanagement.persistence;

import com.arinno.businessmanagement.model.BuyOrder;
import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.Order;
import com.arinno.businessmanagement.services.IBuyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuyOrderServiceImpl implements IBuyOrderService {

    @Autowired
    private IBuyOrderDAO buyOrderDAO;

    @Override
    @Transactional(readOnly = true)
    public Page<BuyOrder> findByCompany(Pageable pageable, Company company) {
        return buyOrderDAO.findByCompany(pageable, company);
    }

    @Override
    @Transactional
    public BuyOrder save(BuyOrder buyOrder) {
        Long number = buyOrderDAO.nextOrderNumber(buyOrder.getCompany());
        if(number!=null){
            buyOrder.setNumber(number);
        }else {
            buyOrder.setNumber((long) 1);
        }
        return buyOrderDAO.save(buyOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public BuyOrder findByIdAndCompany(Long id, Company company) {
        return buyOrderDAO.findByIdAndCompany(id, company);
    }

    @Override
    @Transactional
    public BuyOrder update(BuyOrder buyOrder) {
        return buyOrderDAO.save(buyOrder);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        buyOrderDAO.deleteById(id);
    }
}
