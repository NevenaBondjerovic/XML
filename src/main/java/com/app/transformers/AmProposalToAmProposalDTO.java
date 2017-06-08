package com.app.transformers;

import com.app.DTO.AmendmentProposalDTO;
import com.app.DTO.NodeDataDTO;
import com.app.DTO.RefDTO;
import com.app.model.Amendment;

public class AmProposalToAmProposalDTO implements ITransformer{

	@Override
	public Object transform(Object o) {
		Amendment.ListaPredloga.PredlogAmandmana pr=(Amendment.ListaPredloga.PredlogAmandmana)o;
		AmendmentProposalDTO prDTO=new AmendmentProposalDTO();
		
		prDTO.setContent(pr.getSadrzaj().getContent());
		prDTO.setNode((NodeDataDTO)new NodeToNodeDTO().transform(pr.getCvor()));
		prDTO.setRef((RefDTO)new RefToRefDTO().transform(pr.getRef()));
		
		return prDTO;
	}

}
