package io.github.jtsato.api.rest;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import io.github.jtsato.dao.MasterDAO;
import io.github.jtsato.dto.Request;
import io.github.jtsato.util.MasterUtil;

/**
 * Created by Jorge Takeshi Sato on 20/07/2017.
 */

@RestController
public class MasterController {

    private static final Logger logger = LoggerFactory.getLogger(MasterController.class);

    @Value("${database.connectionString}")
    public String databaseConnectionString;

    public static MasterDAO masterDAO;

    public MasterDAO getMasterDAO() throws Exception {

        if (masterDAO == null){
            MongoClientURI mongoClientURI = new MongoClientURI(databaseConnectionString);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            masterDAO = new MasterDAO(mongoClientURI, mongoClient);
        }

        return masterDAO;
    }

    @RequestMapping("/create")
    public String create(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("collection") final String collection,
            @RequestParam("document") final String document) {

        final Request request = new Request("CREATE", applicationId, clientId, collection, StringUtils.EMPTY, document);

        logger.debug(String.format("REQUEST = %s", request.toString()));

        try {

            MasterDAO masterDAO = this.getMasterDAO();
            Document result = masterDAO.create(collection, document);
        	return MasterUtil.buildSuccessOne(result);

        } catch (final Exception exception) {
            logger.error("/CREATE ", exception);
            return MasterUtil.buildError(exception);

        } finally {

        }
    }
    

    @RequestMapping("/delete")
    public String delete(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("collection") final String collection,
            @RequestParam("queryDocument") final String queryDocument ) {

        final Request request = new Request("DELETE", applicationId, clientId, collection, queryDocument, StringUtils.EMPTY);

        logger.debug(String.format("REQUEST = %s", request.toString()));

        try {

            MasterDAO masterDAO = this.getMasterDAO();
            masterDAO.delete(collection, queryDocument);
            return MasterUtil.buildSuccess();

        } catch (final Exception exception) {
        	logger.error("/DELETE ", exception);
            return MasterUtil.buildError(exception);

        } finally {

        }
    }

    @RequestMapping("/update")
    public String update(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("collection") final String collection,
            @RequestParam("queryDocument") final String queryDocument, 
        	@RequestParam("document") final String document) {

        final Request request = new Request("UPDATE", applicationId, clientId, collection, queryDocument, document);

        logger.debug(String.format("REQUEST = %s", request.toString()));

        try {

            MasterDAO masterDAO = this.getMasterDAO();
            Document result = masterDAO.update(collection, queryDocument, document);
            return MasterUtil.buildSuccessOne(result);

        } catch (final Exception exception) {
            logger.error("/UPDATE ", exception);
            return MasterUtil.buildError(exception);

        } finally {

        }
    }
    
    @RequestMapping("/")
    public String read(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("collection") final String collection,
            @RequestParam("queryDocument") final String queryDocument, 
        	@RequestParam("sortFields") final String sortFields) {

        final Request request = new Request("READ", applicationId, clientId, collection, queryDocument, sortFields);

        logger.debug(String.format("REQUEST = %s", request.toString()));

        try {

            MasterDAO masterDAO = this.getMasterDAO();
            ArrayList<Document> arrayOfDocument = masterDAO.read(collection, queryDocument, sortFields);
            return MasterUtil.buildSuccessMany(arrayOfDocument);

        } catch (final Exception exception) {
            logger.error("/READ ", exception);
            return MasterUtil.buildError(exception);

        } finally {

        }
    }        
}
