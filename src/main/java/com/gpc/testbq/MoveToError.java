package com.gpc.testbq;

        import com.google.cloud.storage.BlobId;
        import com.google.cloud.storage.Blob;
        import com.google.cloud.storage.Storage;
        import com.google.cloud.storage.StorageOptions;

public class MoveToError {
    public static void moveObject(
            String projectId,
            String sourceBucketName,
            String sourceObjectName,
            String targetBucketName,
            String targetObjectName) {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String sourceObjectName = "your-object-name";

        // The ID of the bucket to move the object objectName to
        // String targetBucketName = "target-object-bucket"

        // The ID of your GCS object
        // String targetObjectName = "your-new-object-name";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId source = BlobId.of(sourceBucketName, sourceObjectName);
        BlobId target = BlobId.of(targetBucketName, targetObjectName);

        // Optional: set a generation-match precondition to avoid potential race
        // conditions and data corruptions. The request returns a 412 error if the
        // preconditions are not met.
        Storage.BlobTargetOption precondition;
        if (storage.get(targetBucketName, targetObjectName) == null) {
            // For a target object that does not yet exist, set the DoesNotExist precondition.
            // This will cause the request to fail if the object is created before the request runs.
            precondition = Storage.BlobTargetOption.doesNotExist();
        } else {
            // If the destination already exists in your bucket, instead set a generation-match
            // precondition. This will cause the request to fail if the existing object's generation
            // changes before the request runs.
            precondition =
                    Storage.BlobTargetOption.generationMatch(
                            storage.get(targetBucketName, targetObjectName).getGeneration());
        }

        // Copy source object to target object
        storage.copy(
                Storage.CopyRequest.newBuilder().setSource(source).setTarget(target, precondition).build());
        Blob copiedObject = storage.get(target);
        // Delete the original blob now that we've copied to where we want it, finishing the "move"
        // operation
        storage.get(source).delete();

        System.out.println(
                "Moved object " + sourceObjectName + " from bucket " + sourceBucketName + " to " + targetObjectName + " in bucket " + copiedObject.getBucket());
    }
}