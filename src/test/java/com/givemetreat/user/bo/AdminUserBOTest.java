package com.givemetreat.user.bo;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.givemetreat.user.domain.AdminUserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class AdminUserBOTest {
	@Autowired
	AdminUserBO adminUserBO;

	@Test
	void jUnit테스트_GetListUserVOs() {
		List<AdminUserVO> listVOs = adminUserBO.getListUserVOs(null, null, null, null, null, null);
		
		log.info("[AdminUserBOTest jUnit테스트_GetListUserVOs]");
		assertEquals(listVOs.size(), 4);
	}

}
