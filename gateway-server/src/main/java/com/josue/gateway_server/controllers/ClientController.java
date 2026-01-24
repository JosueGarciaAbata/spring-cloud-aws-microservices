package com.josue.gateway_server.controllers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ClientController {

    @GetMapping("/authorized")
    public Map<String, String> token(@RegisteredOAuth2AuthorizedClient("oidc-client")
                                     OAuth2AuthorizedClient authorizedClient) {
        String accessToken = authorizedClient
                .getAccessToken()
                .getTokenValue();

        return Map.of("access_token", accessToken);
    }
}
