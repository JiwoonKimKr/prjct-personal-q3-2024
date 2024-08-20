package com.givemetreat.user.bo;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.givemetreat.user.domain.VerificationCodeEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserEmailBO {
	@Value("${spring.mail.username}")
	private String emailAddressService;
	private final Integer EXPIRATIOIN_TIME_IN_MINUTES = 5;
	
	private final JavaMailSender mailSender;
	
	public void SendSimpeVerificationMail(String receiver
			, LocalDateTime timeMailSended) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(emailAddressService);
		mailMessage.setTo(receiver);
		mailMessage.setSubject(String.format("[giveMeTreat 간식줘집사]"
				+ " E-mail Verification for %s", receiver));
		
		VerificationCodeEntity entity = VerificationCodeEntity.builder()
				.code(UUID.randomUUID().toString())
				.createdAt(timeMailSended)
				.minutesAdded(EXPIRATIOIN_TIME_IN_MINUTES)
				.build();
		
		mailMessage.setText("서리태(●'◡'●)(●'◡'●)(●'◡'●)");
		
		mailSender.send(mailMessage);
	}
	
}
