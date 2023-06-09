package com.capstone.liveAloneCommunity.config.auth;

import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername_Username(username)
                .map(this::createUserDetails)
                .orElseThrow(()-> new UsernameNotFoundException(username+" -> cannot find from DB"));
    }

    private UserDetails createUserDetails(Member member){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());
        return new org.springframework.security.core.userdetails.User(
                member.getUsername(),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}

