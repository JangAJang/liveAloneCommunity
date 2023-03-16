package com.capstone.liveAloneComunity.entity;

import com.capstone.liveAloneComunity.domain.MemberInfo;
import com.capstone.liveAloneComunity.domain.Password;
import com.capstone.liveAloneComunity.domain.Username;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Username username;

    @Embedded
    private MemberInfo memberInfo;

    @Embedded
    private Password password;

    @Column(name = "MEMBER_ROLE")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public String getUsername(){
        return this.username.getUsername();
    }

    public boolean isRightPassword(String password){
        return this.password.equals(password);
    }

    public String getPassword(){
        return this.password.getPassword();
    }
}
