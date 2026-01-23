package com.josue.gateway_server.controllers;


import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AppController {

    @GetMapping("/authorized")
    public Map<String, String> authorized(@RequestParam("code") String code) {
        return Map.of("code", code);
    }


    @GetMapping("/authorized/v1")
    public Map<String, String> token(@RegisteredOAuth2AuthorizedClient("oidc-client")
                                          OAuth2AuthorizedClient authorizedClient) {
        String accessToken = authorizedClient
                .getAccessToken()
                .getTokenValue();

        return Map.of("access_token", accessToken);
    }

    @PostMapping("/logout")
    public Map<String, String> logout() {
        return Collections.singletonMap("logout", "Ok");
    }

}
