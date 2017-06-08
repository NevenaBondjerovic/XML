package com.app.transformers;

import com.app.DTO.RefDTO;
import com.app.model.Ref;

public class RefToRefDTO implements ITransformer{

	@Override
	public Object transform(Object o) {
		Ref ref=(Ref)o;
		RefDTO refDTO=new RefDTO();
		
		refDTO.setAct(ref.getAkt());
		refDTO.setClause(ref.getTacka());
		refDTO.setHead(ref.getGlava());
		refDTO.setIndent(ref.getAlineja());
		refDTO.setMember(ref.getClan());
		refDTO.setParagraph(ref.getStav());
		refDTO.setPart(ref.getDeo());
		refDTO.setSection(ref.getOdeljak());
		refDTO.setSubclause(ref.getPodtacka());
		refDTO.setSubsection(ref.getPododeljak());
		refDTO.setContent(ref.getContent());
		
		return refDTO;
	}

}
