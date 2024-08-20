package com.givemetreat.user.bo;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.givemetreat.user.domain.VerificationCodeEntity;
import com.givemetreat.user.repository.VerificationCodeRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserEmailBO {
	private final VerificationCodeRepository verificationCodeRepository;
	
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;
	
	@Value("${spring.mail.username}")
	private String emailAddressService;
	@Value("${spring.mail.templates.logo-path}")
	private Resource logoFile;
	
	private final Integer EXPIRATIOIN_TIME_IN_MINUTES = 5;
	
	public Boolean sendVerificationMailWithTemplate(String receiver, LocalDateTime timeMailSent) {
		
		//code 관련 entity 생성 & repository 저장
		VerificationCodeEntity entity = VerificationCodeEntity.builder()
														.code(UUID.randomUUID().toString())
														.createdAt(timeMailSent)
														.minutesAdded(EXPIRATIOIN_TIME_IN_MINUTES)
														.build();
		verificationCodeRepository.save(entity);
		
		Map<String, Object> templateModel = new HashMap<>();
		//MVC의 model에 넣는 상황!
		templateModel.put("code", entity.generatedCodeMessage().get(0));
		templateModel.put("timeExpiredAt", entity.generatedCodeMessage().get(1));
		
		//제목
		String subject = String.format("E-mail Verification for %s", receiver);
		
		Context thymeleafContext = new Context();
		thymeleafContext.setVariables(templateModel);
		String htmlBody = templateEngine.process("emailLayouts/email4CodeVerification.html", thymeleafContext);
		return sendHtmlMessage(receiver, subject, htmlBody);
	}
	
	private Boolean sendHtmlMessage(String receiver, String subject, String htmlBody) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(emailAddressService);
			helper.setTo(receiver);
			helper.setSubject(subject);
			helper.setText(htmlBody, true);
			helper.addInline("logo.png", logoFile);
			
		} catch (MessagingException e) {
			log.warn("[UserEmailBO sendHtmlMessage()]"
					+ " Multi-Media message for Sending Email failed to get translated to html."
					+ " receiver:{}, subject:{}, htmlBody:{}", receiver, subject, htmlBody);
			return false;
		}
		mailSender.send(message);
		return true;
	}
	
	
	public void sendSimpeVerificationMail(String receiver
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
		
		mailMessage.setText(entity.generatedCodeMessage().get(0) + entity.generatedCodeMessage().get(1));
		
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
