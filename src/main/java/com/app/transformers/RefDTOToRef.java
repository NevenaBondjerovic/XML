package com.app.transformers;

import com.app.DTO.RefDTO;
import com.app.model.Ref;

public class RefDTOToRef implements ITransformer{

	@Override
	public Object transform(Object o) {
		RefDTO refDTO=(RefDTO)o;
		Ref ref=new Ref();
		
		ref.setAkt(refDTO.getAct());
		ref.setAlineja(refDTO.getIndent());
		ref.setClan(refDTO.getMember());
		ref.setDeo(refDTO.getPart());
		ref.setGlava(refDTO.getHead());
		ref.setOdeljak(refDTO.getSection());
		ref.setPododeljak(refDTO.getSubsection());
		ref.setPodtacka(refDTO.getSubclause());
		ref.setStav(refDTO.getParagraph());
		ref.setTacka(refDTO.getClause());
		ref.setContent(refDTO.getContent());
		
		return ref;
	}

}
