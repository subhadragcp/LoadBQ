package com.gpc.testbq;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


public class ReadFiles {
    public static void listObjects(String projectId, String bucketName,String directoryPrefix) {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(directoryPrefix), Storage.BlobListOption.currentDirectory());
        String tableName = "abc";

        loadJsonFromGCS index=new loadJsonFromGCS();

        for (Blob blob : blobs.iterateAll()) {
            System.out.println(blob.getName());
            boolean bqJobStatTab1=  index.runLoadJsonFromGCS(blob.getName(),bucketName, tableName);
            if (bqJobStatTab1) {

                    MoveToArchive mvObjArch =new MoveToArchive();
                    mvObjArch.moveObjectArch("my-kubernetes-project-364702","testcloudfunction3subhadra","test6.txt","functionlistsubhadra","test6.txt");
                } else {
                System.out.println("Move to Error");
                MoveToError mvobj =new MoveToError();
                mvobj.moveObject("my-kubernetes-project-364702","testcloudfunction3subhadra","test6.txt","functionlistsubhadra","test6.txt");

            }
            }

            }


        }


