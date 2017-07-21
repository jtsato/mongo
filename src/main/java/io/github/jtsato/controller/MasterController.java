package io.github.jtsato.controller;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by Jorge Takeshi Sato on 20/07/2017.
 */

@RestController
public class MasterController {

    private static final Logger logger = LoggerFactory.getLogger(MasterController.class);

    @RequestMapping
    public String create(
            @RequestParam("appId") String applicationId,
            @RequestParam("userId") String userId,
            @RequestParam("locale") String locale,
            @RequestParam("into") String collectionName,
            @RequestParam("value") String jsonDocument ) {

        return "Error";
    }

    @RequestMapping
    public String update(
            @RequestParam("appId") String applicationId,
            @RequestParam("userId") String userId,
            @RequestParam("locale") String locale,
            @RequestParam("target") String collectionName,
            @RequestParam("value") String jsonDocument ) {

        return "Error";
    }

    @RequestMapping
    public String delete(
            @RequestParam("appId") String applicationId,
            @RequestParam("userId") String userId,
            @RequestParam("locale") String locale,
            @RequestParam("from") String collectionName,
            @RequestParam("criteria") String jsonDocument ) {

        return "Error";
    }

    @RequestMapping
    public ArrayList<Document> search(
            @RequestParam("appId") String applicationId,
            @RequestParam("userId") String userId,
            @RequestParam("locale") String locale,
            @RequestParam("where") String collectionName,
            @RequestParam("criteria") String jsonDocument ) {

        return new ArrayList<>(0);
    }
}
