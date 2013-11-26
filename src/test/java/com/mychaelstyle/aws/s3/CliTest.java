/**
 * 
 */
package com.mychaelstyle.aws.s3;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author masanori
 *
 */
public class CliTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // create bucket
        try {
            Transporter.createBucket(Transporter.AP_NORTHEAST_1, TransporterTest.NAME_TEST_BUCKET);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        try {
            Transporter.deleteBucket(Transporter.AP_NORTHEAST_1, TransporterTest.NAME_TEST_BUCKET);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testCopy() {
        List<String> pathes = new ArrayList<String>();
        pathes.add(TransporterTest.NAME_TEST_FILE);
        pathes.add(TransporterTest.NAME_TEST_BUCKET+":"+TransporterTest.NAME_TEST_FILE);
        Map<String,String> opts = new HashMap<String,String>();
        opts.put("cp", "cp");
        try {
            // upload
            Cli.copy(pathes,opts);
            String localPath = "./"+TransporterTest.NAME_TEST_COPY;
            File result = new File(localPath);
            Transporter.get(TransporterTest.NAME_TEST_BUCKET, TransporterTest.NAME_TEST_FILE, localPath);
            assertTrue(result.exists());
            // move upload
            pathes = new ArrayList<String>();
            pathes.add(TransporterTest.NAME_TEST_COPY);
            pathes.add(TransporterTest.NAME_TEST_BUCKET+":"+TransporterTest.NAME_TEST_COPY);
            opts = new HashMap<String,String>();
            opts.put("mv", "mv");
            Cli.move(pathes, opts);
            File localCopy = new File(TransporterTest.NAME_TEST_COPY);
            assertFalse(localCopy.exists());
            // list
            pathes = new ArrayList<String>();
            pathes.add(TransporterTest.NAME_TEST_BUCKET+":");
            opts = new HashMap<String,String>();
            opts.put("ls", "ls");
            Cli.list(pathes, opts);
            // remove org uploaded
            pathes = new ArrayList<String>();
            pathes.add(TransporterTest.NAME_TEST_BUCKET+":"+TransporterTest.NAME_TEST_FILE);
            opts = new HashMap<String,String>();
            opts.put("rm", "rm");
            Cli.remove(pathes, opts);
            // remove copy
            pathes = new ArrayList<String>();
            pathes.add(TransporterTest.NAME_TEST_BUCKET+":"+TransporterTest.NAME_TEST_COPY);
            Cli.remove(pathes, opts);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
