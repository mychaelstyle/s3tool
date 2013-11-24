package com.mychaelstyle.aws.s3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mychaelstyle.aws.ConfigException;

public class Cli {

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        // regulate command options
        Map<String,String> options = new HashMap<String,String>();
        List<String> pathes = new ArrayList<String>();
        for(String arg : args){
            if(arg.startsWith("-")){
                String name = (arg.indexOf("=")>0) ? arg.substring(0,arg.indexOf("=")) : arg;
                String val  = (arg.indexOf("=")>0) ? arg.substring(arg.indexOf("=")+1) : arg;
                name = name.replaceAll("-", "").toLowerCase();
                options.put(name, val);
            } else {
                pathes.add(arg);
            }
        }

        try {
            if(options.containsKey("cp")){
                copy(pathes,options);
            }else if(options.containsKey("mv")){
                move(pathes,options);
            }
        } catch(RuntimeException e){
            System.err.println(e.getMessage());
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    private static boolean copy(List<String> pathes, Map<String,String> options) throws ConfigException {
        if(pathes.size()<2){
            System.err.println("File pathes is required!");
            return false;
        }
        String source = pathes.get(0);
        String dest   = pathes.get(1);
        if(source.indexOf(":")>0){
            // if source is in s3 transfer from remote to local
           String bucket = source.substring(0,source.indexOf(":"));
           String uri = source.substring(source.indexOf(":")+1);
           Transporter.get(Transporter.AP_NORTHEAST_1, bucket, uri, dest);
        } else {
            // if source is in local, transfer from local to remote
           String bucket = dest.substring(0,source.indexOf(":"));
           String uri = dest.substring(source.indexOf(":")+1);
           Transporter.put(new File(source), Transporter.AP_NORTHEAST_1, bucket, uri);
        }
        return true;
    }

    private static boolean move(List<String> pathes, Map<String,String> options) throws ConfigException {
      if(copy(pathes,options)){
        String source = pathes.get(0);
        String dest   = pathes.get(1);
        if(source.indexOf(":")>0){
          String bucket = dest.substring(0,source.indexOf(":"));
          String uri = dest.substring(source.indexOf(":")+1);
          Transporter.delete(Transporter.AP_NORTHEAST_1, bucket, uri);
        } else {
          new File(source).delete();
        }
        return true;
      }
      return false;
    }
}