#!/bin/bash

START=$(date +%s)

mysql < drop.sql
mysql < create.sql

javac MySAX.java
java MySAX ebay_data/items-*.xml

mysql ad < load.sql

rm *.csv
rm *.class

END=$(date +%s)
DIFF=$(echo "$END - $START" | bc)
echo "Total runtime is $DIFF seconds"
