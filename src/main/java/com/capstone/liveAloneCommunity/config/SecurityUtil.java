package com.capstone.liveAloneCommunity.config;

import com.capstone.liveAloneCommunity.exception.authentication.NeedToLoginException;
import com.capstone.liveAloneCommunity.exception.authentication.NotRightAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static Long getCurrentMemberId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new NeedToLoginException();
        if(authentication.getName() == null) throw new NotRightAuthenticationException();
        return Long.parseLong(authentication.getName());
    }
}
