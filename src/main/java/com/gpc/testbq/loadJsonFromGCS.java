package com.gpc.testbq;

import com.google.cloud.bigquery.*;
import com.google.cloud.bigquery.storage.v1.TableFieldSchema;

// Sample to load JSON data from Cloud Storage into a new BigQuery table
public class loadJsonFromGCS {

    public static boolean runLoadJsonFromGCS(String fileName, String bucketName, String tableName) {

        String datasetName = "LoadJSON";
        //String tableName = "FinalTable";
        String storageprefix= "gs://";
        String Location = bucketName +"/"+ fileName;
        String sourceUri = storageprefix. concat(Location); //"gs://testcloudfunction3subhadra/testbq2.json";
        Schema schema =
                Schema.of(
                        Field.of("source", StandardSQLTypeName.STRING),
                        Field.of("checksum", StandardSQLTypeName.INT64),
                        Field.of("data", StandardSQLTypeName.JSON),
                        Field.of("operation_type", StandardSQLTypeName.STRING),
                        Field.of("correlation_id", StandardSQLTypeName.STRING),
                        Field.of("primary_key_checksum", StandardSQLTypeName.STRING),
                        Field.of("source_create_date", StandardSQLTypeName.STRING),
                        Field.of("source_update_date", StandardSQLTypeName.STRING),
                        Field.newBuilder("primary_key_columns_names", StandardSQLTypeName.STRING)
                                        .setMode(Field.Mode.REPEATED)
                                        .build()




                );


        return loadJsonFromGCS(datasetName, tableName, sourceUri, schema);
    }

    public static boolean  loadJsonFromGCS(String datasetName, String tableName, String sourceUri, Schema schema) {
        Boolean jobStat = false;
        try {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests.
            BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();

            TableId tableId = TableId.of(datasetName, tableName);
            LoadJobConfiguration loadConfig =
                    LoadJobConfiguration.newBuilder(tableId, sourceUri)
                            .setFormatOptions(FormatOptions.json())
                            .setSchema(schema)
                            .build();

            // Load data from a GCS JSON file into the table
            Job job = bigquery.create(JobInfo.of(loadConfig));
            // Blocks until this load table job completes its execution, either failing or succeeding.
            job = job.waitFor();
           // String Status = job.getStatus().getState().name();

            if (job.getStatus().getError() != null) {
                System.out.println("BigQuery was not able to load into the table due to an error:" + job.getStatus().getError());

                jobStat = false;
            } else {
                System.out.println("BigQuery was able to load into the table1 :");

                jobStat =  true;
            }

        } catch (BigQueryException | InterruptedException e) {
                 System.out.println("Column not added during load append \n" + e.toString());
                jobStat =  false;
                   }
        return jobStat;
    }
}