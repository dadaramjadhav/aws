package com.example;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final String cognitoDomain = "https://us-east-1ua48hk4op.auth.us-east-1.amazoncognito.com"; // e.g., myapp.auth.us-east-1.amazoncognito.com
    private final String clientId = "4p1ubi9l5vglbirf9q8seun7pb";
    private final String clientSecret = "112dirvqgbptmjv7j4te4iehfnubc2tkj26hrbntpfj3asugkqrs";
    private final String logoutRedirectUri = "http://localhost:8081/";
    private final WebClient webClient = WebClient.create();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> 
                    userInfo.oidcUserService(this.oidcUserService()) // for OIDC login
                )
            )


        
        .logout(logout -> logout
                    .logoutSuccessHandler((request, response, authentication) -> {
                        // Redirect to Cognito logout URL
                        String logoutUrl = "https://us-east-1ua48hk4op.auth.us-east-1.amazoncognito.com/logout" +
                                "?client_id=4p1ubi9l5vglbirf9q8seun7pb" +
                                "&logout_uri=http://localhost:8081/";
                        response.sendRedirect(logoutUrl);
                    })
                );
        return http.build();
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) {
                OidcUser oidcUser = super.loadUser(userRequest);
                // Map cognito:groups claim to Spring authorities
                List<String> groups = oidcUser.getClaimAsStringList("cognito:groups");
                Collection<GrantedAuthority> mappedAuthorities = (groups == null ? 
                        Collections.emptyList() : 
                        groups.stream()
                              .map(g -> new SimpleGrantedAuthority("ROLE_" + g))
                              .collect(Collectors.toList()));
                return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            }
        };
    }
}
