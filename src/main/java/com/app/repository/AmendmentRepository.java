package com.app.repository;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Repository;

import com.app.config.Config;
import com.app.model.Act;
import com.app.model.Act.Deo;
import com.app.model.Amendment;
import com.app.model.Amendment.ListaPredloga.PredlogAmandmana;
import com.app.model.Clan;
import com.app.model.Clan.Stav;
import com.app.model.Clan.Stav.Tacka;
import com.app.model.Clan.Stav.Tacka.Podtacka;
import com.app.model.Clan.Stav.Tacka.Podtacka.Alineja;
import com.app.model.Cvor;
import com.app.model.Glava;
import com.app.model.Glava.Odeljak;
import com.app.model.Glava.Odeljak.Pododeljak;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

@Repository
public class AmendmentRepository {

private DatabaseClient client;
	
	public boolean createNewAmendment(Amendment amendment){
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
		
		boolean exists=amendmentExists(docManager, amendment.getId());
		if(exists){
			client.release();
			return false;
		}

		boolean created=writeAmendment(amendment);
		
		client.release();
		return created;
	}
	
	public boolean writeAmendment(Amendment amendment){
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();

		JAXBContext context=null;
		try{
			context=JAXBContext.newInstance(Amendment.class);
		}catch(JAXBException e){
			e.printStackTrace();
			client.release();
			return false;
		}
		JAXBHandle<Amendment> handle=new JAXBHandle<Amendment>(context);

		DocumentMetadataHandle metadata=new DocumentMetadataHandle();
		metadata.getCollections().add(amendment.getRefAkta());
		metadata.getCollections().add(amendment.getPredlagac());
		handle.set(amendment);
		docManager.write("/amendment/" + amendment.getId(),metadata, handle);

		client.release();
		return true;
	}
	
	public List<Amendment> getAllAmendmentsByActOrUser(String actOrUser){
		List<Amendment> amendments=new ArrayList<Amendment>();

		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
		
		List<String> amendmentIDs=getAllAmendmentsIDs();
		StringHandle content=new StringHandle();
		DocumentMetadataHandle metadata =new DocumentMetadataHandle();
		for(String id:amendmentIDs){
			content = new StringHandle();
			metadata = new DocumentMetadataHandle();
			docManager.read("/amendment/"+id, metadata, content);
			if(metadata.getCollections().contains(actOrUser)){
				Amendment am=getAmendmentById(id);
				amendments.add(am);
			}
		}

		client.release();
		return amendments;
	}
	
	public Amendment getAmendmentById(String id){
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
		
		if(!amendmentExists(docManager, id)){
			client.release();
			return null;
		}
		
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Amendment.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Amendment> handle = new JAXBHandle<Amendment>(context);
		docManager.read("/amendment/"+id, handle);
		Amendment amendment= handle.get();
		
		client.release();
		return amendment;
	}
	
	public List<String> getAllAmendmentsIDs(){
		List<String> amendmentIDs=new ArrayList<>();
		
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		query.setDirectory("/amendment/");
		query.setCriteria("");
		SearchHandle resultsHandle = new SearchHandle();
		queryMgr.search(query, resultsHandle);
		
		for (MatchDocumentSummary result : resultsHandle.getMatchResults()) {
			String uri = result.getUri();
			uri=uri.substring(1, uri.length());
			int index=uri.indexOf("/");
			uri=uri.substring(index+1,uri.length());
			amendmentIDs.add(uri);
		}

		client.release();
		return amendmentIDs;
	}

