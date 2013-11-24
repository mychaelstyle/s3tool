/**
 * Amazon Web Service Utility class
 * @author Masanori Nakashima
 */
package com.mychaelstyle.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

/**
 * Amazon Web Service Utility class
 * 
 * This class require the following environment variables.<br>
 * このクラスを利用するには以下の環境変数の設定が必要です。<br>
 * <br>
 * AWS_ACCESS_KEY ... AWS APIへのアクセスキー<br>
 * AWS_SECRET_KEY ... AWS APIへのシークレットキー<br>
 
 * @author Masanori Nakashima
 */
public class Util {
    /** Environment variable name: AWS Access key */
    public static final String ENV_ACCESS_KEY = "AWS_ACCESS_KEY";
    /** Environment variable name: AWS Secret key */
    public static final String ENV_SECRET_KEY = "AWS_SECRET_KEY";
    /**
     * Constructor
     */
    private Util() {
    }
    /**
     * get AWSCredentials instance for connection
     * @return AWSCredentials
     */
    public static AWSCredentials getCredentials() throws ConfigException {
        String accessKey = System.getenv(ENV_ACCESS_KEY);
        String secretKey = System.getenv(ENV_SECRET_KEY);
        if(accessKey==null) throw new ConfigException("Environment varuable " + ENV_ACCESS_KEY + " is not found.");
        if(secretKey==null) throw new ConfigException("Environment varuable " + ENV_SECRET_KEY + " is not found.");
        return new BasicAWSCredentials(accessKey,secretKey);
    }
}
