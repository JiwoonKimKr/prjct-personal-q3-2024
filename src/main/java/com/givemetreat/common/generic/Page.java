package com.givemetreat.common.generic;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page <T> {
	
	public Page(List<T> listVOs
			, Integer idFirst
			, Integer idLast
			, String direction
			, Integer index
			, Integer idRequested
			, Integer limit) {

		this.listEntities = listVOs;
		this.limit = limit;
		
		this.numberPageCurrent = index / limit;
		this.numberPageMax = (listVOs.size() - 1) / limit;
		
		this.direction = direction;
		
		this.indexCurrent = index;
		this.idFirst = idFirst;
		this.idLast = idLast;
		
		this.isCurrentIdFirst = idRequested >= idFirst;
		this.isCurrentIdLast = idRequested <= idLast;
		
	}
	
	private List<T> listEntities;
	private Integer limit = 3;
	
	private Integer numberPageCurrent;
	private Integer numberPageMax;
	
	private String direction;
	
	private Integer indexCurrent;
	private Integer idFirst; 
	private Integer idLast; 
	
	private Boolean isCurrentIdFirst; 
	private Boolean isCurrentIdLast; 

	public List<T> returnPageList(){
		List<T> listVOs = new ArrayList<T>();
		if(direction.equals("next")) {
			numberPageCurrent ++;
			numberPageCurrent = numberPageCurrent < numberPageMax ? numberPageCurrent : numberPageMax;
			
		} else { //pref 일 경우
			numberPageCurrent --;
			numberPageCurrent = numberPageCurrent > 0 ? numberPageCurrent : 0;
		}
		int indexStart = numberPageCurrent * limit > 0 ? numberPageCurrent * limit : 0;
		int indexFinish =(indexStart + limit) < listEntities.size() ? indexStart + limit : listEntities.size();
		for(int i= indexStart; i < indexFinish ; i++) {
			listVOs.add(listEntities.get(i));
		}		
		return listVOs;
	}
}
