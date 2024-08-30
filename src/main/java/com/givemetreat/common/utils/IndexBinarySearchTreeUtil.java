package com.givemetreat.common.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.givemetreat.common.generic.VOforIndexing;

@Component
public class IndexBinarySearchTreeUtil {
	
	public Integer findIndexFromList(List<VOforIndexing> list, Integer idRequested) {
		int length = list.size() - 1;
		Integer indexMax = length;
		Integer indexMin = 0; 
		Integer indexCurrent;
		if(list.get(length).getId() == idRequested) {
			return length;
		}
		
		while(true) {
			indexCurrent = (indexMax + indexMin) / 2;
			Integer valueCurrent = list.get(indexCurrent).getId();
			if(valueCurrent == idRequested) {
				return indexCurrent;
			}
			//내림차순으로 정렬하는 탓에, 조회한 Field값이 작으면 거꾸로 Index값이 뒤쪽으로 간다
			if(idRequested < valueCurrent) {
				indexMin = indexCurrent;
			} else {
				indexMax = indexCurrent;
			}
		}
	}
}
