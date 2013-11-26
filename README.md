s3tool
======

Amazon Web Service (AWS) Simple Storage Service (S3) command line tools.
created by Java.

This project is build using gradle.

## Included commands

* s3-ls
* s3-cp
* s3-mv
* s3-rm

## Usage

    $ # listing files
    $ s3-ls mys3bucket:example/files
    
    $ # copy a file local to s3
    $ s3-cp ./foo.png mys3bucket:foo.png
    
    $ # copy a file s3 to local
    $ s3-cp mys3bucket:foo.png ./foo.png
    
    $ # remove a file on s3
    $ s3-rm mys3bucket:foo.png

## Getting started

1. clone this project from github repository.
2. type 'gradle build' in this folder.
3. Path to bin folder in this folder.
4. set the following environment valuables.

### Environment variables

* AWS_ACCESS_KEY="Your Amazon Web Service access key"
* AWS_SECRET_KEY="Your Amazon Web Service access secret key"
* S3TOOL_REGION="AWS Region. e.g) AP_NORTHEAST_1 "

### Regions

* "GovCloud";
* "US_EAST_1";
* "US_WEST_1";
* "US_WEST_2";
* "EU_WEST_1";
* "AP_SOUTHEAST_1";
* "AP_SOUTHEAST_2";
* "AP_NORTHEAST_1";
* "SA_EAST_1";

## Development

you can use eclipse for the development.
type "gradle eclipse" in this folder and import from eclipse as a project.
