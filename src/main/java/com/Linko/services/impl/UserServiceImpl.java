package com.Linko.services.impl;

import com.Linko.entities.User;
import com.Linko.exception.EmailAlreadyExistsException;
import com.Linko.exception.ResourceNotFoundException;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.Linko.repositories.UserRepositories;
import com.Linko.services.EmailService;
import com.Linko.services.UserService;
// import org.slf4j.Logger;
import com.Linko.util.AppConstraint;
import com.Linko.util.Helper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositories userRepositories;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    private final Helper helper;
    // private static final Logger logger =
    // org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

    // @SuppressWarnings("null")
    @Override
    public User createUser(User user) {
        if (userRepositories.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(List.of(AppConstraint.ROLE_USER));
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser = userRepositories.save(user);
        String emailLink = helper.getLinkForEmailVerificatiton(emailToken);
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Linko", emailLink);
        return savedUser;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepositories.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {

        User user2 = userRepositories
                .findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user2.setName(user.getUsername());
        user2.setEmail(user.getEmail());
        user2.setAbout(user.getAbout());
        user2.setPassword(user.getPassword());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePicture(user.getProfilePicture());
        user2.setEnabled(user.isEnabled());
        user2.setEmail(user.getEmail());
        // user2.setName(user.getUsername());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        // save the user in database
        User save = userRepositories.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepositories
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepositories.delete(user);
    }

    @Override
    public boolean isUserExists(Long id) {
        return userRepositories.existsById(id);
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        User user = userRepositories.findByEmail(email).orElse(null);
        return user != null;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepositories.findByEmail(email)
                .orElse(null);
    }

}
