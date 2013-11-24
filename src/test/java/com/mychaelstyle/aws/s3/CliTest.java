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

import com.mychaelstyle.aws.ConfigException;

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
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCopy() {
        List<String> pathes = new ArrayList<String>();
        pathes.add(TransporterTest.NAME_TEST_FILE);
        pathes.add(TransporterTest.NAME_TEST_BUCKET+":"+TransporterTest.NAME_TEST_FILE);
        Map<String,String> opts = new HashMap<String,String>();
        opts.put("cp", "cp");
        try {
            Cli.copy(pathes,opts);
            String localPath = "./"+TransporterTest.NAME_TEST_COPY;
            File result = new File(localPath);
            Transporter.get(TransporterTest.NAME_TEST_BUCKET, TransporterTest.NAME_TEST_FILE, localPath);
            assertTrue(result.exists());
            result.delete();
            // remove
            pathes = new ArrayList<String>();
            pathes.add(TransporterTest.NAME_TEST_BUCKET+":"+TransporterTest.NAME_TEST_FILE);
            opts = new HashMap<String,String>();
            opts.put("rm", "rm");
            Cli.remove(pathes, opts);
        } catch (ConfigException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testMove() {
        File local = new File(TransporterTest.NAME_TEST_FILE);
    }
}
