# AppliedDatabases

# ASSIGNMENT 1: DATA LOADING

The purpose of this assignment is to design a database schema for a given dataset, shared XML data into csv-files, and
create tables in a database and populate them via the csv-files.
The dataset consists of ebay auction data, given by a set of XML files. 
Each XML file is valid with respect to the given DTD. 
For Point (1) you will work out a convenient relational schema to represent the given auction XML data. 
This includes identifying the primary keys of each table, and explaining why the schema is "good", i.e., 
why it adheres to the desired normal forms of database design. Point (2) is the main part of this assignment. 
You are asked to write a Java program that reads in the XML files, and writes out csv-files, one for each table in 
your schema. In Point (3) you are asked to write SQL-scripts which create your tables in a MySQL database, and which 
load the csv-files into these tables. Finally, you are asked to test your database by running a few SQL-queries over it.
