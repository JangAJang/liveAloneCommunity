package com.capstone.liveAloneCommunity.service.email;

import com.capstone.liveAloneCommunity.dto.email.EmailAuthRequestDto;
import com.capstone.liveAloneCommunity.dto.email.EmailAuthResponseDto;
import com.capstone.liveAloneCommunity.dto.email.EmailAuthValidateRequestDto;
import com.capstone.liveAloneCommunity.entity.email.EmailAuth;
import com.capstone.liveAloneCommunity.exception.email.EmailAuthNotEqualException;
import com.capstone.liveAloneCommunity.exception.email.EmailNotSentException;
import com.capstone.liveAloneCommunity.exception.email.TryToSendEmailAgain;
import com.capstone.liveAloneCommunity.repository.email.EmailAuthRepository;
import com.capstone.liveAloneCommunity.service.member.MemberValidator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.capstone.liveAloneCommunity.service.email.EmailAuthComponent.SENDER;
import static com.capstone.liveAloneCommunity.service.email.EmailAuthComponent.TITLE;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailAuthService {

    private final EmailAuthConstructor emailAuthConstructor;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberValidator memberValidator;
    private final EmailConstructor emailConstructor;

    public void verifyEmailAuth(EmailAuthValidateRequestDto request){
        EmailAuth requested = getRequestedEmailAuth(request.getEmail());
        if(requested.isRightAuthNum(request.getAuthNum())) return;
        throw new EmailAuthNotEqualException();
    }

    public EmailAuthResponseDto sendEmail(EmailAuthRequestDto toEmail){
        String email = toEmail.getEmail();
        String authNum = emailAuthConstructor.getAuthNum();
        memberValidator.validateEmail(email);
        emailConstructor.sendEmail(email, authNum);
        saveEmailAuth(email, authNum);
        return EmailAuthResponseDto.toDto(authNum);
    }

    private EmailAuth getRequestedEmailAuth(String email){
        return emailAuthRepository.findByEmail_Email(email).orElseThrow(EmailNotSentException::new);
    }

    private void saveEmailAuth(String email, String authNum){
        emailAuthRepository.findByEmail_Email(email)
                .orElse(emailAuthRepository.save(new EmailAuth(email, authNum)))
                .updateAuthNum(authNum);
    }
}