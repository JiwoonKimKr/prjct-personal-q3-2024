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
	
	private String direction;
	
	private Integer indexCurrent;
	private Integer idFirst; 
	private Integer idLast; 
	
	private Boolean isCurrentIdFirst; 
	private Boolean isCurrentIdLast; 

	public List<T> returnPageList(){
		List<T> listVOs = new ArrayList<T>();
		if(direction.equals("next") && !isCurrentIdLast) {
			int indexStart = (numberPageCurrent + 1)* limit;
			int indexFinish = (indexStart + limit) < listEntities.size() ? indexStart + limit : listEntities.size();
			for(int i= indexStart; i < indexFinish ; i++) {
				listVOs.add(listEntities.get(i));
			}
		} else if (direction.equals("next") && isCurrentIdLast) {
			listVOs.add(listEntities.get(listEntities.size()-1));
		}
			
		if(direction.equals("prev") && !isCurrentIdFirst) {
			int indexStart = (numberPageCurrent - 1) * limit > 0 ? (numberPageCurrent - 1) * limit : 0;
			int indexFinish = indexStart + limit;
			for(int i= indexStart; i < indexFinish ; i++) {
				listVOs.add(listEntities.get(i));
			}
		} else if(direction.equals("prev") && isCurrentIdFirst) {
			for(int i= 0; i < limit ; i++) {
				listVOs.add(listEntities.get(i));
			}
		}
		
		return listVOs;
	}
}