	public boolean deleteAmendment(String id){
		client = DatabaseClientFactory.newClient(Config.host, Config.port,Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
		boolean exists=amendmentExists(docManager,id);
		if(!exists){
			client.release();
			return false;
		}

		docManager.delete("/amendment/" + id);
		
		client.release();
		return true;
		
	}
	
	public List<Amendment> getAllAmendmentsByActWithOperation(String act,String operation){
		List<Amendment> amendmentsByAct=getAllAmendmentsByActOrUser(act);
		List<Amendment> amendmentsWithOperation=new ArrayList<>();
		for(Amendment am:amendmentsByAct){
			if(am.getTip().equals(operation))
				amendmentsWithOperation.add(am);
		}
		return amendmentsWithOperation;
	}
	
	public Act changeAct(Act act,Amendment am){
		String operation=am.getTip();
		for(PredlogAmandmana predlog:am.getListaPredloga().getPredlogAmandmana()){
			return doOperationOnAct(predlog,act,operation);
		}	
		return act;
	}
	
	private Act doOperationOnAct(PredlogAmandmana predlog,Act act,String operation){
		if(!predlog.getRef().getDeo().equals("")){
			Deo deo=(Deo)getElement(act.getDeo().toArray(), predlog.getRef().getDeo(),"Deo");
			if(!predlog.getRef().getGlava().equals("")){
				if(deo!=null){
					Glava glava=(Glava)getElement(deo.getGlava().toArray(), predlog.getRef().getGlava(),"Glava");
					if(!predlog.getRef().getOdeljak().equals("")){
						if(glava!=null){
							Odeljak odeljak=(Odeljak)getElement(glava.getOdeljak().toArray(), predlog.getRef().getOdeljak(),"Odeljak");
							if(!predlog.getRef().getPododeljak().equals("")){
								if(odeljak!=null){
									Pododeljak pododeljak=(Pododeljak)getElement(odeljak.getPododeljak().toArray(), predlog.getRef().getPododeljak(),"Pododeljak");
									if(pododeljak!=null){
										if(!predlog.getRef().getClan().equals("")){
											Clan clan=(Clan)getElement(pododeljak.getClan().toArray(), predlog.getRef().getClan(),"Clan");
											if(clan!=null){
												if(!predlog.getRef().getStav().equals("")){
													Clan noviClan=doOperationOnElement(clan,predlog,operation);
													//izmeni clan
													pododeljak.getClan().remove(clan);
													pododeljak.getClan().add(noviClan);
													return act;
													
												}else{
													if(operation.equals("Brisanje")){
														//obrisi clan
														pododeljak.getClan().remove(clan);
														return act;
													}else if(operation.equals("Dodavanje")){
														//dodaj stav
														Stav noviStav=transformIntoParagraph(predlog.getCvor());
														clan.getContent().add(noviStav);
														return act;
													}else if(operation.equals("Izmena")){
														//izmeni clan
														boolean added=false;
														for(Object o:clan.getContent()){
															if(o instanceof String){
																clan.getContent().remove(o);
																clan.getContent().addAll(predlog.getSadrzaj().getContent());
																added=true;
																break;
															}
														}
														if(!added){
															clan.getContent().addAll(predlog.getSadrzaj().getContent());
														}
//														clan.getContent().clear();
//														clan.getContent().addAll(predlog.getSadrzaj().getContent());
														return act;
													}
												}
											}
										}else{
											if(operation.equals("Dodavanje")){
												//dodaj clan
												Clan noviClan=transformIntoMember(predlog.getCvor());
												pododeljak.getClan().add(noviClan);
												return act;
											}
										}
									}
								}
							}else if(!predlog.getRef().getClan().equals("")){
								if(odeljak!=null){
									Clan clan=(Clan)getElement(odeljak.getClan().toArray(), predlog.getRef().getClan(),"Clan");
									if(clan!=null){
										if(!predlog.getRef().getStav().equals("")){
											Clan noviClan=doOperationOnElement(clan,predlog,operation);
											//izmeni clan
											odeljak.getClan().remove(clan);
											odeljak.getClan().add(noviClan);
											return act;
											
										}else{
											if(operation.equals("Brisanje")){
												//obrisi clan
												odeljak.getClan().remove(clan);
												return act;
											}else if(operation.equals("Dodavanje")){
												//dodaj stav
												Stav noviStav=transformIntoParagraph(predlog.getCvor());
												clan.getContent().add(noviStav);
												return act;
											}else if(operation.equals("Izmena")){
												//izmeni clan
												boolean added=false;
												for(Object o:clan.getContent()){
													if(o instanceof String){
														clan.getContent().remove(o);
														clan.getContent().addAll(predlog.getSadrzaj().getContent());
														added=true;
														break;
													}
												}
												if(!added){
													clan.getContent().addAll(predlog.getSadrzaj().getContent());
												}
//												clan.getContent().clear();
//												clan.getContent().addAll(predlog.getSadrzaj().getContent());
												return act;
											
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private Clan doOperationOnElement(Clan clan,PredlogAmandmana predlog,String operation){
		Stav stav=(Stav)getElement(clan.getContent().toArray(), predlog.getRef().getStav(),"Stav");
		if(stav!=null){
			if(!predlog.getRef().getTacka().equals("")){
				Tacka tacka=(Tacka)getElement(stav.getContent().toArray(), predlog.getRef().getTacka(),"Tacka");
				if(tacka!=null){
					if(!predlog.getRef().getPodtacka().equals("")){
						Podtacka podtacka=(Podtacka)getElement(tacka.getContent().toArray(), predlog.getRef().getPodtacka(),"Podtacka");
						if(podtacka!=null){
							if(!predlog.getRef().getAlineja().equals("")){
								Alineja alineja=(Alineja)getElement(podtacka.getContent().toArray(), predlog.getRef().getAlineja(), "Alineja");
								if(operation.equals("Brisanje")){
									//obrisi alineju
									podtacka.getContent().remove(alineja);
									return clan;
								}else if(operation.equals("Izmena")){
									//izmena
									alineja.getContent().clear();
									alineja.getContent().addAll(predlog.getSadrzaj().getContent());
									return clan;
								}
							}else{
								if(operation.equals("Brisanje")){
									//obrisi podtacku
									tacka.getContent().remove(podtacka);
									return clan;
								}else if(operation.equals("Dodavanje")){
									//dodaj alineju
									Alineja novaAlineja=transformIntoIndent(predlog.getCvor());
									podtacka.getContent().add(novaAlineja);
									return clan;
								}else if(operation.equals("Izmena")){
									//izmeni podtacku
									boolean added=false;
									for(Object o:podtacka.getContent()){
										if(o instanceof String){
											podtacka.getContent().remove(o);
											podtacka.getContent().addAll(predlog.getSadrzaj().getContent());
											added=true;
											break;
										}
									}
									if(!added){
										podtacka.getContent().addAll(predlog.getSadrzaj().getContent());
									}
//									podtacka.getContent().clear();
//									podtacka.getContent().addAll(predlog.getSadrzaj().getContent());
									return clan;
								}
							}
						}
					}else{
						if(operation.equals("Brisanje")){
							//obrisi tacku
							stav.getContent().remove(tacka);
							return clan;
						}else if(operation.equals("Dodavanje")){
							//dodaj podtacku
							Podtacka novaPodtavka=transformIntoSubclause(predlog.getCvor());
							tacka.getContent().add(novaPodtavka);
							return clan;
						}else if(operation.equals("Izmena")){
							//izmeni tacku
							boolean added=false;
							for(Object o:tacka.getContent()){
								if(o instanceof String){
									tacka.getContent().remove(o);
									tacka.getContent().addAll(predlog.getSadrzaj().getContent());
									added=true;
									break;
								}
							}
							if(!added){
								tacka.getContent().addAll(predlog.getSadrzaj().getContent());
							}
//							tacka.getContent().clear();
//							tacka.getContent().addAll(predlog.getSadrzaj().getContent());
							return clan;
						}
					}
				}
			}else{
				if(operation.equals("Brisanje")){
					//obrisi stav
					clan.getContent().remove(stav);
					return clan;
				}else if(operation.equals("Dodavanje")){
					//dodaj tacku
					Tacka tacka=transformIntoClause(predlog.getCvor());
					stav.getContent().add(tacka);
					return clan;
				}else if(operation.equals("Izmena")){
					//izmeni stav
					boolean added=false;
					for(Object o:stav.getContent()){
						if(o instanceof String){
							stav.getContent().remove(o);
							stav.getContent().addAll(predlog.getSadrzaj().getContent());
							added=true;
							break;
						}
					}
					if(!added){
						stav.getContent().addAll(predlog.getSadrzaj().getContent());
					}
//					stav.getContent().clear();
//					stav.getContent().addAll(predlog.getSadrzaj().getContent());
					return clan;
				}
			}
		}
		return null;
	}
	
	public Clan transformIntoMember(Cvor cvor){
		Clan clan=new Clan();
		clan.setId(cvor.getId());
		clan.setNaziv(cvor.getNaziv());
		for(Object child:cvor.getContent()){
			if(child instanceof Cvor){
				Stav stav=transformIntoParagraph((Cvor)child);
				clan.getContent().add(stav);
			}else{
				clan.getContent().add(child);
			}
		}
		return clan;
	}
	
	public Stav transformIntoParagraph(Cvor cvor){
		Stav stav=new Stav();
		stav.setId(cvor.getId());
		for(Object child:cvor.getContent()){
			if(child instanceof Cvor){
				Tacka tacka=transformIntoClause((Cvor)child);
				stav.getContent().add(tacka);
			}else
				stav.getContent().add(child);
		}
		return stav;
	}
	
	public Tacka transformIntoClause(Cvor cvor){
		Tacka tacka=new Tacka();
		tacka.setId(cvor.getId());
		for(Object child:cvor.getContent()){
			if(child instanceof Cvor){
				Podtacka podtacka=transformIntoSubclause((Cvor)child);
				tacka.getContent().add(podtacka);
			}else{
				tacka.getContent().add(child);
			}
		}
		return tacka;
	}
	
	public Podtacka transformIntoSubclause(Cvor cvor){
		Podtacka podtacka=new Podtacka();
		podtacka.setId(cvor.getId());
		for(Object child:cvor.getContent()){
			if(child instanceof Cvor){
				Alineja alineja=transformIntoIndent((Cvor)child);
				podtacka.getContent().add(alineja);
					
			}else{
				podtacka.getContent().add(child);
			}
		}
		return podtacka;
	}

	public Alineja transformIntoIndent(Cvor cvor){
		Alineja alineja=new Alineja();
		alineja.setId(cvor.getId());
		for(Object child:cvor.getContent()){
			alineja.getContent().add(child);
		}
		return alineja;
	}
	
	public Object getElement(Object[] elements, String elementId,String typeOfElement){
		for(Object o:elements){
			if(typeOfElement.equals("Deo") && !(o instanceof String)){
				if(((Deo)o).getId().equals(elementId)){
					return o;
				}
			}else if(typeOfElement.equals("Glava") && !(o instanceof String)){
				if(((Glava)o).getId().equals(elementId)){
					return o;
				}
			}else if(typeOfElement.equals("Odeljak") && !(o instanceof String)){
				if(((Odeljak)o).getId().equals(elementId)){
					return o;
				}
			}else if(typeOfElement.equals("Pododeljak") && !(o instanceof String)){
				if(((Pododeljak)o).getId().equals(elementId)){
					return o;
				}
			}else if(typeOfElement.equals("Clan") && !(o instanceof String)){
				if(((Clan)o).getId().equals(elementId)){
					return o;
				}
			}else if(typeOfElement.equals("Stav") && !(o instanceof String)){
				if(((Stav)o).getId().equals(elementId)){
					return o;
				}
			}else if(typeOfElement.equals("Tacka") && !(o instanceof String)){
				if(((Tacka)o).getId().equals(elementId)){
					return o;
				}
			}else if(typeOfElement.equals("Podtacka") && !(o instanceof String)){
				if(((Podtacka)o).getId().equals(elementId)){
					return o;
				}
			}else if(typeOfElement.equals("Alineja") && !(o instanceof String)){
				if(((Alineja)o).getId().equals(elementId)){
					return o;
				}
			}
		}
		return null;
	}
	
	public boolean changeAmendmentStatus(String id){
		String status="Prihvacen";
		client = DatabaseClientFactory.newClient(Config.host, Config.port,Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
		
		boolean exists=amendmentExists(docManager,id);
		if(!exists){
			client.release();
			return false;
		}
		JAXBContext context=null;
		try{
			context=JAXBContext.newInstance(Amendment.class);
		}catch(JAXBException e){
			e.printStackTrace();
			client.release();
			return true;
		}
		
		JAXBHandle<Amendment> handle=new JAXBHandle<>(context);
		docManager.read("/amendment/" + id, handle);
		Amendment am=handle.get();
		am.setStatus(status);
		
		boolean updated=writeAmendment(am);

		client.release();
		return updated;
	}
		
	public boolean deleteAllAmendmentsByAct(Amendment am){
		List<Amendment> amendmentsWithAct=getAllAmendmentsByActOrUser(am.getRefAkta());
		List<Amendment> amendmentsToDelete=new ArrayList<>();
		for(PredlogAmandmana predlog:am.getListaPredloga().getPredlogAmandmana()){
			for(Amendment amWithAct:amendmentsWithAct){
				if(!predlog.getRef().getDeo().equals("")){
					for(PredlogAmandmana p:amWithAct.getListaPredloga().getPredlogAmandmana()){
						if(predlog.getRef().getDeo().equals(p.getRef().getDeo())){
							if(!predlog.getRef().getGlava().equals("")){
								if(predlog.getRef().getGlava().equals(p.getRef().getGlava())){
									if(!predlog.getRef().getOdeljak().equals("")){
										if(predlog.getRef().getOdeljak().equals(p.getRef().getOdeljak())){
											if(!predlog.getRef().getPododeljak().equals("")){
												if(predlog.getRef().getPododeljak().equals(p.getRef().getPododeljak())){
													if(!predlog.getRef().getClan().equals("")){
														if(predlog.getRef().getClan().equals(p.getRef().getClan())){
															if(!predlog.getRef().getStav().equals("")){
																if(predlog.getRef().getStav().equals(p.getRef().getStav())){
																	if(!predlog.getRef().getTacka().equals("")){
																		if(predlog.getRef().getTacka().equals(p.getRef().getTacka())){
																			if(!predlog.getRef().getPodtacka().equals("")){
																				if(predlog.getRef().getPodtacka().equals(p.getRef().getPodtacka())){
																					if(!predlog.getRef().getAlineja().equals("")){
																						if(predlog.getRef().getAlineja().equals(p.getRef().getAlineja())){
																							amendmentsToDelete.add(amWithAct);
																						}
																					}else{
																						amendmentsToDelete.add(amWithAct);
																					}
																				}
																			}else{
																				amendmentsToDelete.add(amWithAct);
																			}
																		}
																	}else{
																		amendmentsToDelete.add(amWithAct);
																	}
																}
															}else{
																amendmentsToDelete.add(amWithAct);
															}
														}
													}else{
														amendmentsToDelete.add(amWithAct);
													}
												}
											}else if(!predlog.getRef().getClan().equals("")){
												if(predlog.getRef().getClan().equals(p.getRef().getClan())){
													if(!predlog.getRef().getStav().equals("")){
														if(predlog.getRef().getStav().equals(p.getRef().getStav())){
															if(!predlog.getRef().getTacka().equals("")){
																if(predlog.getRef().getTacka().equals(p.getRef().getTacka())){
																	if(!predlog.getRef().getPodtacka().equals("")){
																		if(predlog.getRef().getPodtacka().equals(p.getRef().getPodtacka())){
																			if(!predlog.getRef().getAlineja().equals("")){
																				if(predlog.getRef().getAlineja().equals(p.getRef().getAlineja())){
																					amendmentsToDelete.add(amWithAct);
																				}
																			}else{
																				amendmentsToDelete.add(amWithAct);
																			}
																		}
																	}else{
																		amendmentsToDelete.add(amWithAct);
																	}
																}
															}else{
																amendmentsToDelete.add(amWithAct);
															}
														}
													}else{
														amendmentsToDelete.add(amWithAct);
													}
												}
											}else{
												amendmentsToDelete.add(amWithAct);
											}
										}
									}else{
										amendmentsToDelete.add(amWithAct);
									}
								}
							}else{
								amendmentsToDelete.add(amWithAct);
							}
						}
					}
				}
			}
		}
		
		for(Amendment a:amendmentsToDelete){
			deleteAmendment(a.getId());
		}
		
		return true;
	}
	
	private boolean amendmentExists(XMLDocumentManager docManager, String id){
		JAXBContext context=null;
		try{
			context=JAXBContext.newInstance(Amendment.class);
		}catch(JAXBException e){
			e.printStackTrace();
			return false;
		}
		JAXBHandle<Amendment> handle=new JAXBHandle<>(context);
		try{
			docManager.read("/amendment/"+id,handle);
		}catch(ResourceNotFoundException e){
			return false;
		}
		return true;
	}

}
