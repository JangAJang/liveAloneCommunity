package com.capstone.liveAloneCommunity.service.email;

import com.capstone.liveAloneCommunity.exception.email.EmailNotSentException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static com.capstone.liveAloneCommunity.service.email.EmailAuthComponent.SENDER;
import static com.capstone.liveAloneCommunity.service.email.EmailAuthComponent.TITLE;

@Component
@RequiredArgsConstructor
public class EmailConstructor {

    private final JavaMailSender mailSender;

    public void sendEmail(String email, String authNum) {
        try{
            MimeMessage message = createEmailForm(email, authNum);
            mailSender.send(message);
        } catch (MessagingException e){
            e.printStackTrace();
            throw new EmailNotSentException();
        }
    }

    public MimeMessage createEmailForm(String email, String authNum) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(TITLE.getValue());
        message.setFrom(SENDER.getValue());
        message.setText(createMessageText(email, authNum), "utf-8", "html");
        return message;
    }

    private String createMessageText(String email, String authNum){
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
                + authNum + "</strong><div><br/> "
                +"</div>";
    }
}
