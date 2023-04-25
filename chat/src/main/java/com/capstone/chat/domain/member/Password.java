package com.capstone.chat.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    @Column(name = "PASSWORD")
    private String password;

    public String getPassword() {
        return password;
    }

    public boolean equals(String password, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(password, this.password);
    }
}
