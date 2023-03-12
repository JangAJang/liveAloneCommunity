package com.capstone.liveAloneComunity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "REFRESH_TOKEN")
@Entity
@Data
@Builder
public class RefreshToken {

    @Id
    @Column(name = "RT_KEY")
    private String key;

    @Column(name = "RT_VALUE")
    private String value;

    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void updateValue(String token) {
        this.value = token;
    }
}

