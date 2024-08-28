package com.givemetreat.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.product.domain.CategoryProduct;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Data
@Slf4j
public class SearchMapFromKeyword {
	private String productName = null;
	private CategoryProduct category = null;
	private AgePet agePetProper = null;
	
	private List<String> listCategoryKibble = new ArrayList<>(Arrays.asList("사료", "kibble")); 
	private List<String> listCategoryTreat = new ArrayList<>(Arrays.asList("간식", "treat")); 

	private List<String> listAgePetUnder6Months = new ArrayList<>(Arrays.asList("강아지", "어린", "약한", "puppy", "small")); 
	private List<String> listAgePetAdult = new ArrayList<>(Arrays.asList("성견", "어른", "튼튼", "adult")); 
	private List<String> listAgePetSenior = new ArrayList<>(Arrays.asList("노령견", "노견", "고령견", "노령", "고령", "senior")); 
	
	@Override
	public String toString() {
		return "[SearchMapFromKeyword] productName: "+ productName + ", category: " + category + ", agePetProper: " + agePetProper;
	}
	

	public SearchMapFromKeyword(String keyword) {
		Map<CategoryProduct, Integer> mapCategory = new HashMap<>();
		mapCategory.put(CategoryProduct.kibble, 0);
		mapCategory.put(CategoryProduct.treat, 0);

		Map<AgePet, Integer> mapAgePet = new HashMap<>();
		mapAgePet.put(AgePet.under6months, 0);
		mapAgePet.put(AgePet.adult, 0);
		mapAgePet.put(AgePet.senior, 0);
		
		//빈칸으로 나누고 List 형태로 만들기
		List<String> listString = new ArrayList<>(Arrays.asList(keyword.trim().split(" "))) ;

		log.info("[SearchMapFromKeyword] list<String> from keyword. keyword:{}, listString:{}", keyword, listString.toString());
		
		for(String unit: listString) {
			if(listCategoryKibble.contains(unit)) {
				mapCategory.put(CategoryProduct.kibble, mapCategory.get(CategoryProduct.kibble) + 1);
				continue;
			}
			if(listCategoryTreat.contains(unit)) {
				mapCategory.put(CategoryProduct.treat, mapCategory.get(CategoryProduct.treat) + 1);
				continue;
			}
			
			if(listAgePetUnder6Months.contains(unit)) {
				mapAgePet.put(AgePet.under6months, mapAgePet.get(AgePet.under6months) + 1);
				continue;
			}
			if(listAgePetAdult.contains(unit)) {
				mapAgePet.put(AgePet.adult, mapAgePet.get(AgePet.adult) + 1);
				continue;
			}
			if(listAgePetSenior.contains(unit)) {
				mapAgePet.put(AgePet.senior, mapAgePet.get(AgePet.senior) + 1);
				continue;
			}
			
			//productName이 비어있으면 가장 빨리 도출된 단어를 name으로 지정
			if(ObjectUtils.isEmpty(this.productName)) {
				this.productName = unit;
			}
		}
		
		//Category와 AgePetProper 중 Map 각 Key:Value 중 가장 value값이 큰 key값 도출하기
		
		int maxCountCategory = 0;
		int maxCountAgePet = 0;
		
		Set<CategoryProduct> keysCategory = mapCategory.keySet();
		Iterator<CategoryProduct> iterCategory = keysCategory.iterator();
		
		while(iterCategory.hasNext()) {
			CategoryProduct current = iterCategory.next();
			int count = mapCategory.get(current);
			
			if(count > maxCountCategory) {
				maxCountCategory = count;
				this.category = current;
			}
		}
		
		Set<AgePet> keysAgePet = mapAgePet.keySet();
		Iterator<AgePet> iterAgePet = keysAgePet.iterator();
		
		while(iterAgePet.hasNext()) {
			AgePet current = iterAgePet.next();
			int count = mapAgePet.get(current);
			
			if(count > maxCountAgePet) {
				maxCountAgePet = count;
				this.agePetProper = current;
			}
		}
		
		log.info("[SearchMapFromKeyword] searchMap class has made. keyword:{}", keyword);
	}
}
