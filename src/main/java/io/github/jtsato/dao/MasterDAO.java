package io.github.jtsato.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bson.Document;

/**
 * Created by JT88CA on 25/07/2017.
 */
public class MasterDAO {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MasterDAO(MongoClientURI mongoClientURI, MongoClient mongoClient) throws Exception {
        this.mongoClient = mongoClient;
        this.mongoDatabase = mongoClient.getDatabase(mongoClientURI.getDatabase());
    }

    public Document create(String collection, String jsonDocument) throws Exception {
        final String documentAsString = Base64.isBase64(jsonDocument) ? new String(Base64.decodeBase64(jsonDocument)) : jsonDocument;
        final Document document = Document.parse(documentAsString);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        mongoCollection.insertOne(document);
        return document;
    }

    public void delete(String collection, String id) throws Exception {
        byte[] decodeBase64 = Base64.decodeBase64(id);
        final String documentAsString = new String(decodeBase64);
        final Document document = Document.parse(documentAsString);
        final MongoCollection<Document> mongoCollection = this.mongoDatabase.getCollection(collection);
        mongoCollection.deleteOne(document);
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