package com.app.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.DTO.NodeDataDTO;
import com.app.DTO.RefDTO;
import com.app.model.Act;
import com.app.model.Act.Deo;
import com.app.model.Amendment;
import com.app.model.Clan;
import com.app.model.Clan.Stav;
import com.app.model.Clan.Stav.Tacka;
import com.app.model.Clan.Stav.Tacka.Podtacka;
import com.app.model.Clan.Stav.Tacka.Podtacka.Alineja;
import com.app.model.Glava;
import com.app.model.Glava.Odeljak;
import com.app.model.Glava.Odeljak.Pododeljak;
import com.app.model.User;
import com.app.repository.ActRepository;
import com.app.repository.AmendmentRepository;
import com.app.repository.UserRepository;
import com.app.repository.XHTML_PDF_Repository;
import com.app.transformers.MemberToNodeDataDTO;

@Component
public class AmendmentServiceImpl implements IAmendmentService{

	@Autowired
	private AmendmentRepository amRepo;
	@Autowired
	private XHTML_PDF_Repository xhtml_pdf;
	@Autowired
	private ActRepository actRepo;
	@Autowired
	public UserRepository userRepo;
	
	@Override
	public boolean createNewAmendment(Amendment am) {
		return amRepo.createNewAmendment(am);
	}

	@Override
	public boolean deleteAmendment(String id) {
		return amRepo.deleteAmendment(id);
	}

	@Override
	public List<Amendment> getAllAmendmentsByUser(String user) {
		return amRepo.getAllAmendmentsByActOrUser(user);
	}

	@Override
	public List<Amendment> getAllAmendmetnsByActWithOperation(String act, String operation) {
		return amRepo.getAllAmendmentsByActWithOperation(act, operation);
	}

	@Override
	public boolean acceptAmendment(String id) {
		Amendment am=amRepo.getAmendmentById(id);
		if(am!=null){
			String actName=am.getRefAkta();
			Act act=actRepo.getActByName(actName);
			if(act!=null){
				Act newAct=amRepo.changeAct(act,am);
				if(actRepo.updateAct(newAct)){
					if(am.getTip().equals("Brisanje")){
						amRepo.deleteAllAmendmentsByAct(am);
					}
					return amRepo.changeAmendmentStatus(id);
				}
			}
		}
		return false;
	}

