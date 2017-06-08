package com.app.transformers;

import java.util.ArrayList;
import java.util.List;

import com.app.DTO.AmendmentDTO;
import com.app.DTO.AmendmentDTO.AmendmentStatus;
import com.app.DTO.AmendmentDTO.AmendmentType;
import com.app.DTO.AmendmentProposalDTO;
import com.app.model.Amendment;

public class AmendmentToAmendmentDTO  implements ITransformer{

	@Override
	public Object transform(Object o) {
		Amendment am=(Amendment)o;
		AmendmentDTO amDTO=new AmendmentDTO();
		
		amDTO.setActName(am.getRefAkta());
		amDTO.setDescription(am.getObrazlozenje().getContent());
		amDTO.setId(am.getId());
		amDTO.setName(am.getNaziv());
		List<AmendmentProposalDTO> proposalDTOList=new ArrayList<AmendmentProposalDTO>();
		for(Amendment.ListaPredloga.PredlogAmandmana proposal:am.getListaPredloga().getPredlogAmandmana()){
			proposalDTOList.add((AmendmentProposalDTO)new AmProposalToAmProposalDTO().transform(proposal));
		}
		amDTO.setProposalList(proposalDTOList);
		
		if(am.getStatus().equals("Prihvacen"))
			amDTO.setStatus(AmendmentStatus.Prihvacen);
		else if(am.getStatus().equals("U_procesu"))
			amDTO.setStatus(AmendmentStatus.U_procesu);
		
		if(am.getTip().equals("Dodavanje"))
			amDTO.setType(AmendmentType.Dodavanje);
		else if(am.getTip().equals("Izmena"))
			amDTO.setType(AmendmentType.Izmena);
		else if(am.getTip().equals("Brisanje"))
			amDTO.setType(AmendmentType.Brisanje);
		
		amDTO.setUser(am.getPredlagac());
		
		return amDTO;
	}

}
