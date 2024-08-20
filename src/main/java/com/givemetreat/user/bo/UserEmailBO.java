package com.givemetreat.user.bo;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.givemetreat.user.domain.VerificationCodeEntity;
import com.givemetreat.user.repository.VerificationCodeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserEmailBO {
	private final VerificationCodeRepository verificationCodeRepository;
	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String emailAddressService;
	private final Integer EXPIRATIOIN_TIME_IN_MINUTES = 5;
	
	
	public void SendSimpeVerificationMail(String receiver
									, LocalDateTime timeMailSent) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(emailAddressService);
		mailMessage.setTo(receiver);
		mailMessage.setSubject(String.format("[giveMeTreat 간식줘집사]"
				+ " E-mail Verification for %s", receiver));
		
		VerificationCodeEntity entity = VerificationCodeEntity.builder()
				.code(UUID.randomUUID().toString())
				.createdAt(timeMailSent)
				.minutesAdded(EXPIRATIOIN_TIME_IN_MINUTES)
				.build();
		
		
		verificationCodeRepository.save(entity);
		
		mailMessage.setText(entity.generatedCodeMessage());
		
		mailSender.send(mailMessage);
		log.info("[UserEmailBO SendSimpeVerificationMail()] receiver:{}, time mail sent:{}",receiver, timeMailSent);
	}
	
	
	public Boolean verifyCode(String code, LocalDateTime timeRequested) {
		VerificationCodeEntity entity = verificationCodeRepository.findByCode(code);
		
		if(ObjectUtils.isEmpty(entity)) {
			log.info("[UserEmailBO verifyCode()] Requeseted code not found. code:{}, time requested:{}", code, timeRequested);
			return false;
		}
		
		if(entity.doesGotExpired(timeRequested)) {
			log.info("[UserEmailBO verifyCode()] Requeseted code got out of time. code:{}, time requested:{}", code, timeRequested);
			return false;
		}
		
		log.info("[UserEmailBO verifyCode()] Requeseted code get validated normaly. code:{}, time requested:{}", code, timeRequested);
		verificationCodeRepository.delete(entity);
		return true;
	}
	
}
