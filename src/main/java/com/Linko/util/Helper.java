package com.Linko.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    @Value("${server.baseUrl}")
    private String baseUrl;

    public static Long stringToLong(String id) {
        return Long.valueOf(id);
    }

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        Object principal = authentication.getPrincipal();

        // 1️⃣ OAuth2 Login (Google / GitHub)
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {

            String clientId = oauthToken.getAuthorizedClientRegistrationId();

            if (principal instanceof OAuth2User oauthUser) {

                if (clientId.equalsIgnoreCase("google")) {
                    System.out.println("Verified by Google");
                    return oauthUser.getAttribute("email"); // Google always gives "email"
                }

                if (clientId.equalsIgnoreCase("github")) {
                    System.out.println("Verified by GitHub");
                    // GitHub may return "email" or null if it's private
                    String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email")
                            : oauthUser.getAttribute("login").toString() + "@gmail.com";
                    return email;
                }
            }

            return null;
        }

        // 2️⃣ Normal Login (Username + Password)
        if (principal instanceof UserDetails userDetails) {
            System.out.println("Verified by Username & Password");
            return userDetails.getUsername();
        }

        return null;
    }

    public String getLinkForEmailVerificatiton(String emailToken) {

        return this.baseUrl + "/auth/verify-email?token=" + emailToken;

    }

}
