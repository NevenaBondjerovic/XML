package com.app.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.DTO.ActMetaDataDTO;
import com.app.DTO.ActMetaDataDTO.AktStatus;
import com.app.DTO.ActMetaDataFilterDTO;
import com.app.config.Config;
import com.app.model.Act;
import com.app.model.Act.PodaciOGlasanju;
import com.app.model.Act.PodaciORazresenjuStatusa;
import com.app.model.Amendment;
import com.app.model.Clan;
import com.app.model.Clan.Stav;
import com.app.model.Clan.Stav.Tacka;
import com.app.model.Clan.Stav.Tacka.Podtacka;
import com.app.model.Clan.Stav.Tacka.Podtacka.Alineja;
import com.app.model.Glava;
import com.app.model.Ref;
import com.app.model.User;
import com.app.services.IUserService;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.Transaction;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

@Repository
public class ActRepository {

	@Autowired
	private IUserService userService;

//	private DatabaseClient client;

	public ArrayList<String> allActNames(){
		ArrayList<String> names = new ArrayList<String>();
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		query.setDirectory("/aktovi/precisceni/");
		query.setCriteria("");

		SearchHandle resultsHandle = new SearchHandle();
		queryMgr.search(query, resultsHandle);

		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		for (MatchDocumentSummary result : results) {
			result.getUri();
			String name = result.getUri().substring(result.getUri().lastIndexOf("/") + 1);
			names.add(name);
		}
		return names;
	}
	
	public boolean writeAct(Act act) {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		boolean exists = alreadyExists(docMgr, act);
		if (exists) {
			client.release();
			return false;
		}
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Act.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Act> writeHandle = new JAXBHandle<Act>(context);
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(act.getPredlagac());
		writeHandle.set(act);
		docMgr.write("/aktovi/" + act.getNaziv(), metadata, writeHandle);
		docMgr.write("/aktovi/precisceni/" + act.getNaziv(), metadata,
				writeHandle);

		client.release();
		return true;
	}

	public boolean withdrawAct(String actName) {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		Transaction transaction = client.openTransaction();
		try {
			docMgr.delete("/aktovi/" + actName, transaction);
			docMgr.delete("/aktovi/precisceni/" + actName, transaction);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			client.release();
			return false;
		}
		client.release();
		return true;
	}

	public ArrayList<HashMap<String, String>> getMetadataByUser(String email) {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		query.setDirectory("/aktovi/precisceni");
		query.setCollections(email);
		query.setCriteria("");
		SearchHandle resultsHandle = new SearchHandle();
		queryMgr.search(query, resultsHandle);

		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		ArrayList<HashMap<String, String>> allMetadata = new ArrayList<HashMap<String, String>>();
		for (MatchDocumentSummary result : results) {
			HashMap<String, String> actMetadata = new HashMap<String, String>();
			String uri = result.getUri();
			if (uri.contains("/precisceni/")) {
				fillMetadataMap(actMetadata, uri, docMgr, client);
				allMetadata.add(actMetadata);
			}
		}
		client.release();
		return allMetadata;
	}

	public ArrayList<HashMap<String, String>> getActsmetadataFilteredByContent(
			String keyword) {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		query.setDirectory("/aktovi/precisceni/");
		query.setCriteria(keyword);

		SearchHandle resultsHandle = new SearchHandle();
		queryMgr.search(query, resultsHandle);

		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		ArrayList<HashMap<String, String>> allMetadata = new ArrayList<HashMap<String, String>>();
		for (MatchDocumentSummary result : results) {
			HashMap<String, String> actMetadata = new HashMap<String, String>();
			String uri = result.getUri();
			fillMetadataMap(actMetadata, uri, docMgr, client);
			allMetadata.add(actMetadata);
		}
		client.release();
		return allMetadata;
	}

	public ArrayList<HashMap<String, String>> getActMetadataByRef(String actName) {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		query.setDirectory("/aktovi/precisceni/");
		query.setCriteria("");
		SearchHandle resultsHandle = new SearchHandle();
		queryMgr.search(query, resultsHandle);
		Act actInReference = getActByUri("/aktovi/precisceni/" + actName,
				docMgr);
		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		ArrayList<HashMap<String, String>> allMetadata = new ArrayList<HashMap<String, String>>();

		for (MatchDocumentSummary result : results) {
			HashMap<String, String> actMetadata = new HashMap<String, String>();
			String uri = result.getUri();
			Act act = getActByUri(uri, docMgr);
			boolean referenced = checkIfReferenced(actInReference, act);
			if (referenced) {
				fillMetadataMap(actMetadata, uri, docMgr, client);
				allMetadata.add(actMetadata);
			}
		}
		client.release();
		return allMetadata;
	}


