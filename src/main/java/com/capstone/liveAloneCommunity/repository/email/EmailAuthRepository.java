package com.capstone.liveAloneCommunity.repository.email;

import com.capstone.liveAloneCommunity.entity.email.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, String> {

    public Optional<EmailAuth> findByEmail_Email(final String email);
}
