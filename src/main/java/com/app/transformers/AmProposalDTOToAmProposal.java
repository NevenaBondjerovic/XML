package com.app.transformers;

import java.util.ArrayList;
import java.util.List;

import com.app.DTO.AmendmentProposalDTO;
import com.app.model.Amendment;
import com.app.model.Ref;

public class AmProposalDTOToAmProposal implements ITransformer{

	@Override
	public Object transform(Object o) {
		AmendmentProposalDTO prDTO=(AmendmentProposalDTO)o;
		Amendment.ListaPredloga.PredlogAmandmana pr=new Amendment.ListaPredloga.PredlogAmandmana();
		
		pr.setRef((Ref)new RefDTOToRef().transform(prDTO.getRef()));
		Amendment.ListaPredloga.PredlogAmandmana.Sadrzaj content=new Amendment.ListaPredloga.PredlogAmandmana.Sadrzaj();
		List<Object> list=new ArrayList<>();
		if(prDTO.getContent().size()>0){
			for(Object listItem:prDTO.getContent())
				list.add(listItem);
		}
		content.setContent(list);
		pr.setSadrzaj(content);
		return pr;
	}

}
