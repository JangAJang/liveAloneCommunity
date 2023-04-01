package com.capstone.liveAloneCommunity.domain.auth;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationNumber {

    private String authNum;

    public boolean equals(String input){
        return authNum.equals(input);
    }
}
