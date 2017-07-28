package io.github.jtsato.api.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    
    @Value("${log.request.active}")
    public String logRequestActive;    

    private static HashMap<String, MasterDAO> hashMapMasterDAO = new HashMap<String, MasterDAO>();

    @RequestMapping("/create")
    public String create(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("collection") final String collection,
            @RequestParam("document") final String document) {

    	StopWatch stopWatch = new StopWatch();
    	stopWatch.start();
    	
        final Request request = new Request("CREATE", applicationId, clientId, collection, new Date(), StringUtils.EMPTY, document);
        logRequest(request);

        try {
            MasterDAO masterDAO = this.getDataAcessObject(applicationId);
            Document result = masterDAO.create(collection, document);
        	return MasterUtil.buildSuccessOne(result);

        } catch (final Exception exception) {
            logger.error("/CREATE ", exception);
            return MasterUtil.buildError(exception);

        } finally {
        	stopWatch.stop();
        	logger.debug(String.format("/CREATE executed in %s milliseconds ", stopWatch.getTime()));
        }
    }
    

	@RequestMapping("/delete")
    public String delete(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("collection") final String collection,
            @RequestParam("queryDocument") final String queryDocument ) {

    	StopWatch stopWatch = new StopWatch();
    	stopWatch.start();

        final Request request = new Request("DELETE", applicationId, clientId, collection, new Date(), queryDocument, StringUtils.EMPTY);
        logRequest(request);

        try {
            MasterDAO masterDAO = this.getDataAcessObject(applicationId);
            masterDAO.delete(collection, queryDocument);
            return MasterUtil.buildSuccess();

        } catch (final Exception exception) {
        	logger.error("/DELETE ", exception);
            return MasterUtil.buildError(exception);

        } finally {
        	stopWatch.stop();
        	logger.debug(String.format("/DELETE executed in %s milliseconds ", stopWatch.getTime()));
        }
    }

    @RequestMapping("/update")
    public String update(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("collection") final String collection,
            @RequestParam("queryDocument") final String queryDocument, 
        	@RequestParam("document") final String document) {

    	StopWatch stopWatch = new StopWatch();
    	stopWatch.start();
    	
        final Request request = new Request("UPDATE", applicationId, clientId, collection, new Date(), queryDocument, document);
        logRequest(request);

        try {
            MasterDAO masterDAO = this.getDataAcessObject(applicationId);
            Document result = masterDAO.update(collection, queryDocument, document);
            return MasterUtil.buildSuccessOne(result);

        } catch (final Exception exception) {
            logger.error("/UPDATE ", exception);
            return MasterUtil.buildError(exception);

        } finally {
        	stopWatch.stop();
        	logger.debug(String.format("/UPDATE executed in %s milliseconds ", stopWatch.getTime()));
        }
    }
    
    @RequestMapping("/")
    public String read(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("collection") final String collection,
            @RequestParam("queryDocument") final String queryDocument, 
        	@RequestParam("sortFields") final String sortFields) {

    	StopWatch stopWatch = new StopWatch();
    	stopWatch.start();

        final Request request = new Request("READ", applicationId, clientId, collection, new Date(), queryDocument, sortFields);
        logRequest(request);

        try {
            MasterDAO masterDAO = this.getDataAcessObject(applicationId);
            ArrayList<Document> arrayOfDocument = masterDAO.read(collection, queryDocument, sortFields);
            return MasterUtil.buildSuccessMany(arrayOfDocument);

        } catch (final Exception exception) {
            logger.error("/READ ", exception);
            return MasterUtil.buildError(exception);

        } finally {
        	stopWatch.stop();
        	logger.debug(String.format("/READ executed in %s milliseconds ", stopWatch.getTime()));
        }
    }
    
    private MasterDAO getDataAcessObject(String applicationId) throws Exception {
    	
    	if (StringUtils.isBlank(applicationId)) return null;

    	MasterDAO dataAccessObject = hashMapMasterDAO.get(applicationId);
    	
        if (dataAccessObject == null){
            MongoClientURI mongoClientURI = new MongoClientURI(databaseConnectionString + applicationId);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            dataAccessObject = new MasterDAO(mongoClientURI, mongoClient);
            hashMapMasterDAO.put(applicationId, dataAccessObject);
        }

        return dataAccessObject;
    }
    
    private void logRequest(Request request) {
    	
        logger.debug(String.format("REQUEST = %s", request.toString()));
        
        if (!StringUtils.equalsIgnoreCase(logRequestActive, "Yes")) return;
        if (StringUtils.isBlank(request.getApplicationId())) return;
        
        try {
        	
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = objectMapper.writeValueAsString(request);
            MasterDAO masterDAO = this.getDataAcessObject(request.getApplicationId());
            masterDAO.create("logs", jsonRequest);
            
		} catch (Exception exception) {
	        logger.error(String.format("The request could not be saved in database %s.", databaseConnectionString + request.getApplicationId()));
		}
	}
}
