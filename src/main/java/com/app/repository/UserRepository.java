package com.app.repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Repository;

import com.app.config.Config;
import com.app.model.User;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.JAXBHandle;

@Repository
public class UserRepository {

	private DatabaseClient client;

	public boolean registerUser(User user) {
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();

		boolean alreadyCreated = userExists(docMgr, user.getEmail());
		if (alreadyCreated) {
			client.release();
			return false;
		}
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(User.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JAXBHandle<User> writeHandle = new JAXBHandle<User>(context);
		writeHandle.set(user);
		docMgr.write("/user/" + user.getEmail(), writeHandle);

		client.release();
		return true;
	}

	public boolean login(String email, String password) {
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(User.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<User> readHandle = new JAXBHandle<User>(context);
		try {
			docMgr.read("/user/" + email, readHandle);
			User user = readHandle.get();
			if (password.equals(user.getPassword())) {
				client.release();
				return true;
			} else {
				client.release();
				return false;
			}
		} catch (ResourceNotFoundException e) {
			client.release();
			return false;
		}
	}

	public User getByEmail(String email) {
		client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.database,
				Config.user, Config.password, Config.authType);
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(User.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<User> readHandle = new JAXBHandle<User>(context);
		try {
			docMgr.read("/user/" + email, readHandle);
			User user = readHandle.get();
			client.release();
			return user;
		} catch (ResourceNotFoundException e) {
			client.release();
			return null;
		}
	}

	private boolean userExists(XMLDocumentManager docMgr, String email) {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(User.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		JAXBHandle<User> readHandle = new JAXBHandle<User>(context);
		try {
			docMgr.read("/user/" + email, readHandle);
		} catch (ResourceNotFoundException e) {
			return false;
		}
		return true;
	}
}
