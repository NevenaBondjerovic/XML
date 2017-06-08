package com.app.transformers;

import java.util.LinkedHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.app.DTO.ActDTO;
import com.app.DTO.ActMetaDataDTO.AktStatus;
import com.app.config.Config;
import com.app.model.Act;
import com.app.model.Act.Deo;
import com.app.model.Clan;
import com.app.model.Clan.Stav;
import com.app.model.Clan.Stav.Tacka;
import com.app.model.Clan.Stav.Tacka.Podtacka;
import com.app.model.Clan.Stav.Tacka.Podtacka.Alineja;
import com.app.model.Glava;
import com.app.model.Glava.Odeljak;
import com.app.model.Glava.Odeljak.Pododeljak;
import com.app.model.Ref;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.JAXBHandle;

public class ActDTOToAct implements ITransformer{

	private DatabaseClient client;
	private Act act;
	
	@Override
	public Object transform(Object o) {
		client = DatabaseClientFactory.newClient(Config.host, Config.port,
				Config.database, Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		ActDTO dto = (ActDTO) o;
		act = new Act();
		act.setPredlagac(dto.getPredlagac());
		act.setNaziv(dto.getName());
		act.setStatus(AktStatus.U_procesu.toString());
		
		for (ActDTO deo : dto.getChildren()){
			Deo d = new Deo();
			int i = deo.getId();
			d.setId(Integer.toString(i));
			d.setNaziv(deo.getName());
			insertGlave(d, deo, docMgr, new String(act.getNaziv()));
			act.getDeo().add(d);
		}
		return act;
	}

	private void insertGlave(Deo d, ActDTO deo, XMLDocumentManager docMgr, String fullPath) {
		for (ActDTO glava : deo.getChildren()){
			Glava g = new Glava();
			int i = glava.getId();
			g.setId(Integer.toString(i));
			g.setNaziv(glava.getName());
			String n = fullPath+g.getNaziv();
			insertOdeljke(g, glava, docMgr, new String(n));
			d.getGlava().add(g);
		}
	}

	private void insertOdeljke(Glava g, ActDTO glava, XMLDocumentManager docMgr, String fullPath) {
		for (ActDTO odeljak : glava.getChildren()){
			Odeljak o = new Odeljak();
			o.setId(Integer.toString(odeljak.getId()));
			o.setNaziv(odeljak.getName());
			String n = fullPath + o.getNaziv();
			if (odeljak.getChildType().toString().equals("Pododeljak")){
				insertPododeljke(o, odeljak, docMgr, new String(n));
			}else if(odeljak.getChildType().equals("Clan")){
				insertClanove(null, o, odeljak, docMgr, new String(n));
			}
			g.getOdeljak().add(o);
		}
	}

	private void insertPododeljke(Odeljak o, ActDTO odeljak, XMLDocumentManager docMgr, String fullPath) {
		for (ActDTO pododeljak : odeljak.getChildren()){
			Pododeljak po = new Pododeljak();
			po.setId(Integer.toString(pododeljak.getId()));
			po.setNaziv(pododeljak.getName());
			String n = fullPath + po.getNaziv();
			insertClanove(po, null, pododeljak, docMgr, new String(n));
			o.getPododeljak().add(po);
		}
	}

	private void insertClanove(Pododeljak po, Odeljak o, ActDTO parent, XMLDocumentManager docMgr, String fullPath) {
		for (ActDTO clan : parent.getChildren()){
			Clan c = new Clan();
			c.setId(Integer.toString(clan.getId()));
			c.setNaziv(clan.getName());
			if (clan.getContent()!=null)
			for (Object obj : clan.getContent()){
				if (obj instanceof String){
					c.getContent().add(obj);
				}else if (obj instanceof LinkedHashMap){
					Ref ref = new Ref();
					refDTOToRef((LinkedHashMap) obj, ref);
					c.getContent().add(ref);
				}
			}
			String n = act.getNaziv() + c.getNaziv();
			insertStavovi(c, clan, docMgr, new String(n));
			if (po != null){
				po.getClan().add(c);
			}else if (o != null){
				o.getClan().add(c);
			}
		}
	}

	private void insertStavovi(Clan c, ActDTO clan, XMLDocumentManager docMgr, String fullPath) {
		for (ActDTO stav : clan.getChildren()){
			Stav s = new Stav();
			s.setId(Integer.toString(stav.getId()));
			if (stav.getContent()!=null)
			for (Object obj : stav.getContent()){
				if (obj instanceof String){
					s.getContent().add(obj);
				}else if (obj instanceof LinkedHashMap){
					Ref ref = new Ref();
					refDTOToRef((LinkedHashMap) obj, ref);
					s.getContent().add(ref);
				}
			}
			String n = fullPath + s.getId();
			insertTacke(s, stav, docMgr, new String(n));
			JAXBContext context = null;
			try {
				context = JAXBContext.newInstance(Stav.class);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			JAXBHandle<Stav> writeHandle = new JAXBHandle<Stav>(context);
			writeHandle.set(s);
			n = n.replace(' ', '_');
			docMgr.write(n, writeHandle);
			c.getContent().add(s);
		}
	}

	private void insertTacke(Stav s, ActDTO stav, XMLDocumentManager docMgr, String fullPath) {
		for (ActDTO tacka : stav.getChildren()){
			Tacka t = new Tacka();
			t.setId(Integer.toString(tacka.getId()));
			if (tacka.getContent()!=null)
			for (Object obj : tacka.getContent()){
				if (obj instanceof String){
					t.getContent().add(obj);
				}else if (obj instanceof LinkedHashMap){
					Ref ref = new Ref();
					refDTOToRef((LinkedHashMap) obj, ref);
					t.getContent().add(ref);
				}
			}
			String n = fullPath + t.getId();
			insertPodtacke(t, tacka, docMgr, new String(n));
			JAXBContext context = null;
			try {
				context = JAXBContext.newInstance(Tacka.class);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			JAXBHandle<Tacka> writeHandle = new JAXBHandle<Tacka>(context);
			writeHandle.set(t);
			n = n.replace(' ', '_');
			docMgr.write(n, writeHandle);
			s.getContent().add(t);
		}
	}

	private void insertPodtacke(Tacka t, ActDTO tacka, XMLDocumentManager docMgr, String fullPath) {
		for (ActDTO podtacka : tacka.getChildren()){
			Podtacka pt = new Podtacka();
			pt.setId(Integer.toString(podtacka.getId()));
			if (podtacka.getContent()!=null)
			for (Object obj : podtacka.getContent()){
				if (obj instanceof String){
					pt.getContent().add(obj);
				}else if (obj instanceof LinkedHashMap){
					Ref ref = new Ref();
					refDTOToRef((LinkedHashMap) obj, ref);
					pt.getContent().add(ref);
				}
			}
			String n = fullPath + pt.getId();
			insertAlineje(pt, podtacka, docMgr, new String(n));
			JAXBContext context = null;
			try {
				context = JAXBContext.newInstance(Podtacka.class);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			JAXBHandle<Podtacka> writeHandle = new JAXBHandle<Podtacka>(context);
			writeHandle.set(pt);
			n = n.replace(' ', '_');
			docMgr.write(n, writeHandle);
			t.getContent().add(pt);
		}
		
	}

	private void insertAlineje(Podtacka pt, ActDTO podtacka, XMLDocumentManager docMgr, String fullPath) {
		for (ActDTO alineja : podtacka.getChildren()){
			Alineja a = new Alineja();
			a.setId(Integer.toString(alineja.getId()));
			if (alineja.getContent()!=null)
			for (Object obj : alineja.getContent()){
				if (obj instanceof String){
					a.getContent().add(obj);
				}else if (obj instanceof LinkedHashMap){
					Ref ref = new Ref();
					refDTOToRef((LinkedHashMap) obj, ref);
					a.getContent().add(ref);
				}
			}
			String n = fullPath + a.getId();
			JAXBContext context = null;
			try {
				context = JAXBContext.newInstance(Alineja.class);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			JAXBHandle<Alineja> writeHandle = new JAXBHandle<Alineja>(context);
			writeHandle.set(a);
			n = n.replace(' ', '_');
			docMgr.write(n, writeHandle);
			pt.getContent().add(a);
		}
	}
	
	private void refDTOToRef(LinkedHashMap obj, Ref ref){
		ref.setAkt((String)obj.get("act"));
		ref.setContent((String)obj.get("content"));
		if ((String)obj.get("member") != null){
			ref.setClan((String)obj.get("member"));
		}
		if ((String)obj.get("paragraph") != null){
			ref.setStav((String)obj.get("paragraph"));
		}
		if ((String)obj.get("clause") != null){
			ref.setTacka((String)obj.get("clause"));
		}
		if ((String)obj.get("subclause") != null){
			ref.setPodtacka((String)obj.get("subclause"));
		}
		if ((String)obj.get("indent") != null){
			ref.setAlineja((String)obj.get("indent"));
		}
	}
}
