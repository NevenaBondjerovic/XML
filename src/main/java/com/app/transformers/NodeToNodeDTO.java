package com.app.transformers;

import java.util.ArrayList;

import com.app.DTO.NodeDataDTO;
import com.app.DTO.NodeDataDTO.Type;
import com.app.model.Cvor;

public class NodeToNodeDTO implements ITransformer{

	@Override
	public Object transform(Object o) {
		Cvor node=(Cvor)o;
//		NodeDTO nodeDTO=new NodeDTO();
		NodeDataDTO nodeDTO=new NodeDataDTO();
//		nodeDTO.setContent(node.getContent());
		nodeDTO.setId(node.getId());
		nodeDTO.setName(node.getNaziv());
		nodeDTO.setParentId("");
		nodeDTO.setRef(null);
		nodeDTO.setContent(new ArrayList<NodeDataDTO>());
		nodeDTO.setText(new ArrayList<Object>());
		for(Object content: node.getContent()){
			if(content instanceof Cvor){
				nodeDTO.getContent().add((NodeDataDTO)new NodeToNodeDTO().transform(content));
			}else{
				nodeDTO.getText().add(content);
			}
		}
		
		if(node.getTipCvora().equals("Clan"))
			nodeDTO.setType(Type.member);
		else if(node.getTipCvora().equals("Stav"))
			nodeDTO.setType(Type.paragraph);
		else if(node.getTipCvora().equals("Tacka"))
			nodeDTO.setType(Type.clause);
		else if(node.getTipCvora().equals("Podtacka"))
			nodeDTO.setType(Type.subclause);
		else if(node.getTipCvora().equals("Alineja"))
			nodeDTO.setType(Type.indent);
		
		return nodeDTO;
	}

}
