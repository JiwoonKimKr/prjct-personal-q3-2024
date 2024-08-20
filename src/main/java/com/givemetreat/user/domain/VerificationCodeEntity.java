package com.givemetreat.user.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Table(name= "verification_code")
@Entity
public class VerificationCodeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "userId")
	private int userId;
	private String code;
	
	@Column(name = "minutesAdded")
	private Integer minutesAdded;
	
	@CreationTimestamp
	@Column(name = "createdAt")
	private LocalDateTime createdAt;
	
	public Boolean doesGotExpired(LocalDateTime timeRequested) {
		LocalDateTime timeExpired = createdAt.plusMinutes(minutesAdded);
		return timeRequested.isAfter(timeExpired); 
	}
	
	public String generatedCodeMessage() {
		String TimeExpiredformatted = createdAt
								.plusMinutes(minutesAdded)
								.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
		
		return String.format("[VerificationCode]__code__: %s ,__Expired At__: %s"
				+ "</html>",  code, TimeExpiredformatted);
		
	}
}
