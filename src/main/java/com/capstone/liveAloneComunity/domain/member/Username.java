package com.capstone.liveAloneComunity.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Username {

    @Column(name = "USERNAME")
    private String username;

    public boolean equals(String username){
        return this.username.equals(username);
    }
}