	@Override
	public boolean generateXHTML(String id) {
		Amendment am=amRepo.getAmendmentById(id);
		String xmlPath="src/main/resources/data/amendment.xml";
		String xslPath="src/main/resources/data/htmlAmendment.xsl";
		if(am!=null){
			if(xhtml_pdf.generateXml(am, xmlPath)){
				return xhtml_pdf.generateXHTML(xslPath,xmlPath);
			}else
				return false;
		}else{
			return false;
		}
		
	}

	
	@Override
	public boolean generatePDF(String id) {
		Amendment am=amRepo.getAmendmentById(id);
		String xmlPath="src/main/resources/data/amendment.xml";
		String xslPath="src/main/resources/data/pdfAmendment.xsl";
		if(am!=null){
			if(xhtml_pdf.generateXml(am, xmlPath)){
				return xhtml_pdf.generatePDF(xslPath,xmlPath);
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	
	@Override
	public List<NodeDataDTO> getNodeMembersByAct(String actName) {
		List<NodeDataDTO> members=new ArrayList<NodeDataDTO>();
		Act act=actRepo.getActByName(actName);
		for(Deo deo:act.getDeo()){
			for(Glava glava:deo.getGlava()){
				for(Odeljak odeljak:glava.getOdeljak()){
					if(odeljak.getPododeljak().size()>0){
						for(Pododeljak pododeljak:odeljak.getPododeljak()){
							for(Clan clan:pododeljak.getClan()){
								NodeDataDTO node=(NodeDataDTO)new MemberToNodeDataDTO().transform(clan);
								node.setParentId(pododeljak.getId());
								RefDTO ref=new RefDTO(act.getNaziv(), deo.getId(), glava.getId(), odeljak.getId(), pododeljak.getId(), clan.getId(), "", "", "", "", "");
								node.setRef(ref);
								String fullPath=act.getNaziv()+clan.getNaziv();
//								String fullPath="Treci_test_za_nevenuClan1";
								int counter=1;
								for(Object o: clan.getContent()){
									try{
										NodeDataDTO stav=getParagraph(o, fullPath,counter,clan,ref);
										if(stav!=null){
											node.getContent().add(stav);
										}
									}catch(Exception e){
										node.getText().add(o);
									}
									counter++;
								}
								members.add(node);
							}
						}
					}else if(odeljak.getClan().size()>0){
						for(Clan clan:odeljak.getClan()){
							NodeDataDTO node=(NodeDataDTO)new MemberToNodeDataDTO().transform(clan);
							node.setParentId(odeljak.getId());
							RefDTO ref=new RefDTO(act.getNaziv(), deo.getId(), glava.getId(), odeljak.getId(), "", clan.getId(), "", "", "", "", "");
							node.setRef(ref);
							members.add(node);
						}
					}
				}
			}
		}
		return members;
	}
	
	private NodeDataDTO getParagraph(Object content, String fullPath,int index,Clan clan,RefDTO refDTO){
		fullPath+=index+"";
		Stav stav=actRepo.getParagraphByUri(fullPath);
		NodeDataDTO node=new NodeDataDTO();
		if(stav!=null){
			node=(NodeDataDTO)new MemberToNodeDataDTO().transform(stav);
			node.setParentId(clan.getId());
			RefDTO ref=refDTO;
			ref.setParagraph(stav.getId());
			node.setRef(ref);
			int counter=1;
			for(Object o: clan.getContent()){
				try{
					NodeDataDTO tacka=getClause(o, fullPath, counter, stav, ref);
					if(tacka!=null){
						node.getContent().add(tacka);
					}
				}catch(Exception e){
					node.getText().add(o);
				}
				counter++;
			}
		}
		return node;
	}
	
	private NodeDataDTO getClause(Object content, String fullPath,int index,Stav stav,RefDTO refDTO){
		fullPath+=index+"";
		Tacka tacka=actRepo.getClauseByUri(fullPath);
		NodeDataDTO node=new NodeDataDTO();
		if(tacka!=null){
			node=(NodeDataDTO)new MemberToNodeDataDTO().transform(tacka);
			node.setParentId(stav.getId());
			RefDTO ref=refDTO;
			ref.setClause(tacka.getId());
			node.setRef(ref);
			int counter=1;
			for(Object o: stav.getContent()){
				try{
					NodeDataDTO podtacka=getSubclause(o, fullPath, counter, tacka, ref);
					if(podtacka!=null){
						node.getContent().add(podtacka);
					}
				}catch(Exception e){
					node.getText().add(o);
				}
			}
		}
		return node;
	}
	
	private NodeDataDTO getSubclause(Object content, String fullPath,int index,Tacka tacka,RefDTO refDTO){
		fullPath+=index+"";
		Podtacka podtacka=actRepo.getSubclauseByUri(fullPath);
		NodeDataDTO node=new NodeDataDTO();
		if(podtacka!=null){
			node=(NodeDataDTO)new MemberToNodeDataDTO().transform(podtacka);
			node.setParentId(tacka.getId());
			RefDTO ref=refDTO;
			ref.setSubclause(podtacka.getId());
			node.setRef(ref);
			int counter=1;
			for(Object o: tacka.getContent()){
				try{
					NodeDataDTO alineja=getIndent(o, fullPath, counter, podtacka, ref);
					if(alineja!=null){
						node.getContent().add(alineja);
					}
				}catch(Exception e){
					node.getText().add(o);
				}
			}
		}
		return node;
	}
	
	private NodeDataDTO getIndent(Object content, String fullPath,int index,Podtacka podtacka,RefDTO refDTO){
		fullPath+=index+"";
		Alineja alineja=actRepo.getAlinejaByUri(fullPath);
		NodeDataDTO node=new NodeDataDTO();
		if(alineja!=null){
			node=(NodeDataDTO)new MemberToNodeDataDTO().transform(alineja);
			node.setParentId(podtacka.getId());
			RefDTO ref=refDTO;
			ref.setIndent(alineja.getId());
			node.setRef(ref);
			for(Object o: podtacka.getContent()){
					node.getText().add(o);
			}
		}
		return node;
	}
	
	@Override
	public NodeDataDTO getMemberByActAndId(String actName,String memberID) {
		List<Act> allActs=actRepo.getAllActs();
		for(Act act:allActs){
			if(act.getNaziv().equals(actName)){
				for(Deo deo:act.getDeo())
					for(Glava glava:deo.getGlava())
						for(Odeljak odeljak:glava.getOdeljak()){
							if(odeljak.getPododeljak().size()>0){
								for(Pododeljak pododeljak:odeljak.getPododeljak())
									for(Clan clan:pododeljak.getClan())
										if(clan.getId().equals(memberID)){
											NodeDataDTO node=(NodeDataDTO)new MemberToNodeDataDTO().transform(clan);
											node.setParentId(pododeljak.getId());
											RefDTO ref=new RefDTO(act.getNaziv(), deo.getId(), glava.getId(), odeljak.getId(), pododeljak.getId(), clan.getId(), "", "", "", "", "");
											node.setRef(ref);
//											String fullPath=act.getNaziv()+clan.getNaziv();
											String fullPath="Treci_test_za_nevenuClan1";
											int counter=1;
											for(Object o: clan.getContent()){
												try{
													NodeDataDTO stav=getParagraph(o, fullPath,counter,clan,ref);
													if(stav!=null){
														node.getContent().add(stav);
													}
												}catch(Exception e){
													node.getText().add(o);
												}
												counter++;
											}
											return node;
										}
							}else if(odeljak.getClan().size()>0){
								for(Clan clan:odeljak.getClan())
									if(clan.getId().equals(memberID)){
										NodeDataDTO node=(NodeDataDTO)new MemberToNodeDataDTO().transform(clan);
										node.setParentId(odeljak.getId());
										RefDTO ref=new RefDTO(act.getId(), deo.getId(), glava.getId(), odeljak.getId(), "", clan.getId(), "", "", "", "", "");
										node.setRef(ref);
										return node;
									}
							}
						}
			}
		}
		return null;
	}

	
	@Override
	public int getAmendmentID() {
		return (amRepo.getAllAmendmentsIDs().size()+1);
	}

	private Clan getMember(String memberId,String actName) {
		Act act=actRepo.getActByName(actName);
			for(Deo deo:act.getDeo()){
				for(Glava glava:deo.getGlava()){
					for(Odeljak odeljak:glava.getOdeljak()){
						if(odeljak.getPododeljak().size()>0){
							for(Pododeljak pododeljak:odeljak.getPododeljak())
								for(Clan clan:pododeljak.getClan()){
									if(clan.getId().equals(memberId)){
										for(Object o: clan.getContent()){
											System.out.println(o.getClass());
											System.out.println(o);
										}
										return clan;
									}
								}
						}else if(odeljak.getClan().size()>0){
							for(Clan clan:odeljak.getClan()){
								if(clan.getId().equals(memberId)){
									return clan;
								}
							}
						}
					}
				}
			}
		
		return null;
	}
	
	private List<Clan> getMembersByAct(String actName){
		List<Clan> members=new ArrayList<Clan>();
		Act act=actRepo.getActByName(actName);
		for(Deo deo:act.getDeo()){
			for(Glava glava:deo.getGlava()){
				for(Odeljak odeljak:glava.getOdeljak()){
					if(odeljak.getPododeljak().size()>0){
						for(Pododeljak pododeljak:odeljak.getPododeljak()){
							for(Clan clan:pododeljak.getClan()){
								members.add(clan);
							}
						}
					}else if(odeljak.getClan().size()>0){
						for(Clan clan:odeljak.getClan()){
							members.add(clan);
						}
					}
				}
			}
		}
		return members;
	}

	@Override
	public Map<String, Object> getReferenceElement(Map<String, String> path) {
		Map<String,Object> retVal=new HashMap<>();
		Clan clan=null;
		if(path.keySet().size()>1){
			clan=getMember(path.get("member"),path.get("act"));
			if(clan==null)
				return retVal;
		}
		ArrayList<String> result = new ArrayList<String>();
		if(path.containsKey("subclause")){
			Stav stav=(Stav)amRepo.getElement(clan.getContent().toArray(), path.get("paragraph"), "Stav");
			Tacka tacka=(Tacka)amRepo.getElement(stav.getContent().toArray(), path.get("clause"), "Tacka");
			Podtacka podtacka=(Podtacka)amRepo.getElement(tacka.getContent().toArray(), path.get("subclause"), "Podtacka");
			for(Object o:podtacka.getContent()){
				if(o instanceof Alineja){
					result.add(((Alineja)o).getId());
				}
			}
			retVal.put("type","indent");
		}else if(path.containsKey("clause")){
			Stav stav=(Stav)amRepo.getElement(clan.getContent().toArray(), path.get("paragraph"), "Stav");
			Tacka tacka=(Tacka)amRepo.getElement(stav.getContent().toArray(), path.get("clause"), "Tacka");
			for(Object o:tacka.getContent()){
				if(o instanceof Podtacka){
					result.add(((Podtacka)o).getId());
				}
			}
			retVal.put("type","subclause");
		}else if(path.containsKey("paragraph")){
			Stav stav=(Stav)amRepo.getElement(clan.getContent().toArray(), path.get("paragraph"), "Stav");
			for(Object o:stav.getContent()){
				if(o instanceof Tacka){
					result.add(((Tacka)o).getId());
				}
			}
			retVal.put("type","clause");
		}else if(path.containsKey("member")){
			for(Object o:clan.getContent()){
				if(o instanceof Stav){
					result.add(((Stav)o).getId());
				}
			}
			retVal.put("type","paragraph");
		}else{
			for(Clan cl:getMembersByAct(path.get("act"))){
				result.add(((Clan)cl).getId());
			}
			retVal.put("type","member");
		}


		retVal.put("data", result);
		return retVal;
	}

	@Override
	public String getUserInfoByEmail(String email) {
		User user=userRepo.getByEmail(email);
		String userInfo=user.getName()+" "+user.getSurname();
		return userInfo;
	}


	
}
