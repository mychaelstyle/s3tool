/**
 * 
 */
package com.mychaelstyle.aws;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentials;

/**
 * @author masanori
 *
 */
public class UtilTest {

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
    public void test() {
        // 設定なし
        AWSCredentials credentials = null;
        try {
            credentials = Util.getCredentials();
            assertNotNull(credentials);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
