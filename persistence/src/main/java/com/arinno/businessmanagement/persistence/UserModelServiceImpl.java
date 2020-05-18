package com.arinno.businessmanagement.persistence;


import com.arinno.businessmanagement.model.Company;
import com.arinno.businessmanagement.model.UserModel;
import com.arinno.businessmanagement.services.IUserModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by aarinopu on 15/12/2019.
 */

@Service
@Transactional(readOnly = true)
public class UserModelServiceImpl implements IUserModelService, UserDetailsService  {

    private Logger logger = LoggerFactory.getLogger(UserModelServiceImpl.class);

    @Autowired
    private IUserModelDAO userDAO;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserModel user = userDAO.findByUsername(username);

        if(user == null) {
            logger.error("Error en el login: no existe el usuario '"+username+"' en el sistema!");
            throw new UsernameNotFoundException("Error en el login: no existe el usuario '"+username+"' en el sistema!");
        }

        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .peek(authority -> logger.info("Role: " + authority.getAuthority()))
                .collect(Collectors.toList());


        return new User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);

    }


    @Override
    @Transactional(readOnly = true)
    public UserModel findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserModel findByIdAndCompany(Long id, Company company) {
        return userDAO.findByIdAndCompany(id, company);
    }

    @Override
    public UserModel save(@Valid UserModel userModel) {
        return userDAO.save(userModel);
    }
}
