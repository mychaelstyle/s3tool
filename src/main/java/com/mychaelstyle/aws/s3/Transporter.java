package com.mychaelstyle.aws.s3;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mychaelstyle.aws.ConfigException;

import static com.mychaelstyle.aws.Util.*;

public class Transporter {
    public static final String GovCloud = "GovCloud";
    public static final String US_EAST_1  = "US_EAST_1";
    public static final String US_WEST_1 = "US_WEST_1";
    public static final String US_WEST_2 = "US_WEST_2";
    public static final String EU_WEST_1 = "EU_WEST_1";
    public static final String AP_SOUTHEAST_1 = "AP_SOUTHEAST_1";
    public static final String AP_SOUTHEAST_2 = "AP_SOUTHEAST_2";
    public static final String AP_NORTHEAST_1 = "AP_NORTHEAST_1";
    public static final String SA_EAST_1 = "SA_EAST_1";

    public static final String TOKYO = AP_NORTHEAST_1;

    public static final Map<String,Regions> REGIONS = new HashMap<String,Regions>(){
        private static final long serialVersionUID = 1L;
        {
        put(GovCloud,       Regions.GovCloud);
        put(US_EAST_1,      Regions.US_EAST_1);
        put(US_WEST_1,      Regions.US_WEST_1);
        put(US_WEST_2,      Regions.US_WEST_2);
        put(EU_WEST_1,      Regions.EU_WEST_1);
        put(AP_SOUTHEAST_1, Regions.AP_SOUTHEAST_1);
        put(AP_SOUTHEAST_2, Regions.AP_SOUTHEAST_2);
        put(AP_NORTHEAST_1, Regions.AP_NORTHEAST_1);
        put(SA_EAST_1,      Regions.SA_EAST_1);
        }
    };

    /** Amazon S3 client instance */
    private static Map<String,AmazonS3Client> clientMap = new HashMap<String,AmazonS3Client>();
    /** Region */
    private static String lastRegion = AP_NORTHEAST_1;

    /**
     * get S3 client
     * @param String regionName e.g) AP_NORTHEAST_1
     * @return AmazonS3Client
     */
    protected static AmazonS3Client getClient(String regionName) throws ConfigException {
        lastRegion = regionName;
        Regions r = REGIONS.get(regionName);
        Region region = Region.getRegion(r);
        if(!clientMap.containsKey(regionName)){
            AWSCredentials credentials = getCredentials();
            AmazonS3Client client = new AmazonS3Client(credentials);
            client.setRegion(region);
            clientMap.put(regionName, client);
        }
        return clientMap.get(regionName);
    }

    /**
     * get file
     * @param region
     * @param bucket
     * @param uri
     * @param localPath
     * @throws ConfigException 
     */
    public static void get(String region, String bucket, String uri, String localPath) throws ConfigException{
        AmazonS3Client client = getClient(region);
        GetObjectRequest request = new GetObjectRequest(bucket, uri);
        File file = new File(localPath);
        client.getObject(request, file);
    }

    public static void get(String bucket, String uri, String localPath) throws ConfigException{
        get(lastRegion,bucket,uri,localPath);
    }

    /**
     * private upload
     * @param file
     * @param region
     * @param bucket
     * @param uri
     * @throws ConfigException 
     */
    public static void put(File file, String region, String bucket, String uri) throws ConfigException {
        try {
            delete(region,bucket,uri);
        } catch(Exception e){
            // through
        }
        AmazonS3Client client = getClient(region);
        PutObjectRequest request = new PutObjectRequest(bucket,uri,file);
        client.putObject(request);
    }

    public static void put(File file, String bucket, String uri) throws ConfigException {
        put(file,lastRegion,bucket,uri);
    }

    /**
     * public access upload
     * @param file
     * @param region
     * @param bucket
     * @param uri
     * @throws ConfigException 
     */
    public static void pub(File file, String region, String bucket, String uri) throws ConfigException{
        try {
            delete(region,bucket,uri);
        } catch(Exception e){
            // through
        }
        AmazonS3Client client = getClient(region);
        PutObjectRequest request = new PutObjectRequest(bucket,uri,file);
        request.withCannedAcl(CannedAccessControlList.PublicRead);
        client.putObject(request);
    }

    public static void pub(File file, String bucket, String uri) throws ConfigException{
        pub(file,lastRegion,bucket,uri);
    }

    /**
     * delete object
     * @param region
     * @param bucket
     * @param uri
     * @throws ConfigException 
     */
    public static void delete(String region, String bucket, String uri) throws ConfigException{
        AmazonS3Client client = getClient(region);
        DeleteObjectRequest request = new DeleteObjectRequest(bucket,uri);
        client.deleteObject(request);
    }

    /**
     * create bucket
     * @param region
     * @param tableName
     * @throws ConfigException
     */
    public static void createTable(String region, String tableName) throws ConfigException {
        AmazonS3Client client = getClient(region);
        client.createBucket(new CreateBucketRequest(tableName));
    }
}
