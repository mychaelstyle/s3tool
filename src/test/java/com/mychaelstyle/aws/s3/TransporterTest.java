package com.mychaelstyle.aws.s3;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mychaelstyle.aws.ConfigException;

public class TransporterTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    public static final String NAME_TEST_BUCKET = "test-bucket-mychaelstype-s3tools";
    public static final String NAME_TEST_FILE = "test.png";
    public static final String NAME_TEST_COPY = "g.png";

    @Test
    public void test() {
        String localPath = "./"+NAME_TEST_COPY;
        String upUri     = NAME_TEST_FILE;

        // create bucket
        try {
            Transporter.createTable(Transporter.AP_NORTHEAST_1, NAME_TEST_BUCKET);
        } catch(Exception e){
            e.printStackTrace();
        }

        // file upload
        File file = new File(NAME_TEST_FILE);
        try {
            Transporter.put(file, Transporter.TOKYO, NAME_TEST_BUCKET, upUri);
        } catch (ConfigException e) {
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
            e.printStackTrace();
        } finally {
            if(null!=f && f.exists()){
                f.delete();
            }
        }
    }
}
