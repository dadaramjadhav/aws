package com.example;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/token")
    public String getToken(@AuthenticationPrincipal OidcUser oidcUser) {
        // ID token
        String idToken = oidcUser.getIdToken().getTokenValue();
        // Access token
        String accessToken = oidcUser.getIdToken().getTokenValue(); // ID token used for OIDC claims
        return "ID Token: " + idToken + "\nAccess Token: " + accessToken;
    }
}