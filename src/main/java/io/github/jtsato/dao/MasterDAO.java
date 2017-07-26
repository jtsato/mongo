package io.github.jtsato.dao;

import java.util.ArrayList;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Created by JT88CA on 25/07/2017.
 */
public class MasterDAO {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MasterDAO(MongoClientURI mongoClientURI, MongoClient mongoClient) throws Exception {
        this.mongoClient = mongoClient;
        this.mongoDatabase = this.mongoClient.getDatabase(mongoClientURI.getDatabase());
    }

    public Document create(String collection, String document64) throws Exception {
        final String documentAsString = Base64.isBase64(document64) ? new String(Base64.decodeBase64(document64)) : document64;
        final Document newDocument = Document.parse(documentAsString);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        mongoCollection.insertOne(newDocument);
        return newDocument;
    }

    public void delete(String collection, String documentKey64) throws Exception {
        byte[] decodeBase64 = Base64.decodeBase64(documentKey64);
        final String keyAsString = new String(decodeBase64);
        final Document deleteDocument = Document.parse(keyAsString);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        mongoCollection.deleteOne(deleteDocument);
    }

    public Document update(String collection, String documentKey64, String document64) throws Exception {
        final String documentKeyAsString = Base64.isBase64(documentKey64) ? new String(Base64.decodeBase64(documentKey64)) : documentKey64;
        final String documentAsString = Base64.isBase64(document64) ? new String(Base64.decodeBase64(document64)) : document64;
        final Document updateDocumentKey = Document.parse(documentKeyAsString);
        final Document updateDocument = Document.parse(documentAsString);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        mongoCollection.updateOne(updateDocumentKey, updateDocument);
        return updateDocument;
    }

    public ArrayList<Document> read(String collection, String queryDocument64, String orderByDocument64) throws Exception {
    	
    	final String queryDocument64AsString = Base64.isBase64(queryDocument64) ? new String(Base64.decodeBase64(queryDocument64)) : queryDocument64;
    	final String orderDocument64AsString = Base64.isBase64(orderByDocument64) ? new String(Base64.decodeBase64(orderByDocument64)) : orderByDocument64;
        final Document queryDocument = Document.parse(queryDocument64AsString);
        final Document orderDocument = Document.parse(orderDocument64AsString);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        
        final ArrayList<Document> arrayOfDocuments = new ArrayList<>(0);
        FindIterable<Document> cursor = mongoCollection.find(queryDocument).sort(orderDocument);
		
        cursor.forEach((Block<Document>) document -> {
        	arrayOfDocuments.add(document);
        });
        
        cursor.iterator().close();
        
        return arrayOfDocuments;
    }
        
    
    @Override
    public void finalize(){
        if (this.mongoClient != null){
            this.mongoClient.close();
        }
    }

/*
    public void updatePerson(Person p) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(p.getId())).get();
        this.col.update(query, PersonConverter.toDBObject(p));
    }

    public List<Person> readAllPerson() {
        List<Person> data = new ArrayList<Person>();
        DBCursor cursor = col.find();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            Person p = PersonConverter.toPerson(doc);
            data.add(p);
        }
        return data;
    }


    public Person readPerson(Person p) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(p.getId())).get();
        DBObject data = this.col.findOne(query);
        return PersonConverter.toPerson(data);
    }
*/
}