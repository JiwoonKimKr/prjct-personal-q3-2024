package com.givemetreat;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.givemetreat.productInvoice.domain.ItemOrderedDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class JacksonParsing테스트 {

	@Test
	void test() {
		ObjectMapper objectMapper = new ObjectMapper();
		
		String jsonArray = "[{\"cartItemId\": \"1\", \"productId\": \"2\", \"quantity\": 1, \"price\": 10000, \"checked\": \"true\"},"
				+ "{\"cartItemId\": 2, \"productId\": 4, \"quantity\": 1, \"price\": 15000, \"checked\": \"false\"}]";
		
		try {
			List<ItemOrderedDto> listItems = objectMapper.readValue(jsonArray, new TypeReference<>(){});
			log.info("[🚀🚀🚀🚀🚀 JacksonParsing테스트] list<ItemOrderedDto>:{}", listItems);
//[🚀🚀🚀🚀🚀 JacksonParsing테스트] list<ItemOrderedDto>:[ItemOrderedDto(cartItemId=1, productId=2, quantity=1, price=10000, checked=true, productInvoiceId=null), ItemOrderedDto(cartItemId=2, productId=4, quantity=1, price=15000, checked=false, productInvoiceId=null)]

			
		} catch (JsonProcessingException e) {
			log.warn("[🚧🚧🚧🚧🚧 JacksonParsing테스트] 아니 왜 이게 파싱이 안 돼니!!!!");
		}
	}

}
