package com.capstone.liveAloneCommunity.factory;

import com.capstone.liveAloneCommunity.service.email.EmailAuthConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EmailAuthConstructorTest {
    
    @Test
    @DisplayName("EmailAuthConstructor에서 getAuthName을 사용하면 매번 새로운 문자열을 반환한다.")
    public void constructorTest() throws Exception{
        //given
        EmailAuthConstructor emailAuthConstructor = new EmailAuthConstructor();
        //when
        String auth1 = emailAuthConstructor.getAuthNum();
        String auth2 = emailAuthConstructor.getAuthNum();
        String auth3 = emailAuthConstructor.getAuthNum();
        //then
        Assertions.assertThat(auth1.equals(auth2)).isFalse();
        Assertions.assertThat(auth2.equals(auth3)).isFalse();
        Assertions.assertThat(auth3.equals(auth1)).isFalse();
    }
}
