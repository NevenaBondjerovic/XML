package com.app.transformers;

import java.util.ArrayList;
import java.util.List;

import com.app.DTO.AmendmentDTO;
import com.app.DTO.AmendmentProposalDTO;
import com.app.model.Amendment;
import com.app.model.Amendment.ListaPredloga.PredlogAmandmana;

public class AmendmentDTOToAmendment implements ITransformer{

	@Override
	public Object transform(Object o) {
		AmendmentDTO amDTO=(AmendmentDTO)o;
		Amendment am=new Amendment();
		
		am.setId(amDTO.getId());
		Amendment.ListaPredloga list=new Amendment.ListaPredloga();
		List<Amendment.ListaPredloga.PredlogAmandmana> proposals=new ArrayList<>();
		
		if(amDTO.getProposalList().size()>0){
			for(AmendmentProposalDTO prDTO:amDTO.getProposalList())
			proposals.add((PredlogAmandmana)new AmProposalDTOToAmProposal().transform(prDTO));
		}
		list.setPredlogAmandmana(proposals);
		am.setListaPredloga(list);
		am.setNaziv(amDTO.getName());

		Amendment.Obrazlozenje descriptions=new Amendment.Obrazlozenje();
		List<Object> content=new ArrayList<>();
		for(Object desc:amDTO.getDescription())
			content.add(desc);
		descriptions.setContent(content);
		am.setObrazlozenje(descriptions);
		
		am.setPredlagac(amDTO.getUser());
		am.setRefAkta(amDTO.getActName());
		am.setStatus(amDTO.getStatus().toString());
		am.setTip(amDTO.getType().toString());
		
		return am;
	}

}
