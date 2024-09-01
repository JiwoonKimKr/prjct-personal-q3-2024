package com.givemetreat.user.bo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.givemetreat.common.utils.EncryptUtils;
import com.givemetreat.user.domain.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class UserBOTest {
	@Autowired
	UserBO userBO;
	
	@Test
	void 회원가입비밀번호체크() {
		String loginId = "qwerty@qwerty.com";
		String password = "aaaa";
		String nickname = "천상의도적_치지직";
				
		String salt = "fff574b245d4a0afc07153687138c219730c024e";
		String hashedPassword = EncryptUtils.sha256(salt, password);
		UserEntity user = userBO.addUser(loginId, hashedPassword, salt, nickname);
		log.info("[userBO_Test: 회원가입 관련 비밀번호 Salt + SHA256 방식 검증] hashedPassword:{}, salt:{}", hashedPassword, salt);
		assertEquals(user.getPassword(), "7cc68c9637d50b3676c36c45bcf1ea6549bd3176b2e7bcf703786c8c7bbfb080");
	}
	
	
}
