package com.capstone.liveAloneCommunity.config.oauth;

import com.capstone.liveAloneCommunity.config.auth.CustomUserDetails;
import com.capstone.liveAloneCommunity.config.oauth.provider.*;
import com.capstone.liveAloneCommunity.domain.member.Email;
import com.capstone.liveAloneCommunity.domain.member.Nickname;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.member.Role;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = initializeUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = createInfoByProvider(oAuth2User, userRequest);
        Member member = memberRepository.findByUsername_Username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .orElse(createUser(oAuth2UserInfo));
        return new CustomUserDetails(member, oAuth2User.getAttributes());
    }

    private Member createUser(OAuth2UserInfo oAuth2UserInfo){
        Member member = Member.builder()
                .username(new Username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId()))
                .password(new Password(passwordEncoder.encode("겟인데어")))
                .nickname(new Nickname(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId()))
                .email(new Email(oAuth2UserInfo.getEmail()))
                .role(Role.USER)
                .providerId(oAuth2UserInfo.getProviderId())
                .provider(oAuth2UserInfo.getProvider())
                .build();
        memberRepository.save(member);
        return member;
    }

    private OAuth2User initializeUser(OAuth2UserRequest userRequest){
        return super.loadUser(userRequest);
    }

    private OAuth2UserInfo createInfoByProvider(OAuth2User oAuth2User, OAuth2UserRequest userRequest){
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            return new GoogleUserInfo(oAuth2User.getAttributes());
        }
        if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            return  new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }
        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            return new KakaoUserInfo((Map)oAuth2User.getAttributes().get("profile"));
        }
        return null;
    }
}