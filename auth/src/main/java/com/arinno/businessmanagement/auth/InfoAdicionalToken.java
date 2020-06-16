package com.arinno.businessmanagement.auth;

import com.arinno.businessmanagement.model.UserModel;
import com.arinno.businessmanagement.services.IUserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aarinopu on 29/12/2019.
 */

@Component
public class InfoAdicionalToken implements TokenEnhancer {

    @Autowired
    private IUserModelService userModelService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        UserModel userModel = userModelService.findByUsername(authentication.getName());

        Map<String, Object> info = new HashMap<>();
/*
        info.put("info_adicional", "Hola que tal!: ".concat(authentication.getName()));
        info.put("nombre", userModel.getName());
        info.put("apellido", userModel.getLastName());
        info.put("email", userModel.getEmail());


*/
        info.put("name", userModel.getName());
        info.put("company", userModel.getCompany().getName());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

        return accessToken;
    }
}
