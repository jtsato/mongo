package io.github.jtsato.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jorge Takeshi Sato on 20/07/2017.
 */

public class Request implements Serializable {

	private static final long serialVersionUID = 4142073284044370603L;

	private String statement;
	private String applicationId;
	private String clientId;
	private String collection;
	private Date timestamp;
	private String documentKey;
	private String document;

	private Request() {
		super();
	}

	public Request(String statement, String applicationId, String clientId, String collection, Date timestamp,
			String documentKey, String document) {
		this();
		this.statement = statement;
		this.applicationId = applicationId;
		this.clientId = clientId;
		this.collection = collection;
		this.timestamp = timestamp;
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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Request [statement=");
		builder.append(statement);
		builder.append(", applicationId=");
		builder.append(applicationId);
		builder.append(", clientId=");
		builder.append(clientId);
		builder.append(", collection=");
		builder.append(collection);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", documentKey=");
		builder.append(documentKey);
		builder.append(", document=");
		builder.append(document);
		builder.append("]");
		return builder.toString();
	}
}
