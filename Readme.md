# Client-Server Database

A thorough design and experiment of

## Description

The project can be substantially decompose to three major sections:
* The first section is the Graphic User Interface (GUI) which creates with Java Swing and AWT packages and responsible for interacting with users’ demands and queries. 
* The second is the backend enginee which is hosted on MySQL database schema. The database we use, adventureworks, is offered by Professor Yunsock Choe. 
* The last section is the Dashboard specifically designed for busiess analysis with support from XChart and maven. 

## Getting Started
### Installing

1. download this repo:
2. install docker and run the daemon 
 * method may vary, depending on OS
 * see https://docs.docker.com/get-docker/
3. install docker-compose 
 * method may vary, depending on OS
 * see https://docs.docker.com/compose/install/
4. in the directory, run:
```
docker-compose up -d
```
5. now you can access the web server on http://localhost:5555
6. Note: the web site root directory is ./public/ 
  * files in the above directory will be visible at http://localhost:5555
  * edits in your public/ directory in your host filesystem will immediately become visible on the web site.
7. to stop and remove the instances from the docker runtime, run (in the top directory of docker):
```
docker-compose stop 
```
    
Credits: https://phpdocker.io/generator was used to seed this 

### Executing program

* How to run the program
* cd to ./java directory
```
cd java
make
java Login
```

## Authors

Contributors names and contact info
* Zengxiaoran Kang
* Yifei Liang
* Yuqi Sun
* Taowei Ji

## Acknowledgments

Inspiration, code snippets, etc.
1. BidirectionalTables-https://www.geeksforgeeks.org/graph-and-its-representations/
2. SQLQueryjdb-stats-https://www.geeksforgeeks.org/calculate-median-in-mysql/
3. DesignDocumentTemplate-
https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&ved=2ahUKEwjxkI-20L_sAhVJ2qwKHbgyAkMQFjATegQIChAC&url=https%3A%2F%2Fwww.cms.gov%2Fresearch-statistics-data-and-systems%2Fcms-information-technology%2Fxlc%2Fdownloads%2Fsystemdesigndocument.docx%23%3A~%3Atext%3DThe%2520System%2520Design%2520Document%2520(SDD)%2520describes%2520how%2520the%2520functional%2520and%2Cdocumented%2520in%2520the%2520Logical%2520Data&usg=AOvVaw0K6mjaN8CPeX1m6KaQlqZX
4. DTBTablePrinter-https://github.com/htorun/dbtableprinter
5. SQLTutorial-https://www.w3schools.com/sql/default.asp
6. AdventureWorksSandbox-https://sqlzoo.net/wiki/AdventureWorks
7. JavaSwingTutorial-https://www.youtube.com/watch?v=HXV3zeQKqGY&t=6619s
8. jsplitpanel-https://stackoverflow.com/questions/1879091/jsplitpane- setdividerlocation-problem
9. TabbedPanel-https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
10.Xchart implementation - https://knowm.org/open-source/xchart/xchart-example-code/
