package com.Linko.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Linko.entities.Email;
import com.Linko.entities.User;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    Page<Email> findByUser(User user, PageRequest pageable);

}
