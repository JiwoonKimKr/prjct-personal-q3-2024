package com.givemetreat.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.user.domain.VerificationCodeEntity;

public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Integer> {

	VerificationCodeEntity findByCode(String code);

}
