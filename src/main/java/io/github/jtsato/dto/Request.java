package io.github.jtsato.dto;

import java.io.Serializable;

public class Request implements Serializable {

	private static final long serialVersionUID = 4142073284044370603L;

	private String applicationId;
	private String clientId;
	private String locale;
	private String collection;
	private String document;

	public Request(final String applicationId, final String clientId, final String locale, final String collection, final String document) {
		super();
		this.applicationId = applicationId;
		this.clientId = clientId;
		this.locale = locale;
		this.collection = collection;
		this.document = document;
	}

	public final String getApplicationId() {
		return this.applicationId;
	}

	public final String getClientId() {
		return this.clientId;
	}

	public final String getCollection() {
		return this.collection;
	}

	public final String getDocument() {
		return this.document;
	}

	public final String getLocale() {
		return this.locale;
	}

	public final void setApplicationId(final String applicationId) {
		this.applicationId = applicationId;
	}

	public final void setClientId(final String clientId) {
		this.clientId = clientId;
	}

	public final void setCollection(final String collection) {
		this.collection = collection;
	}

	public final void setDocument(final String document) {
		this.document = document;
	}

	public final void setLocale(final String locale) {
		this.locale = locale;
	}

	@Override
	public String toString() {
		return String.format("Request [applicationId=%s, clientId=%s, locale=%s, collection=%s, document=%s]",
				this.applicationId, this.clientId, this.locale, this.collection, this.document);
	}
}
