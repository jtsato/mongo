package io.github.jtsato.api.rest;

import java.text.SimpleDateFormat;
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

import io.github.jtsato.dao.ApiDataAccess;
import io.github.jtsato.dto.Request;
import io.github.jtsato.util.ApiUtil;

/**
 * Created by Jorge Takeshi Sato on 20/07/2017.
 */

@RestController
public class ApiController {

	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    
    @Value("${database.connectionString}")
    public String databaseConnectionString;
    
    @Value("${log.request.active}")
    public String logRequestActive;    

    private static HashMap<String, ApiDataAccess> hashMapApiDao = new HashMap<String, ApiDataAccess>();

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
            ApiDataAccess apiDataAccess = this.getDataAcessObject(applicationId);
            Document result = apiDataAccess.create(collection, document);
        	return ApiUtil.buildSuccessOne(result);

        } catch (final Exception exception) {
            logger.error("/CREATE ", exception);
            return ApiUtil.buildError(exception);

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
            ApiDataAccess apiDataAccess = this.getDataAcessObject(applicationId);
            apiDataAccess.delete(collection, queryDocument);
            return ApiUtil.buildSuccess();

        } catch (final Exception exception) {
        	logger.error("/DELETE ", exception);
            return ApiUtil.buildError(exception);

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
            ApiDataAccess apiDataAccess = this.getDataAcessObject(applicationId);
            Document result = apiDataAccess.update(collection, queryDocument, document);
            return ApiUtil.buildSuccessOne(result);

        } catch (final Exception exception) {
            logger.error("/UPDATE ", exception);
            return ApiUtil.buildError(exception);

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
            ApiDataAccess apiDataAccess = this.getDataAcessObject(applicationId);
            ArrayList<Document> arrayOfDocument = apiDataAccess.read(collection, queryDocument, sortFields);
            return ApiUtil.buildSuccessMany(arrayOfDocument);

        } catch (final Exception exception) {
            logger.error("/READ ", exception);
            return ApiUtil.buildError(exception);

        } finally {
        	stopWatch.stop();
        	logger.debug(String.format("/READ executed in %s milliseconds ", stopWatch.getTime()));
        }
    }
    
    private ApiDataAccess getDataAcessObject(String applicationId) throws Exception {
    	
    	if (StringUtils.isBlank(applicationId)) return null;

    	ApiDataAccess apiDataAccess = hashMapApiDao.get(applicationId);
    	
        if (apiDataAccess == null){
            MongoClientURI mongoClientURI = new MongoClientURI(databaseConnectionString + applicationId);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            apiDataAccess = new ApiDataAccess(mongoClientURI, mongoClient);
            hashMapApiDao.put(applicationId, apiDataAccess);
        }

        return apiDataAccess;
    }
    
    private void logRequest(Request request) {
    	
        logger.debug(String.format("REQUEST = %s", request.toString()));
        
        if (!StringUtils.equalsIgnoreCase(logRequestActive, "Yes")) return;
        if (StringUtils.isBlank(request.getApplicationId())) return;
        
        try {
        	
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
            ApiDataAccess masterDAO = this.getDataAcessObject(request.getApplicationId());
            masterDAO.create("logs", objectMapper.writeValueAsString(request));
            
		} catch (Exception exception) {
	        logger.error(String.format("The request could not be saved in database %s.", databaseConnectionString + request.getApplicationId()));
		}
	}
}
