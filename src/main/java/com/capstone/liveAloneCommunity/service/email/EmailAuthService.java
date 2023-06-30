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
public class EmailAuthService {

    private final EmailAuthConstructor emailAuthConstructor;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberValidator memberValidator;
    private final EmailConstructor emailConstructor;

    @Transactional
    public void verifyEmailAuth(final EmailAuthValidateRequestDto request) {
        memberValidator.validateEmail(request.getEmail());
        EmailAuth requested = getRequestedEmailAuth(request.getEmail());
        if(requested.isRightAuthNum(request.getAuthNum())) return;
        throw new EmailAuthNotEqualException();
    }

    @Transactional
    public EmailAuthResponseDto sendEmail(final EmailAuthRequestDto toEmail) {
        String email = toEmail.getEmail();
        String authNum = emailAuthConstructor.getAuthNum();
        memberValidator.validateEmail(email);
        emailConstructor.sendEmail(email, authNum);
        saveEmailAuth(email, authNum);
        return EmailAuthResponseDto.toDto(authNum);
    }

    private EmailAuth getRequestedEmailAuth(final String email) {
        return emailAuthRepository.findByEmail_Email(email).orElseThrow(EmailNotSentException::new);
    }

    private void saveEmailAuth(final String email, final String authNum) {
        emailAuthRepository.findByEmail_Email(email)
                .orElse(emailAuthRepository.save(new EmailAuth(email, authNum)))
                .updateAuthNum(authNum);
    }
}