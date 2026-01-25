package com.Linko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.Linko.handler.AuthFaliureHandler;
import com.Linko.handler.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler handler;
    private final AuthFaliureHandler authFaliureHandler;

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity hSecurity) throws Exception {

        hSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> {
                    formLogin.loginPage("/login")
                            .loginProcessingUrl("/authenticate")
                            .successHandler((request, response, authentication) -> {
                                response.sendRedirect("/user/contacts");
                            })
                            .failureUrl("/login?error=true")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            // similarly we can also make success handler
                            .failureHandler(authFaliureHandler);
                })
                .logout(logoutform -> {
                    logoutform.logoutUrl("/do-logout");
                    logoutform.logoutSuccessUrl("/login?logout=true");
                });

        // auth2 configuration

        hSecurity.oauth2Login(auth -> {
            auth.loginPage("/login")
                    .successHandler(handler);
        });

        return hSecurity.build();
    }

}