	public ArrayList<HashMap<String, String>> getActsMetaDataFilteredByMetadata(ActMetaDataFilterDTO filter) {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		query.setDirectory("/aktovi/precisceni/");
		query.setCriteria("");
		SearchHandle resultsHandle = new SearchHandle();
		queryMgr.search(query, resultsHandle);
		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		ArrayList<HashMap<String, String>> allMetadata = new ArrayList<HashMap<String, String>>();
		
		for (MatchDocumentSummary result : results) {
			HashMap<String, String> actMetadata = new HashMap<String, String>();
			String uri = result.getUri();
			Act act = getActByUri(uri, docMgr);
			boolean allIsWell = allIsWell(act, filter);
			if (allIsWell){
				fillMetadataMap(actMetadata, uri, docMgr, client);
				allMetadata.add(actMetadata);
			}
		}
		client.release();
		return allMetadata;
	}
	
	private boolean allIsWell(Act act, ActMetaDataFilterDTO filter) {
		boolean allIsWell = true;
		if (filter.getName() != null && filter.getName().trim().length()<1 && filter.getStatus() != null && filter.getStatus().trim().length()<1){
			allIsWell = false;
			return allIsWell;
		}
		if (filter.getName() != null && filter.getName().trim().length()>0){
			if (!act.getNaziv().equals(filter.getName())){
				return false;
			}
		}
		if ( filter.getStatus() != null && filter.getStatus().trim().length()>0){
			if (!act.getStatus().equals(filter.getStatus())){
				return false;
			}
		}
		if (filter.getVotesAgainst() != -1){
			if (act.getPodaciOGlasanju().getBrojGlasovaProtiv() != filter.getVotesAgainst()){
				return false;
			}
		}
		if (filter.getVotesFor() != -1){
			if (act.getPodaciOGlasanju().getBrojGlasovaZa() != filter.getVotesFor()){
				return false;
			}
		}
		if (filter.getVotesReserved() != -1){
			if (act.getPodaciOGlasanju().getBrojGlasovaUzdrzan() != filter.getVotesReserved()){
				return false;
			}
		}
		if (act.getPodaciORazresenjuStatusa()==null){
			act.setPodaciORazresenjuStatusa(new PodaciORazresenjuStatusa());
		}
		if (!(filter.getDateFrom() == null)){
			if (!(filter.getDateTo() == null)){
				if (filter.getAcceptType().equals("U_nacelu")){
					Date date = act.getPodaciORazresenjuStatusa().getDatumPrihvatanjaUNacelu().toGregorianCalendar().getTime();
					if (date.after(filter.getDateFrom()) && date.before(filter.getDateTo())){
						allIsWell = true;
					}
				}
				if (filter.getAcceptType().equals("U_pojedinostima")){
					Date date = act.getPodaciORazresenjuStatusa().getDatumPrihvatanjaUPojedinostima().toGregorianCalendar().getTime();
					if (date.after(filter.getDateFrom()) && date.before(filter.getDateTo())){
						allIsWell = true;
					}
				}
				if (filter.getAcceptType().equals("U_celosti")){
					Date date = act.getPodaciORazresenjuStatusa().getDatumPrihvatanjaUCelosti().toGregorianCalendar().getTime();
					if (date.after(filter.getDateFrom()) && date.before(filter.getDateTo())){
						allIsWell = true;
					}
				}
			}else{
				if (filter.getAcceptType().equals("U_nacelu")){
					Date date = act.getPodaciORazresenjuStatusa().getDatumPrihvatanjaUNacelu().toGregorianCalendar().getTime();
					long julianDayNumber1 = date.getTime() / 86400000;
					long julianDayNumber2 = filter.getDateFrom().getTime() / 86400000;;
					if (julianDayNumber1 != julianDayNumber2){
						return false;
					}
				}
				if (filter.getAcceptType().equals("U_pojedinostima")){
					Date date = act.getPodaciORazresenjuStatusa().getDatumPrihvatanjaUPojedinostima().toGregorianCalendar().getTime();
					long julianDayNumber1 = date.getTime() / 86400000;
					long julianDayNumber2 = filter.getDateFrom().getTime() / 86400000;;
					if (julianDayNumber1 != julianDayNumber2){
						return false;
					}
				}
				if (filter.getAcceptType().equals("U_celosti")){
					Date date = act.getPodaciORazresenjuStatusa().getDatumPrihvatanjaUCelosti().toGregorianCalendar().getTime();
					long julianDayNumber1 = date.getTime() / 86400000;
					long julianDayNumber2 = filter.getDateFrom().getTime() / 86400000;;
					if (julianDayNumber1 != julianDayNumber2){
						return false;
					}
				}
			}
		}
		return allIsWell;
	}

