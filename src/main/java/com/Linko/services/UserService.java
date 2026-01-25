package com.Linko.services;



import com.Linko.entities.User;
import java.util.*;

public interface UserService {

    User createUser(User user);
    Optional<User> getUserById(Long id);
    Optional<User> updateUser(User user);
    void deleteUser(Long id);
    boolean isUserExists(Long id);
    boolean isUserExistsByEmail(String email);
    User getUserByEmail(String email);


}
