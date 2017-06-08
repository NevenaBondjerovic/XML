package com.app.transformers;

import com.app.DTO.NodeDataDTO;
import com.app.DTO.NodeDataDTO.Type;
import com.app.model.Cvor;

public class NodeDTOToNode implements ITransformer{

	@Override
	public Object transform(Object o) {
		NodeDataDTO nodeDTO=(NodeDataDTO)o;
		Cvor node=new Cvor();
		
		node.setId(nodeDTO.getId());
		node.setNaziv(nodeDTO.getName());
//		node.setTipCvora(nodeDTO.getType().toString());
		if(nodeDTO.getType().equals(Type.member)){
			node.setTipCvora("Clan");
		}else if(nodeDTO.getType().equals(Type.paragraph)){
			node.setTipCvora("Stav");
		}else if(nodeDTO.getType().equals(Type.clause)){
			node.setTipCvora("Tacka");
		}else if(nodeDTO.getType().equals(Type.subclause)){
			node.setTipCvora("Podtacka");
		}else if(nodeDTO.getType().equals(Type.indent)){
			node.setTipCvora("Alineja");
		}
		for(NodeDataDTO content:nodeDTO.getContent()){
			node.getContent().add((Cvor)new NodeDTOToNode().transform(content));
		}
		for(Object content:nodeDTO.getText()){
			node.getContent().add(content);
		}
		return node;
	}

}
