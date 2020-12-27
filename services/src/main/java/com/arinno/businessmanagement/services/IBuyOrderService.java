package com.arinno.businessmanagement.services;

import com.arinno.businessmanagement.model.BuyOrder;
import com.arinno.businessmanagement.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBuyOrderService {

    Page<BuyOrder> findByCompany(Pageable pageable, Company company);

    Page<BuyOrder> findByInputDateIsNullAndCompany(Pageable pageable, Company company);

    BuyOrder save(BuyOrder buyOrder);

    BuyOrder findByIdAndCompany(Long id, Company company);

    BuyOrder update(BuyOrder currentBuyOrder);

    void deleteById(Long id);
}
