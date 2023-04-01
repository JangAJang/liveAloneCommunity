package com.capstone.liveAloneCommunity.service.auth;

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

    private void createCode(){
        authNum = new StringBuilder();
        Random random = new Random();
        IntStream.range(0, 8).forEach(i -> authNum.append(createRandomCharacter(random)));
    }

    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        createCode();
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(TITLE.getValue());
        message.setFrom(SENDER.getValue());
        message.setText("인증 번호는 " + authNum + "입니다.", "utf-8", "html");
        return message;
    }
    private char createRandomCharacter(Random random){
        int value = random.nextInt(3);
        if(value == 0) return (char) (random.nextInt(26)+97);
        if(value == 1) return (char) (random.nextInt(26)+65);
        return (char) (random.nextInt(9));
    }
}