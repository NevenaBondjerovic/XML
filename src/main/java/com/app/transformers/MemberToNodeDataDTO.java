package com.app.transformers;

import java.util.ArrayList;

import com.app.DTO.NodeDataDTO;
import com.app.DTO.NodeDataDTO.Type;
import com.app.DTO.RefDTO;
import com.app.model.Clan;
import com.app.model.Clan.Stav;
import com.app.model.Clan.Stav.Tacka;
import com.app.model.Clan.Stav.Tacka.Podtacka;
import com.app.model.Clan.Stav.Tacka.Podtacka.Alineja;

public class MemberToNodeDataDTO implements ITransformer{
	
	
	@Override
	public Object transform(Object o) {
		NodeDataDTO nodeDTO=new NodeDataDTO();
		if(o instanceof Clan){
			nodeDTO=new NodeDataDTO();
			Clan member=(Clan)o;
			nodeDTO.setId(member.getId());
			nodeDTO.setName(member.getNaziv());
			nodeDTO.setParentId("");
			nodeDTO.setRef(new RefDTO());
			nodeDTO.setContent(new ArrayList<NodeDataDTO>());
			nodeDTO.setText(new ArrayList<Object>());
			for(Object content: member.getContent()){
//				if(!(content instanceof String)){
//					System.out.println("nije string");
//					nodeDTO.getContent().add((NodeDataDTO)new NodeToNodeDTO().transform(content));
//				}else{
//					nodeDTO.getText().add(content);
//				}
//				if(content instanceof Stav){
//					nodeDTO.getContent().add((NodeDataDTO)new NodeToNodeDTO().transform(content));
//				}else{
//					nodeDTO.getText().add(content);
//				}
//				try{
//					NodeDataDTO stav=addParagraphs(member,content);
//					if(stav!=null)
//						nodeDTO.getContent().add(stav);
//				}catch(Exception e){
//					nodeDTO.getText().add(content);
//				}
			}
			nodeDTO.setType(Type.member);
		}else if(o instanceof Stav){
			nodeDTO=new NodeDataDTO();
			Stav paragraph=(Stav)o;
			nodeDTO.setId(paragraph.getId());
			nodeDTO.setName("Stav-"+paragraph.getId());
			nodeDTO.setParentId("");
			nodeDTO.setRef(new RefDTO());
			nodeDTO.setContent(new ArrayList<NodeDataDTO>());
			nodeDTO.setText(new ArrayList<Object>());
//			for(Object content: paragraph.getContent()){
//				if(!(content instanceof String)){
//					nodeDTO.getContent().add((NodeDataDTO)new NodeToNodeDTO().transform(content));
//				}else{
//					nodeDTO.getText().add(content);
//				}
//			}
			nodeDTO.setType(Type.paragraph);
		}else if(o instanceof Tacka){
			nodeDTO=new NodeDataDTO();
			Tacka clause=(Tacka)o;
			nodeDTO.setId(clause.getId());
			nodeDTO.setName("Tacka-"+clause.getId());
			nodeDTO.setParentId("");
			nodeDTO.setRef(new RefDTO());
			nodeDTO.setContent(new ArrayList<NodeDataDTO>());
			nodeDTO.setText(new ArrayList<Object>());
//			for(Object content: clause.getContent()){
//				if(!(content instanceof String)){
//					nodeDTO.getContent().add((NodeDataDTO)new NodeToNodeDTO().transform(content));
//				}else{
//					nodeDTO.getText().add(content);
//				}
//			}
			nodeDTO.setType(Type.clause);
		}else if(o instanceof Podtacka){
			nodeDTO=new NodeDataDTO();
			Podtacka subclause=(Podtacka)o;
			nodeDTO.setId(subclause.getId());
			nodeDTO.setName("Podtacka-"+subclause.getId());
			nodeDTO.setParentId("");
			nodeDTO.setRef(new RefDTO());
			nodeDTO.setContent(new ArrayList<NodeDataDTO>());
			nodeDTO.setText(new ArrayList<Object>());
//			for(Object content: subclause.getContent()){
//				if(!(content instanceof String)){
//					nodeDTO.getContent().add((NodeDataDTO)new NodeToNodeDTO().transform(content));
//				}else{
//					nodeDTO.getText().add(content);
//				}
//			}
			nodeDTO.setType(Type.subclause);
		}else if(o instanceof Alineja){
			nodeDTO=new NodeDataDTO();
			Alineja indent=(Alineja)o;
			nodeDTO.setId(indent.getId());
			nodeDTO.setName("Alineja-"+indent.getId());
			nodeDTO.setParentId("");
			nodeDTO.setRef(new RefDTO());
			nodeDTO.setContent(new ArrayList<NodeDataDTO>());
			nodeDTO.setText(new ArrayList<Object>());
			for(Object content: indent.getContent()){
					nodeDTO.getText().add(content);
			}
			nodeDTO.setType(Type.indent);
		}
		return nodeDTO;
	}
//	
//	private NodeDataDTO addParagraphs(Clan clan,Object o){
//		NodeDataDTO nodeDTO=new NodeDataDTO();
//		Stav paragraph=(Stav)o;
//		nodeDTO.setId(paragraph.getId());
//		nodeDTO.setName("Stav-"+paragraph.getId());
//		nodeDTO.setParentId("");
//		fullPath+=paragraph.getId();
//		nodeDTO.setRef(new RefDTO());
//		nodeDTO.setContent(new ArrayList<NodeDataDTO>());
//		nodeDTO.setText(new ArrayList<Object>());
//		for(Object content: paragraph.getContent()){
//			try{
//				NodeDataDTO tacka=addClause(paragraph, content);
//				if(tacka!=null)
//					nodeDTO.getContent().add(tacka);
//			}catch(Exception e){
//				System.out.println("text");
//				nodeDTO.getText().add(content);
//			}
//		}
//		nodeDTO.setType(Type.paragraph);
//		return nodeDTO;
//	}
//	
//	private NodeDataDTO addClause(Stav stav, Object o){
//		NodeDataDTO nodeDTO=new NodeDataDTO();
//		Tacka clause=(Tacka)o;
//		nodeDTO.setId(clause.getId());
//		nodeDTO.setName("Tacka-"+clause.getId());
//		nodeDTO.setParentId("");
//		nodeDTO.setRef(new RefDTO());
//		nodeDTO.setContent(new ArrayList<NodeDataDTO>());
//		nodeDTO.setText(new ArrayList<Object>());
//		for(Object content: clause.getContent()){
//			if(!(content instanceof String)){
//				nodeDTO.getContent().add((NodeDataDTO)new NodeToNodeDTO().transform(content));
//			}else{
//				nodeDTO.getText().add(content);
//			}
//		}
//		nodeDTO.setType(Type.clause);
//		
//		return nodeDTO;
//	}


}
