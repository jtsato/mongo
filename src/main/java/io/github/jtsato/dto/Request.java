package io.github.jtsato.dto;

import java.io.Serializable;

public class Request implements Serializable {

	private static final long serialVersionUID = 4142073284044370603L;

	private String statement;
	private String applicationId;
	private String clientId;
	private String collection;
	private String documentKey;
	private String document;
	
	public Request(String statement, String applicationId, String clientId, String collection, String documentKey, String document) {
		super();
		this.statement = statement;
		this.applicationId = applicationId;
		this.clientId = clientId;
		this.collection = collection;
		this.documentKey = documentKey;
		this.document = document;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getDocumentKey() {
		return documentKey;
	}

	public void setDocumentKey(String documentKey) {
		this.documentKey = documentKey;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
}
