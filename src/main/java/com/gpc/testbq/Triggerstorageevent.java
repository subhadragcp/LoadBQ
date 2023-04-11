


package com.gpc.testbq;

// [START functions_cloudevent_storage]
import com.google.cloud.functions.CloudEventsFunction;
import com.google.gson.Gson;
import com.gpc.testbq.eventpojos.GcsEvent;
import io.cloudevents.CloudEvent;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Triggerstorageevent implements CloudEventsFunction {
    private static final Logger logger = Logger.getLogger(Triggerstorageevent.class.getName());

    @Override
    public void accept(CloudEvent event) {
        logger.info("Event: " + event.getId());
        logger.info("Event Type: " + event.getType());
        String fileName = "";
        String bucketName = "";
        String tableName ="";

        if (event.getData() != null) {
            String cloudEventData = new String(event.getData().toBytes(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            GcsEvent gcsEvent = gson.fromJson(cloudEventData, GcsEvent.class);
            fileName = gcsEvent.getName();
            bucketName = gcsEvent.getBucket();
            tableName="";

            logger.info("Bucket: " + gcsEvent.getBucket());
            logger.info("File: " + gcsEvent.getName());
            logger.info("Metageneration: " + gcsEvent.getMetageneration());
            logger.info("Created: " + gcsEvent.getTimeCreated());
            logger.info("Updated: " + gcsEvent.getUpdated());
        }
        loadJsonFromGCS bqobj = new loadJsonFromGCS();
        bqobj.runLoadJsonFromGCS(fileName, bucketName,tableName);
        System.out.println("BQObj created!");
    }
}

// [END functions_cloudevent_storage]
