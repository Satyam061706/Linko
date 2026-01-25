package com.Linko.handler;

import java.io.IOException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.Linko.entities.Provider;
import com.Linko.entities.User;
import com.Linko.repositories.UserRepositories;
import com.Linko.util.AppConstraint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);
    private final UserRepositories repositories;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuth2SuccesHandler");

        // Identifying the provider

        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;
        String clientRegistationId = auth.getAuthorizedClientRegistrationId();
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        logger.info("ClientRegitrationId: " + clientRegistationId);

        user.getAttributes().forEach((key, value) -> {
            logger.info(key + " : " + value);
        });

        // creating user and adding common fields
        User user1 = new User();
        user1.setRoles(List.of(AppConstraint.ROLE_USER));
        user1.setEmailVerified(true);
        user1.setEnabled(true);

        if (clientRegistationId.equalsIgnoreCase("google")) {

            // for google
            // google attributes

            String email = user.getAttribute("email").toString();
            String name = user.getAttribute("name").toString();
            String picture = user.getAttribute("picture").toString();

            // // adding user field

            user1.setEmail(email);
            user1.setName(name);
            user1.setProfilePicture(picture);
            user1.setPassword("password");
            user1.setProvider(Provider.GOOGLE);
            user1.setProviderUserId(name);
            user1.setAbout("This account is created using googel...");

        } else if (clientRegistationId.equalsIgnoreCase("github")) {

            // for github
            // github attribute
            String email = user.getAttribute("email") != null ? user.getAttribute("email")
                    : user.getAttribute("login").toString() + "@gmail.com";
            String picture = user.getAttribute("avatar_url").toString();
            String name = user.getAttribute("login").toString();

            // // adding user field
            user1.setEmail(email);
            user1.setProfilePicture(picture);
            user1.setName(name);
            user1.setPassword("password");
            user1.setProvider(Provider.Github);
            user1.setProviderUserId(name);
            user1.setAbout("This account is created using github...");

        } else if (clientRegistationId.equalsIgnoreCase("facebook")) {

            // for facebook
            // facebook attribute

        } else {
            logger.info("OAuth2SuccessHandler: Unknown provider");
        }

        // saving user in database if not present
        User user2 = repositories.findByEmail(user1.getEmail()).orElse(null);
        if (user2 == null) {
            repositories.save(user1);
            logger.info("User saved:" + user1);
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
