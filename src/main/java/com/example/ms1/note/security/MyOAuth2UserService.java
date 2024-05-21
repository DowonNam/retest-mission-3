package com.example.ms1.note.security;

import com.example.ms1.note.member.Member;
import com.example.ms1.note.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        MySocialUser mySocialUser;

        switch (registrationId){
            case "google" -> mySocialUser = googleService(user);
            case "kakao" -> mySocialUser = kakakoService(user);
            case "naver" -> mySocialUser = naverService(user);
            default -> throw new IllegalStateException("Unexpected value " + registrationId);
        }

        Member member = memberRepository.findByLoginId(mySocialUser.getSub()).orElse(null);

        if(member == null){
            member = new Member();
            member.setLoginId(mySocialUser.getSub());
            member.setPassword(mySocialUser.getPass());
            member.setNickname(mySocialUser.getName());
            member.setEmail(mySocialUser.getEmail());
            member.setCreateDate(LocalDateTime.now());

            memberRepository.save(member);
        }

        return super.loadUser(userRequest);
    }

    public MySocialUser googleService(OAuth2User user){
        String sub = user.getAttribute("sub");
        String pass = "";
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");

        return new MySocialUser(sub, pass, name, email);
    }

    public MySocialUser kakakoService(OAuth2User user){
        String sub = user.getAttribute("id").toString();
        String pass = "";

        Map<String, Object> kakaoAccount = user.getAttribute("kakao_account");
        Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");
        String name = (String)profile.get("nickname");

        String email = (String)kakaoAccount.get("email");

        return new MySocialUser(sub,pass,name,email);
    }

    public MySocialUser naverService(OAuth2User user){
        Map<String,Object> reponse = user.getAttribute("response");
        String sub = reponse.get("id").toString();
        String pass = "";
        String name = reponse.get("name").toString();
        String email = reponse.get("email").toString();

        return new MySocialUser(sub,pass,name,email);
    }
}
