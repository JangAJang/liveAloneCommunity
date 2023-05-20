package com.capstone.liveAloneCommunity.service.auth;

import com.capstone.liveAloneCommunity.config.oauth.provider.KakaoProfile;
import com.capstone.liveAloneCommunity.domain.member.Email;
import com.capstone.liveAloneCommunity.domain.member.Nickname;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.member.Role;
import com.capstone.liveAloneCommunity.entity.token.OAuthToken;
import com.capstone.liveAloneCommunity.exception.authentication.LogInAgainException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public LogInRequestDto getLogInRequestByCode(String code){
        KakaoProfile kakaoProfile = getKakaoInfo(code);
        Member member = memberRepository.findByUsername_Username("KAKAO_" + kakaoProfile.getEmail())
                .orElseGet(()-> createMemberByKakaoInfo(kakaoProfile));
        return LogInRequestDto.builder()
                .username(member.getUsername())
                .password("KAKAO_" + kakaoProfile.getId()).build();
    }

    private Member createMemberByKakaoInfo(KakaoProfile kakaoProfile){
        Member member = Member.builder()
                .username(new Username("KAKAO_" + kakaoProfile.getEmail()))
                .email(new Email(kakaoProfile.getEmail()))
                .nickname(new Nickname(kakaoProfile.getNickname()))
                .password(new Password(passwordEncoder.encode("KAKAO_" + kakaoProfile.getId())))
                .role(Role.USER)
                .build();
        memberRepository.save(member);
        return member;
    }

    private KakaoProfile getKakaoInfo(String code){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(getKakaoInfoByToken(getKakaoToken(code, objectMapper).getAccess_token()).getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new LogInAgainException();
        }
    }

    private OAuthToken getKakaoToken(String code, ObjectMapper objectMapper){
        try {
            return objectMapper.readValue(getKakaoToken(code).getBody(), OAuthToken.class);
        } catch (
                JsonProcessingException e) {
            throw new LogInAgainException();
        }
    }

    private HttpHeaders createHeaderForKakaoTokenRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private ResponseEntity<String> getKakaoToken(String code){
        RestTemplate template = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(createBodyParams(code), createHeaderForKakaoTokenRequest());
        return template.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);
    }

    private MultiValueMap<String, String> createBodyParams(String code){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "8ca91c36f6c867eabd678eb00abb06e3");
        params.add("redirect_uri", "http://localhost:5173/auth/kakao-login");
        params.add("code", code);
        return params;
    }

    private ResponseEntity<String> getKakaoInfoByToken(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);
        return restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoProfileRequest, String.class);
    }
}
