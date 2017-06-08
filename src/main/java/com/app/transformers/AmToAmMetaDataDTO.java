package com.app.transformers;

import com.app.DTO.AmendmentMetaDataDTO;
import com.app.model.Amendment;
import com.app.model.Amendment.ListaPredloga.PredlogAmandmana;
import com.app.DTO.AmendmentMetaDataDTO.AmendmentOperations;

public class AmToAmMetaDataDTO implements ITransformer{

	@Override
	public Object transform(Object o) {
		Amendment am=(Amendment)o;
		AmendmentMetaDataDTO amDTO=new AmendmentMetaDataDTO();
		amDTO.setId(am.getId());
		if(am.getTip().equals("Brisanje"))
			amDTO.setOperationType(AmendmentOperations.Brisanje);
		else if(am.getTip().equals("Dodavanje"))
			amDTO.setOperationType(AmendmentOperations.Dodavanje);
		else if(am.getTip().equals("Izmena"))
			amDTO.setOperationType(AmendmentOperations.Izmena);
		amDTO.setReferredActName(am.getRefAkta());
		String referredClauseId="";
		for(PredlogAmandmana predlog:am.getListaPredloga().getPredlogAmandmana()){
			if(!predlog.getRef().getClan().equals("")){
				referredClauseId=predlog.getRef().getClan();
				break;
			}
		}
		amDTO.setReferredClauseNumber(Integer.parseInt(referredClauseId));
		amDTO.setUserInfo("");
		return amDTO;
	}

}
