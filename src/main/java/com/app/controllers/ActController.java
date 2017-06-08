package com.app.controllers;

import java.util.ArrayList;

import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.DTO.ActDTO;
import com.app.DTO.ActMetaDataDTO;
import com.app.DTO.ActMetaDataDTO.AktStatus;
import com.app.DTO.ActMetaDataFilterDTO;
import com.app.DTO.FileDownloadDTO;
import com.app.DTO.NodeDataDTO;
import com.app.model.Act;
import com.app.model.Ref;
import com.app.services.IActService;
import com.app.transformers.ActDTOToAct;

@RestController
@RequestMapping(value = "/act")
public class ActController {


	@Autowired
	private IActService actService;
	
	@RequestMapping(path = "/allActNames", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllNames(){
		//actRepo.deleteAllActs();
		return new ResponseEntity<Object>(actService.getAllActNames(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/allActNamesPrihvaceni", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllNamesPrihvaceni(){
		ArrayList<String> acceptedActs = new ArrayList<String>();
		for (ActMetaDataDTO act : actService.getAllActMetadata()) {
			if(act.getStatus().equals(AktStatus.Prihvacen)) {
				acceptedActs.add(act.getName());
			}
		}
		return new ResponseEntity<Object>(acceptedActs, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/addAct", method = RequestMethod.POST)
	public ResponseEntity<Object> writeAct(@RequestBody ActDTO act){
		ActDTOToAct transformer = new ActDTOToAct();
		Act a = (Act) transformer.transform(act);
		if (actService.writeAct(a)){
			return new ResponseEntity<Object>(HttpStatus.CREATED);
		}else{
			return new ResponseEntity<Object>(HttpStatus.CONFLICT);
		}
	}
	
	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllActsMetaData() {
		ArrayList<ActMetaDataDTO> acceptedActs = new ArrayList<ActMetaDataDTO>();
		ArrayList<ActMetaDataDTO> acts = actService.getAllActMetadata();
		for (ActMetaDataDTO act : acts) {
			if(act.getStatus().equals(AktStatus.Prihvacen)) {
				acceptedActs.add(act);
			}
		}
		return new ResponseEntity<Object>(acceptedActs, HttpStatus.OK);
		// return new ResponseEntity<Object>(getMockedActsMetaData(),
		// HttpStatus.OK);
	}
	@RequestMapping(path = "/MembersOfAct/{act}", method = RequestMethod.GET)
	public ResponseEntity<Object> allMembersOfAct(@PathVariable String act) {
		//vraca broj clanova za prosledjeni akt
		return new ResponseEntity<Object>(getActsInProgress(),
				HttpStatus.OK);
	}

	@RequestMapping(path="/allInProgress", method=RequestMethod.GET)
	public ResponseEntity<Object> getAllActsInProgress(){
		//vratiti sve akte kojima je status U procesu, odnosno nisu jos uvek prihvaceni
		ArrayList<ActMetaDataDTO> actsInProgress = new ArrayList<ActMetaDataDTO>();
		ArrayList<ActMetaDataDTO> acts = actService.getAllActMetadata();
		for (ActMetaDataDTO act : acts) {
			if(act.getStatus().equals(AktStatus.U_procesu)) {
				actsInProgress.add(act);
			}
		}
		return new ResponseEntity<Object>(actsInProgress, HttpStatus.OK);
	}
	
	@RequestMapping(path="/accept", method=RequestMethod.PATCH)
	public ResponseEntity<Object> acceptAct(@RequestBody ActMetaDataDTO act){
		//dolazi sa klijenta objekat KOJI IMA SAMO NAME POLJE POPUNJENO. 
		//treba aktu sa tim nazivom promeniti status u Prihvacen.
		/*
		 * TODO 16.6.2016. - U DTO objektu stoje glasovi za, protiv i uzdrzani.
		 * Pored toga, u sva tri datuma ubaciti tekuci trenutak.
		 * */
		try {
			if (actService.acceptAct(act))
				return new ResponseEntity<>(HttpStatus.OK);
			else{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (DatatypeConfigurationException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/actsInProgress", method = RequestMethod.GET)
	public ResponseEntity<Object> actsInProgressFilter() {
		return new ResponseEntity<Object>(getActsInProgress(), HttpStatus.OK);
	}

	@RequestMapping(path = "/getClan/{naziv}", method = RequestMethod.GET)
	public ResponseEntity<Object> getClan(@PathVariable String naziv) {

		ArrayList<NodeDataDTO> retVal = new ArrayList<NodeDataDTO>();
		/*ArrayList<NodeDataDTO> clanovi = ge();

		for (NodeDataDTO cl : clanovi) {
			if (cl.getNaziv().equals(naziv)) {
				retVal.add(cl);
			}
		}*/
		return new ResponseEntity<Object>(retVal, HttpStatus.OK);
	}

	@RequestMapping(path = "/meta-data-filter/{keyword}", method = RequestMethod.GET)
	public ResponseEntity<Object> metadataFilter(@PathVariable String keyword) {
		ArrayList<ActMetaDataDTO> metaData = getMockedActsMetaData();

		ArrayList<ActMetaDataDTO> retVal = new ArrayList<ActMetaDataDTO>();
		for (ActMetaDataDTO akt : metaData) {
			if (akt.getName().contains(keyword)) {
				retVal.add(akt);
			}
		}

		return new ResponseEntity<Object>(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(path="/meta-data-filter", method=RequestMethod.POST)
	public ResponseEntity<Object> metadataFilter(@RequestBody ActMetaDataFilterDTO filter){
		ArrayList<ActMetaDataDTO> acts;// = getMockedActsMetaData();
		/*filtrirati akte po parametrima iz objekta i vratiti kao response IAKO JE POST METODA!
		  U filteru je obavezno ILI status akta ILI naziv akta. Minimalno moze biti jedno od ta dva, i kad je jedno uneto,
		  sve ostalo je opciono.
		  Ne moraju oba datuma biti unesena. Ako je samo dateFrom unesen a dateTo je null, onda se filtrira tacno za taj dan, 
		  a ako su oba, onda ide interval.
		  Vrednosti za status su u enumeraciji AktStatus u klasi ActMetaDataDTO, a vrednosti za acceptType ukazuju na to koji 
		  datum iz seme uzimas. Vrednosti su U_nacelu, U_pojedinostima i U_celosti.
		*/
		acts = actService.getActsMetaDataFilteredByMetadata(filter);
		return new ResponseEntity<Object>(acts, HttpStatus.OK);
	}

	@RequestMapping(path = "/content-filter/{keyword}", method = RequestMethod.GET)
	public ResponseEntity<Object> getActsByMetaDataFilter(
			@PathVariable String keyword) {
		return new ResponseEntity<Object>(actService.getActsmetadataFilteredByContent(keyword),
				HttpStatus.OK);
		/*ArrayList<ActMetaDataDTO> filteredByContent = new ArrayList<ActMetaDataDTO>();
		Ref ref = new Ref();
		Ref ref1 = new Ref();
		Ref ref2 = new Ref();
		ref.setAkt("Akt1");
		ref1.setAkt("Akt2");
		ref2.setAkt("Akt3");
		filteredByContent.add(new ActMetaDataDTO("a1", AktStatus.Prihvacen,
				"Imenko Prezimić1", 0, 2, 1,ref));
		filteredByContent.add(new ActMetaDataDTO("Naziv2", AktStatus.U_procesu,
				"Imenko Prezimić2", 0, 2, 1,ref1));
		filteredByContent.add(new ActMetaDataDTO("Naziv3", AktStatus.Prihvacen,
				"Imenko Prezimić3", 0, 2, 1,ref2));

		return new ResponseEntity<Object>(filteredByContent, HttpStatus.OK);*/
	}

	//Vraca sve akte koji spominju ovaj akt
	@RequestMapping(path = "/refs-filter/{actName}", method = RequestMethod.GET)
	public ResponseEntity<Object> getActsByRef(@PathVariable String actName) {
		return new ResponseEntity<Object>(actService.getActsByRef(actName), HttpStatus.OK);
		/*ArrayList<ActMetaDataDTO> filteredByRef = new ArrayList<ActMetaDataDTO>();
		Ref ref = new Ref();
		Ref ref1 = new Ref();
		Ref ref2 = new Ref();
		ref.setAkt("Akt1");
		ref1.setAkt("Akt2");
		ref2.setAkt("Akt3");
		filteredByRef.add(new ActMetaDataDTO("Naziv11", AktStatus.Prihvacen,
				"Imenko Prezimić1", 1, 0, 3,ref));
		filteredByRef.add(new ActMetaDataDTO("Naziv22", AktStatus.U_procesu,
				"Imenko Prezimić2", 0, 2, 1,ref1));
		filteredByRef.add(new ActMetaDataDTO("Naziv33", AktStatus.Prihvacen,
				"Imenko Prezimić3", 4, 2, 0,ref2));

		return new ResponseEntity<Object>(filteredByRef, HttpStatus.OK);*/
	}

	@RequestMapping(path = "/getByUser/{email}/all", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllActsByUser(@PathVariable String email) {
		return new ResponseEntity<Object>(
				actService.getActMetadataByUser(email), HttpStatus.OK);
		/*
		 * return new ResponseEntity<Object>(getMockedActsMetaData(),
		 * HttpStatus.OK);
		 */
	}

	@RequestMapping(path="/{name}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> deleteAct(@PathVariable String name){
		if (actService.withdrawAct(name)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(path="/generatePDF/{actName}", method=RequestMethod.GET)
	public ResponseEntity<Object> generatePDF(@PathVariable String actName){
		FileDownloadDTO download = new FileDownloadDTO("downloads/data.pdf", "data.pdf");
		if(actService.generatePDF(actName))
			return new ResponseEntity<Object>(download, HttpStatus.OK); 
		return new ResponseEntity<Object>(download, HttpStatus.NOT_FOUND);	
	}
	
	@RequestMapping(path="/generateXHTML/{actName}", method=RequestMethod.GET)
	public ResponseEntity<Object> generateXHTML(@PathVariable String actName){
		FileDownloadDTO download = new FileDownloadDTO("downloads/data.html", "data.html");
		if(actService.generateXHTML(actName))
			return new ResponseEntity<Object>(download, HttpStatus.OK); 
		return new ResponseEntity<Object>(download, HttpStatus.NOT_FOUND);	
	}

	private ArrayList<ActMetaDataDTO> getMockedActsMetaData() {
		ArrayList<ActMetaDataDTO> metaData = new ArrayList<ActMetaDataDTO>();

		Ref ref = new Ref();
		Ref ref1 = new Ref();
		Ref ref2 = new Ref();
		Ref ref3 = new Ref();
		Ref ref4 = new Ref();
		Ref ref5 = new Ref();
		Ref ref6 = new Ref();
		ref.setAkt("Akt1");
		ref1.setAkt("Akt2");
		ref2.setAkt("Akt3");
		ref3.setAkt("Akt4");
		ref4.setAkt("Ak5");
		ref5.setAkt("Akt6");
		ref6.setAkt("Akt7");
		metaData.add(new ActMetaDataDTO("Akt1", AktStatus.Prihvacen, "Imenko Prezimić1", 0, 2, 1,ref));
		metaData.add(new ActMetaDataDTO("Akt2", AktStatus.U_procesu, "Imenko Prezimić2", 0, 1, 1,ref1));
		metaData.add(new ActMetaDataDTO("Akt3", AktStatus.U_procesu, "Imenko Prezimić3", 1, 0, 1,ref2));
		metaData.add(new ActMetaDataDTO("Akt4", AktStatus.U_procesu, "Imenko Prezimić4", 1, 1, 0,ref3));
		metaData.add(new ActMetaDataDTO("Akt5", AktStatus.U_procesu, "Imenko Prezimić5", 3, 2, 1,ref4));
		metaData.add(new ActMetaDataDTO("Akt6", AktStatus.U_procesu, "Imenko Prezimić6", 0, 0, 0,ref5));
		metaData.add(new ActMetaDataDTO("Akt7", AktStatus.Prihvacen, "Imenko Prezimić7", 0, 2, 1,ref6));
		return metaData;
	}

	private ArrayList<ActMetaDataDTO> getActsInProgress() {
		ArrayList<ActMetaDataDTO> metaData = getMockedActsMetaData();
		ArrayList<ActMetaDataDTO> actsInProgress = new ArrayList<ActMetaDataDTO>();

		for (ActMetaDataDTO akt : metaData) {
			if (akt.getStatus().equals(AktStatus.U_procesu))
				actsInProgress.add(akt);

		}
		return actsInProgress;
	}


}
