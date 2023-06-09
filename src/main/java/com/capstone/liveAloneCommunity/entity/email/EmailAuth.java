package com.capstone.liveAloneCommunity.entity.email;

import com.capstone.liveAloneCommunity.domain.email.AuthenticationNumber;
import com.capstone.liveAloneCommunity.domain.member.Email;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmailAuth {

    @EmbeddedId
    private Email email;
    @Embedded
    private AuthenticationNumber authenticationNumber;

    public EmailAuth(String email, String authenticationNumber) {
        this.email = new Email(email);
        this.authenticationNumber = new AuthenticationNumber(authenticationNumber);
    }

    public boolean isRightAuthNum(String authNum){
        return authenticationNumber.equals(authNum);
    }

    public void updateAuthNum(String authNum) {
        this.authenticationNumber = new AuthenticationNumber(authNum);
    }

    public String getAuthNum(){
        return authenticationNumber.getAuthNum();
    }
}
