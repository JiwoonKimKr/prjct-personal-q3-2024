package com.givemetreat.user.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	@Column(name = "loginId")
	private String loginId;
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
	
	public List<String> generatedCodeMessage() {
		String TimeExpiredformatted = createdAt
								.plusMinutes(minutesAdded)
								.format(DateTimeFormatter.ofPattern("yy-MM-dd HH시 mm분 ss초 까지"));
		
		return new ArrayList<>(Arrays.asList( code
										, TimeExpiredformatted));
	}
}
