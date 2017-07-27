package io.github.jtsato.dao;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.jtsato.util.MasterUtil;

/**
 * Created by Jorge Takeshi Sato on 25/07/2017.
 */

public class MasterDAO {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MasterDAO(MongoClientURI mongoClientURI, MongoClient mongoClient) throws Exception {
        this.mongoClient = mongoClient;
        this.mongoDatabase = this.mongoClient.getDatabase(mongoClientURI.getDatabase());
    }

    public Document create(String collection, String document64) throws Exception {
        final Document newDocument = MasterUtil.convert(document64);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        mongoCollection.insertOne(newDocument);
        return newDocument;
    }

    public void delete(String collection, String documentKey64) throws Exception {
        final Document deleteDocument = MasterUtil.convert(documentKey64);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        mongoCollection.deleteOne(deleteDocument);
    }

    public Document update(String collection, String documentKey64, String document64) throws Exception {
        final Document updateDocumentKey = MasterUtil.convert(documentKey64);
        final Document updateDocument = MasterUtil.convert(document64);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        mongoCollection.replaceOne(updateDocumentKey, updateDocument);
        return updateDocument;
    }

    public ArrayList<Document> read(String collection, String queryDocument64, String orderByDocument64) throws Exception {
    	
        final Document queryDocument = MasterUtil.convert(queryDocument64);
        final Document orderDocument = MasterUtil.convert(orderByDocument64);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        
        final ArrayList<Document> arrayOfDocuments = new ArrayList<>(0);
        final FindIterable<Document> cursor = (orderDocument == null) ? mongoCollection.find(queryDocument) : mongoCollection.find(queryDocument).sort(orderDocument);
		
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