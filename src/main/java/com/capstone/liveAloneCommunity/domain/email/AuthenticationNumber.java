package com.capstone.liveAloneCommunity.domain.email;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AuthenticationNumber {

    private String authNum;

    public boolean equals(String input){
        return authNum.equals(input);
    }
}