	private boolean checkIfReferenced(Act actInReference, Act act) {
		String name = actInReference.getNaziv();
		boolean returnValue = false;
		for (Act.Deo deo : act.getDeo())
			for (Glava glava : deo.getGlava())
				for (Glava.Odeljak odeljak : glava.getOdeljak()) {
					if (odeljak.getPododeljak().size() > 0) {
						for (Glava.Odeljak.Pododeljak pododeljak : odeljak
								.getPododeljak()) {
							checkInClans(returnValue, pododeljak.getClan(),
									name);
							if (returnValue)
								break;
						}
					} else {
						checkInClans(returnValue, odeljak.getClan(), name);
						if (returnValue)
							break;
					}
				}

		return returnValue;
	}

	private void checkInClans(boolean returnValue, List<Clan> clan, String name) {
		for (Object content : clan) {
			if (content instanceof Ref) {
				if (((Ref) content).getAkt().equals(name)) {
					returnValue = true;
					return;
				}
			} else if (content instanceof Stav) {
				for (Object stavContent : ((Stav) content).getContent()) {
					if (stavContent instanceof Ref) {
						if (((Ref) stavContent).getAkt().equals(name)) {
							returnValue = true;
							return;
						}
					} else if (stavContent instanceof Tacka) {
						for (Object tackaContent : ((Tacka) stavContent)
								.getContent()) {
							if (tackaContent instanceof Ref) {
								if (((Ref) tackaContent).getAkt().equals(name)) {
									returnValue = true;
									return;
								}
							} else if (tackaContent instanceof Podtacka) {
								for (Object podtackaContent : ((Podtacka) tackaContent)
										.getContent()) {
									if (podtackaContent instanceof Ref) {
										if (((Ref) podtackaContent).getAkt()
												.equals(name)) {
											returnValue = true;
											return;
										}
									} else if (podtackaContent instanceof Clan.Stav.Tacka.Podtacka.Alineja) {
										for (Object alinejaContent : ((Clan.Stav.Tacka.Podtacka.Alineja) podtackaContent)
												.getContent()) {
											if (alinejaContent instanceof Ref) {
												if (((Ref) alinejaContent)
														.getAkt().equals(name)) {
													returnValue = true;
													return;
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
		}
	}

	private Act getActByUri(String uri, XMLDocumentManager docMgr) {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Act.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Act> readHandle = new JAXBHandle<Act>(context);
		docMgr.read(uri, readHandle);
		Act act = readHandle.get();
		return act;
	}

	public boolean acceptAct(ActMetaDataDTO actMeta) throws DatatypeConfigurationException {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Act.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Act> readHandle = new JAXBHandle<Act>(context);
		JAXBHandle<Act> readHandlePreciscen = new JAXBHandle<Act>(context);
		try {
			docMgr.read("/aktovi/" + actMeta.getName(), readHandle);
			docMgr.read("/aktovi/precisceni/" + actMeta.getName(), readHandlePreciscen);
		} catch (ResourceNotFoundException e) {
			client.release();
			return false;
		}
		Act act = readHandle.get();
		Act actPreciscen = readHandlePreciscen.get();
		act.setStatus(AktStatus.Prihvacen.toString());
		if(act.getPodaciOGlasanju() == null) {
			act.setPodaciOGlasanju(new PodaciOGlasanju());
		}
		if(act.getPodaciORazresenjuStatusa() == null) {
			act.setPodaciORazresenjuStatusa(new PodaciORazresenjuStatusa());
		}
		act.getPodaciOGlasanju().setBrojGlasovaProtiv(actMeta.getVotesAgainst());
		act.getPodaciOGlasanju().setBrojGlasovaUzdrzan(actMeta.getVotesReserved());
		act.getPodaciOGlasanju().setBrojGlasovaZa(actMeta.getVotesFor());
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime(new Date());
		GregorianCalendar c2 = new GregorianCalendar();
		c2.setTime(new Date());
		GregorianCalendar c3 = new GregorianCalendar();
		c3.setTime(new Date());
		act.getPodaciORazresenjuStatusa().setDatumPrihvatanjaUCelosti(DatatypeFactory.newInstance().newXMLGregorianCalendar(c1));
		act.getPodaciORazresenjuStatusa().setDatumPrihvatanjaUNacelu(DatatypeFactory.newInstance().newXMLGregorianCalendar(c2));
		act.getPodaciORazresenjuStatusa().setDatumPrihvatanjaUPojedinostima(DatatypeFactory.newInstance().newXMLGregorianCalendar(c3));
		actPreciscen.setStatus(AktStatus.Prihvacen.toString());
		
		if(actPreciscen.getPodaciOGlasanju() == null) {
			actPreciscen.setPodaciOGlasanju(new PodaciOGlasanju());
		}
		if(actPreciscen.getPodaciORazresenjuStatusa() == null) {
			actPreciscen.setPodaciORazresenjuStatusa(new PodaciORazresenjuStatusa());
		}
		actPreciscen.getPodaciOGlasanju().setBrojGlasovaProtiv(actMeta.getVotesAgainst());
		actPreciscen.getPodaciOGlasanju().setBrojGlasovaUzdrzan(actMeta.getVotesReserved());
		actPreciscen.getPodaciOGlasanju().setBrojGlasovaZa(actMeta.getVotesFor());
		actPreciscen.getPodaciORazresenjuStatusa().setDatumPrihvatanjaUCelosti(DatatypeFactory.newInstance().newXMLGregorianCalendar(c1));
		actPreciscen.getPodaciORazresenjuStatusa().setDatumPrihvatanjaUNacelu(DatatypeFactory.newInstance().newXMLGregorianCalendar(c2));
		actPreciscen.getPodaciORazresenjuStatusa().setDatumPrihvatanjaUPojedinostima(DatatypeFactory.newInstance().newXMLGregorianCalendar(c3));
		
		JAXBHandle<Act> writeHandle = new JAXBHandle<Act>(context);
		JAXBHandle<Act> writeHandlePreciscen = new JAXBHandle<Act>(context);
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(act.getPredlagac());
		writeHandle.set(act);
		writeHandlePreciscen.set(actPreciscen);
		docMgr.write("/aktovi/" + act.getNaziv(), metadata, writeHandle);
		docMgr.write("/aktovi/precisceni/" + act.getNaziv(), metadata,
				writeHandlePreciscen);

		client.release();
		return true;
	}

	private void fillMetadataMap(HashMap<String, String> metadata, String uri,
			XMLDocumentManager docMgr, DatabaseClient client) {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Act.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Act> readHandle = new JAXBHandle<Act>(context);
		docMgr.read(uri, readHandle);
		Act act = readHandle.get();
		User user = userService.getByEmail(act.getPredlagac());
		metadata.put("User info", user.getName() + " " + user.getSurname());
		metadata.put("Naziv", act.getNaziv());
		metadata.put("Status", act.getStatus());
		if (act.getPodaciOGlasanju() == null){
			act.setPodaciOGlasanju(new PodaciOGlasanju());
		}
		if (act.getPodaciORazresenjuStatusa() == null){
			act.setPodaciORazresenjuStatusa(new PodaciORazresenjuStatusa());
		}
		
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		query.setCollections(act.getNaziv());
		query.setCriteria("");
		SearchHandle resultsHandle = new SearchHandle();
		queryMgr.search(query, resultsHandle);

		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		int deleteAm = 0;
		int updateAm = 0;
		int appendAm = 0;
		for (MatchDocumentSummary result : results) {
			String amUri = result.getUri();
			JAXBContext amContext = null;
			try {
				context = JAXBContext.newInstance(Amendment.class);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			JAXBHandle<Amendment> amReadHandle = new JAXBHandle<Amendment>(
					amContext);
			docMgr.read(amUri, amReadHandle);
			Amendment am = amReadHandle.get();
			if (am.getTip() == "Brisanje")
				deleteAm++;
			else if (am.getTip() == "Izmena")
				updateAm++;
			else if (am.getTip() == "Dodavanje")
				appendAm++;
		}
		metadata.put("Delete amendments", Integer.toString(deleteAm));
		metadata.put("Update amendments", Integer.toString(updateAm));
		metadata.put("Append amendments", Integer.toString(appendAm));
	}

	private boolean alreadyExists(XMLDocumentManager docMgr, Act act) {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Act.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Act> readHandle = new JAXBHandle<Act>(context);
		try {
			docMgr.read("/aktovi/" + act.getNaziv(), readHandle);
		} catch (ResourceNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public Act getActByName(String name){
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
		
		if(!alreadyExistsByActName(docManager, name)){
			client.release();
			return null;
		}
		
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Act.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Act> handle = new JAXBHandle<Act>(context);
		docManager.read("/aktovi/"+name, handle);
		Act act= handle.get();
	
		client.release();
		return act;
	}
	
	public Stav getParagraphByUri(String uri){
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
		
//		if(!alreadyExistsByActName(docManager, name)){
//			client.release();
//			return null;
//		}
		
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Stav.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Stav> handle = new JAXBHandle<Stav>(context);
		docManager.read(uri, handle);
		Stav stav= handle.get();
		
		client.release();
		return stav;
	}
	
	public Tacka getClauseByUri(String uri){
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
//		
//		if(!alreadyExistsByActName(docManager, name)){
//			client.release();
//			return null;
//		}
		
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Tacka.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Tacka> handle = new JAXBHandle<Tacka>(context);
		docManager.read(uri, handle);
		Tacka tacka= handle.get();
		
		client.release();
		return tacka;
	}
	
	public Podtacka getSubclauseByUri(String uri){
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
//		
//		if(!alreadyExistsByActName(docManager, name)){
//			client.release();
//			return null;
//		}
		
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Podtacka.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Podtacka> handle = new JAXBHandle<Podtacka>(context);
		docManager.read(uri, handle);
		Podtacka podtacka= handle.get();
		
		client.release();
		return podtacka;
	}
	
	public Alineja getAlinejaByUri(String uri){
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docManager=client.newXMLDocumentManager();
//		
//		if(!alreadyExistsByActName(docManager, name)){
//			client.release();
//			return null;
//		}
		
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Alineja.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Alineja> handle = new JAXBHandle<Alineja>(context);
		docManager.read(uri, handle);
		Alineja alineja= handle.get();
		
		client.release();
		return alineja;
	}
	
	public boolean alreadyExistsByActName(XMLDocumentManager docMgr, String actName) {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Act.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Act> readHandle = new JAXBHandle<Act>(context);
		try {
			docMgr.read("/aktovi/" + actName, readHandle);
		} catch (ResourceNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public void deleteAllActs() {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		
		QueryManager queryMgr = client.newQueryManager();
		StringQueryDefinition query = queryMgr.newStringDefinition();
		query.setDirectory("/aktovi/precisceni");
		query.setCriteria("");
		SearchHandle resultsHandle = new SearchHandle();
		queryMgr.search(query, resultsHandle);

		MatchDocumentSummary[] results = resultsHandle.getMatchResults();
		for (MatchDocumentSummary result : results) {
			String uri = result.getUri();
			Transaction transaction = client.openTransaction();
			try {
				docMgr.delete(uri , transaction);
				transaction.commit();
			} catch (Exception e) {
				e.printStackTrace();
				client.release();
			}
		}
		
		
		client.release();
	}
	
	public boolean updateAct(Act act){
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		boolean exists = alreadyExists(docMgr, act);
		if (!exists) {
			client.release();
			return false;
		}
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Act.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<Act> writeHandle = new JAXBHandle<Act>(context);
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(act.getPredlagac());
		writeHandle.set(act);
		docMgr.write("/aktovi/precisceni/" + act.getNaziv(), metadata,
				writeHandle);

		client.release();
		return true;
	}

	public List<Act> getAllActs(){
		List<Act> allActs=new ArrayList<>();
		
		for(String name:allActNames()){
			allActs.add(getActByName(name));
		}
		
		return allActs;
	}

}
