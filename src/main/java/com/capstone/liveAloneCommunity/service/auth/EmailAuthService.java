package com.capstone.liveAloneCommunity.service.auth;

import com.capstone.liveAloneCommunity.dto.email.EmailAuthRequestDto;
import com.capstone.liveAloneCommunity.entity.email.EmailAuth;
import com.capstone.liveAloneCommunity.repository.email.EmailAuthRepository;
import com.capstone.liveAloneCommunity.service.member.MemberValidator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.stream.IntStream;

import static com.capstone.liveAloneCommunity.service.auth.EmailAuthComponent.SENDER;
import static com.capstone.liveAloneCommunity.service.auth.EmailAuthComponent.TITLE;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailAuthService {

    private StringBuilder authNum;
    private final JavaMailSender mailSender;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberValidator memberValidator;

    public String sendEmail(EmailAuthRequestDto toEmail) throws MessagingException, UnsupportedEncodingException{
        String email = toEmail.getEmail();
        memberValidator.validateEmail(email);
        mailSender.send(createEmailForm(email));
        saveEmailAuth(email);
        return authNum.toString();
    }

    private void saveEmailAuth(String email){
        emailAuthRepository.findByEmail_Email(email)
                .orElse(emailAuthRepository.save(new EmailAuth(email, authNum.toString())))
                .updateAuthNum(authNum.toString());
    }

    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(TITLE.getValue());
        message.setFrom(SENDER.getValue());
        message.setText(createMessageText(email), "utf-8", "html");
        return message;
    }

    private String createMessageText(String email){
        createCode();
        return "<div style='margin:100px;'>"
                +"<h1> 자취생들을 위한 커뮤니티! Live Alone Network, L.A.N</h1>"
                +"<h1 style='color:orange;'>인증번호 안내 메일입니다.</h1>"
                +"<br>"
                +"<p>" + email + "님! L.A.N <p>"
                +"<br>"
                +"<p>해당 이메일은 회원가입을 위한 인증번호 안내 메일입니다.<p>"
                +"<br>"
                +"<p>하단 인증번호를 '이메일 인증번호' 칸에 입력하여 가입을 완료해주세요.<p>"
                +"<br>"
                +"<div align='center' style='border:1px solid black; font-family:verdana';>"
                +"<h3 style='color:green;'>회원가입 인증 코드입니다.</h3>"
                +"<div style='font-size:130%'>"
                +"CODE : <strong>"
                +authNum + "</strong><div><br/> "
                +"</div>";
    }

    private void createCode(){
        authNum = new StringBuilder();
        Random random = new Random();
        IntStream.range(0, 8).forEach(i -> authNum.append(createRandomCharacter(random)));
    }
    private char createRandomCharacter(Random random){
        int value = random.nextInt(3);
        if(value == 0) return (char) (random.nextInt(26)+97);
        if(value == 1) return (char) (random.nextInt(26)+65);
        return (char) (random.nextInt(9));
    }
}