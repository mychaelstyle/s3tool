/**
 * Configuration exception
 * @author Masanori Nakashima
 * 
 */
package com.mychaelstyle.aws;

/**
 * Configuration exception
 * @author Masanori Nakashima
 */
public class ConfigException extends Exception {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param string
     */
    public ConfigException(String string) {
        super(string);
    }

}
