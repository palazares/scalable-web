package com.waes.palazares.scalableweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
//Security enabled if prod or uat profile
@Profile("prod")
public class WebSecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http) {
        http.csrf().disable()
                .authorizeExchange()
                .anyExchange()
                .permitAll();
        return http.build();
    }

    @Bean
    //This could be used further if Pre/PostAuthorized annotation on methods to restrict certain api calls for particular user
    public UserDetailsService userDetailsService() {
        return username -> {
            if (username.equals("cid")) {
                return new User(username, "",
                        AuthorityUtils
                                .commaSeparatedStringToAuthorityList("ROLE_USER"));
            }
            return new User(username, "", AuthorityUtils
                    .commaSeparatedStringToAuthorityList(""));
        };
    }
}
