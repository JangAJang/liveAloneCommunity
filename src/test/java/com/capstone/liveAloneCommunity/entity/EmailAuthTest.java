package com.capstone.liveAloneCommunity.entity;

import com.capstone.liveAloneCommunity.entity.email.EmailAuth;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EmailAuthTest {

    @Test
    @DisplayName("EmailAuth에서 같은 값을 입력하면 isRightAuthNum에서 참을 반환하고, 아니면 거짓을 반환한다.")
    public void isRightAuthNumTest() throws Exception{
        //given
        EmailAuth emailAuth = new EmailAuth("email", "auth");
        //when
        String authNum = "auth";
        String different = "different";
        //then
        Assertions.assertThat(emailAuth.isRightAuthNum(authNum)).isEqualTo(true);
        Assertions.assertThat(emailAuth.isRightAuthNum(different)).isEqualTo(false);
    }
}
