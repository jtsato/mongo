package io.github.jtsato.api.rest;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.jtsato.dto.Request;

/**
 * Created by Jorge Takeshi Sato on 20/07/2017.
 */

@RestController
public class MasterController {

	private static final Logger logger = LoggerFactory.getLogger(MasterController.class);
	private MongoClientURI mongoClientURI;
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	@RequestMapping("/create")
	public String create(
			@RequestParam("input") final String applicationId,
			@RequestParam("clientId") final String clientId,
			@RequestParam("locale") final String locale,
			@RequestParam("collection") final String collection,
			@RequestParam("value") final String document ) {

		final Request request = new Request(applicationId, clientId, locale, collection, document);

		logger.debug(String.format("/create -> request = %s", request.toString()));

		try {

			final Document mongoDocument = Document.parse(document);
			this.mongoClientURI = new MongoClientURI("mongodb://master:masterkey@ds028310.mlab.com:28310/poc");
			this.mongoClient = new MongoClient(this.mongoClientURI);
			this.mongoDatabase = this.mongoClient.getDatabase(this.mongoClientURI.getDatabase());
			final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
			mongoCollection.insertOne(mongoDocument);

		} catch (final Exception exception) {
			logger.error("/create ", exception);
			return "Error";

		} finally {
			if (this.mongoClient != null) {
				this.mongoClient.close();
			}
		}

		return "OK";
	}

	@RequestMapping
	public String hello() {
		
		try {

			this.mongoClientURI = new MongoClientURI("mongodb://master:masterkey@ds028310.mlab.com:28310/poc");
			this.mongoClient = new MongoClient(this.mongoClientURI);
			this.mongoDatabase = this.mongoClient.getDatabase(this.mongoClientURI.getDatabase());
			final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection("songs");
			final Document mongoDocument = Document.parse("{'decade':'2000s', 'artist': 'Metallica', 'song': 'Enter Sandman', weeksAtOne : 99 }");
			mongoCollection.insertOne(mongoDocument);

		} catch (final Exception exception) {
			logger.error("/create ", exception);
			return "Error";

		} finally {
			if (this.mongoClient != null) {
				this.mongoClient.close();
			}
		}
		return "Hello";
	}
}
