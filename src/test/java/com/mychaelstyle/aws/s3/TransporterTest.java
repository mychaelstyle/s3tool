package com.mychaelstyle.aws.s3;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.mychaelstyle.aws.ConfigException;

/**
 * Test case for S3
 * This test requires following environment valuables.
 * 
 * <ul>
 * <li>AWS_ACCESS_KEY ... your access key for AWS.</li>
 * <li>AWS_SECRET_KEY ... your access secret key for AWS.</li>
 * </ul>
 * 
 * @author Masanori Nakashima
 */
public class TransporterTest {

    public static final String NAME_TEST_BUCKET = "test-bucket-mychaelstype-s3tools";
    public static final String NAME_TEST_FILE = "test.png";
    public static final String NAME_TEST_COPY = "g.png";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        // create bucket
        try {
            Transporter.createBucket(Transporter.AP_NORTHEAST_1, NAME_TEST_BUCKET);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            Transporter.deleteBucket(Transporter.AP_NORTHEAST_1, NAME_TEST_BUCKET);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        String localPath = "./"+NAME_TEST_COPY;
        String upUri     = NAME_TEST_FILE;

        // file upload
        File file = new File(NAME_TEST_FILE);
        try {
            Transporter.put(file, Transporter.TOKYO, NAME_TEST_BUCKET, upUri);
        } catch (ConfigException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        // ls
        try {
            List<FileInfo> files = Transporter.list(Transporter.TOKYO, NAME_TEST_BUCKET, "");
            assertTrue(files.size()==1);
            for(FileInfo info : files){
                System.out.println("name = " + info.name);
                System.out.println("type = " + info.type);
                System.out.println("size = " + info.size);
                System.out.println("date = " + info.date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        // file download
        File f = new File(localPath);
        try {
            Transporter.get(Transporter.TOKYO, NAME_TEST_BUCKET, upUri, localPath);
            assertTrue(f.exists());
        } catch (ConfigException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if(null!=f && f.exists()){
                f.delete();
            }
        }
        // remove
        try {
            Transporter.delete(Transporter.TOKYO, NAME_TEST_BUCKET, upUri);
        } catch (ConfigException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        // confirm
        try {
            Transporter.get(Transporter.TOKYO, NAME_TEST_BUCKET, upUri, localPath);
        } catch (Exception e) {
            // 存在しないので必ず例外発生
            e.printStackTrace();
        } finally {
            if(null!=f && f.exists()){
                f.delete();
            }
        }
    }
}
