package io.github.jtsato.api.rest;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import io.github.jtsato.dao.MasterDAO;
import io.github.jtsato.dto.Request;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam("language") final String language,
            @RequestParam("collection") final String collection,
            @RequestParam("value") final String document64) {

        final Request request = new Request(applicationId, clientId, language, collection, document64);

        logger.debug(String.format("/CREATE -> REQUEST = %s", request.toString()));

        try {

            MasterDAO masterDAO = this.getMasterDAO();
            masterDAO.create(collection, document64);

        } catch (final Exception exception) {
            if (exception instanceof MongoWriteException){
                return "ERROR|" + StringUtils.substringBefore(exception.getMessage(), ":");
            }
            logger.error("/CREATE ", exception);
            return "ERROR";

        } finally {

        }

        return "SUCCESS";
    }

    @RequestMapping("/delete")
    public String delete(
            @RequestParam("applicationId") final String applicationId,
            @RequestParam("clientId") final String clientId,
            @RequestParam("language") final String language,
            @RequestParam("collection") final String collection,
            @RequestParam("id") final String id ) {

        final Request request = new Request(applicationId, clientId, language, collection, id);

        logger.debug(String.format("/DELETE -> REQUEST = %s", request.toString()));

        try {

            MasterDAO masterDAO = this.getMasterDAO();
            masterDAO.delete(collection, id);

        } catch (final Exception exception) {
            if (exception instanceof MongoWriteException){
                return "ERROR|" + StringUtils.substringBefore(exception.getMessage(), ":");
            }
            logger.error("/DELETE ", exception);
            return "ERROR";

        } finally {

        }

        return "SUCCESS";
    }

    @RequestMapping
    public String root() {

        try {

            final String input = "{ '_id': '597750996d6b6d77ebd41a05', 'index': 0, 'guid': '666493c6-d932-4b31-bd1d-1bd5893366dd', 'isActive': true, 'balance': '$1,658.21', 'picture': 'http://placehold.it/32x32', 'age': 32, 'eyeColor': 'brown', 'name': 'Dixon Allen', 'gender': 'male', 'company': 'EXOSWITCH', 'email': 'dixonallen@exoswitch.com', 'phone': '+1 (887) 595-2640', 'address': '861 Krier Place, Linganore, Rhode Island, 2752', 'about': 'In officia incididunt non ut est proident incididunt ad nostrud velit. Magna eiusmod consequat fugiat nulla esse labore. Sunt exercitation id exercitation excepteur excepteur cupidatat incididunt proident ex fugiat id voluptate commodo id. Aliqua reprehenderit qui nostrud fugiat dolor ipsum aliquip ad exercitation ad officia.\\r\\n', 'registered': '2014-07-31T10:46:39 +03:00', 'latitude': -52.573337, 'longitude': -57.233864, 'tags': [ 'consequat', 'enim', 'Lorem', 'sunt', 'velit', 'non', 'consectetur' ], 'friends': [ { 'id': 0, 'name': 'Landry Leonard' }, { 'id': 1, 'name': 'Ross Hester' }, { 'id': 2, 'name': 'Megan Cummings' } ], 'greeting': 'Hello, Dixon Allen! You have 5 unread messages.', 'favoriteFruit': 'apple' }";

            MasterDAO masterDAO = this.getMasterDAO();
            masterDAO.create("generator", input);

        } catch (final Exception exception) {
            logger.error("/ROOT", exception);
            return "ERROR";

        } finally {

        }

        return "SUCCESS";
    }
}
