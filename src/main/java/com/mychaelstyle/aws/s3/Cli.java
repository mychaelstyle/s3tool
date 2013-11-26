package com.mychaelstyle.aws.s3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mychaelstyle.aws.ConfigException;

public class Cli {

    /** command : copy */
    public static final String CMD_COPY = "cp";
    /** command : move */
    public static final String CMD_MOVE = "mv";
    /** command : remove */
    public static final String CMD_REMOVE = "rm";
    /** command : list */
    public static final String CMD_LIST = "ls";

    /** default region */
    public static final String ENV_REGION = "S3TOOL_REGION";
    /** default bucket */
    public static final String ENV_BUCKET = "S3TOOL_BUCKET";
    /** current region */
    public static final String ENV_CURRENT_REGION = "S3TOOL_CURRENT_REGION";
    /** current bucket */
    public static final String ENV_CURRENT_BUCKET = "S3TOOL_CURRENT_BUCKET";
    /** current uri prefix */
    public static final String ENV_CURRENT_URI_PREFIX = "S3TOOL_CURRENT_URI_PREFIX";

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
            if(options.containsKey(CMD_COPY)){
                copy(pathes,options);
            }else if(options.containsKey(CMD_MOVE)){
                move(pathes,options);
            }else if(options.containsKey(CMD_REMOVE)){
                remove(pathes,options);
            }else if(options.containsKey(CMD_LIST)){
                list(pathes,options);
            }
        } catch(RuntimeException e){
            System.err.println(e.getMessage());
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * copy command
     * 
     * @param pathes
     * @param options
     * @return
     * @throws ConfigException
     */
    public static boolean copy(List<String> pathes, Map<String,String> options)
            throws ConfigException {
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
           Transporter.get(getRegion(), bucket, uri, dest);
        } else if(dest.indexOf(":")>0){
            // if source is in local, transfer from local to remote
           String bucket = dest.substring(0,dest.indexOf(":"));
           String uri = dest.substring(dest.indexOf(":")+1);
           Transporter.put(new File(source), getRegion(), bucket, uri);
        }
        return true;
    }

    /**
     * mv command
     * 
     * @param pathes
     * @param options
     * @return
     * @throws ConfigException
     */
    public static boolean move(List<String> pathes, Map<String,String> options)
            throws ConfigException {
      if(copy(pathes,options)){
        String source = pathes.get(0);
        if(source.indexOf(":")>0){
          String bucket = source.substring(0,source.indexOf(":"));
          String uri = source.substring(source.indexOf(":")+1);
          Transporter.delete(getRegion(), bucket, uri);
        } else {
          new File(source).delete();
        }
        return true;
      }
      return false;
    }

    /**
     * rm command
     * 
     * @param pathes
     * @param options
     * @return
     * @throws ConfigException
     */
    public static boolean remove(List<String> pathes, Map<String,String> options)
            throws ConfigException{
      String source = pathes.get(0);
      if(source.indexOf(":")>0){
        String bucket = source.substring(0,source.indexOf(":"));
        String uri = source.substring(source.indexOf(":")+1);
        Transporter.delete(getRegion(), bucket, uri);
      } else {
        new File(source).delete();
      }
      return true;
    }

    /**
     * ls command
     * @param pathes
     * @param options
     * @throws Exception
     */
    public static void list(List<String> pathes, Map<String,String> options)
            throws Exception {
      String source = pathes.get(0);
      System.out.println("Files in the bucket : "+source+" -----");
      if(source.indexOf(":")>0){
        String bucket = source.substring(0,source.indexOf(":"));
        String uri = source.substring(source.indexOf(":")+1);
        List<FileInfo> files = Transporter.list(getRegion(), bucket,uri); 
        for(FileInfo info : files){
            StringBuffer buf = new StringBuffer(info.name);
            for(int num=info.name.length(); num<50; num++){
                buf.append(" ");
            }
            buf.append(info.size)
            .append("     ").append(info.date);
            System.out.println(buf.toString());
        }
      } else {
          throw new Exception("You must use a option [bucket]:[prefix]");
      }
    }

    private static Map<String,String> parseUri(String uri){
      Map<String,String> target = new HashMap<String,String>();
      if(uri.indexOf(":")>0){
          target.put("location", "remote");
          String[] elms = uri.split(":");

      } else {
          target.put("location", "local");
          target.put("path", uri);
      }
      return target;
    }
    /**
     * region
     * @return region name strings
     */
    private static String getRegion(){
        String region = System.getenv(ENV_CURRENT_REGION);
        if(null==region || 0==region.length()){
            region = System.getenv(ENV_REGION);
        }
        if(null==region || region.length()==0){
            return Transporter.AP_NORTHEAST_1;
        } else {
            return region;
        }
    }
}