RocksDB Statestore Implimentation In SparkSQL Streaming in Dockers
--------------------------------------------------------------------------------------------------------------------
User Note - I'm Running my Spark and Hadoop Cluster in Docker environment

RocksDB Statestore Implimentation In SparkSQL Streaming If you have more StateStore data which can't handle by HDFS
Advantage - Can Handle millions of Key, Values in ROCKS DB easily with its cache mechanism

   --package com.qubole.spark/spark-rocksdb-state-store_2.11/1.0.0
   --conf spark.sql.streaming.stateStore.providerClass = org.apache.spark.sql.execution.streaming.state.RocksDbStateStoreProvider
   --conf spark.sql.streaming.stateStore.rocksDb.localDir=<tmp-path>

Error:- java.lang.unsatisfiedlinkerror /tmp/librocksdbjni error Or Class org.rockdb. class not found
this meaning RocksDB native libraries are not available 

Steps To Resolve this Error:-
------------------------------
Step1: add the Native rockdb and rockdbconnector add in Spark-Program jar in both Dependencies.scala file
Step2: Build the Jar and Use this jar
step3: Check the Temp direcoty location (JVM) in the spark container or node manager container.Use the below command to checkpoint
    java -XshowSettings
	  check this path->java.io.tmpdir
	  IT SHOULD HAVE /tmp as VALUE. or Else PLEASE CHANGE HERE.
	  you can also set this path during spark-submit or in spark code
    
step4: Give full permissions to that path-
NOTE:- Below step is not nessary

step5: create one folder name "rocksdb" with full permissions, give that folder in (spark.sql.streaming.stateStore.rocksDb.localDir=<tmp path> )
Step6 :Run spark submit in Yarn Mode only,So that yarn, Driver can access one /tmp path and Executor access another /tmp path

NOTE:- Do not run in Local mode-since, driver and Executor uses the same JVM [RocksDB can't run]

VERY-Important:-SYSTEM/Docker Container SHOULD HAVE "java.io.tmpdir=/tmp" and SPARK SHOULD RUN IN YARN, to use ROCKSDB

NOTE:-Checkpoint directory can store the RockDB connection information as well, So make sure use only one connection while restarting
      If no RockDB used before, we can run Job with any connector


	spark-submit --class xxx.drivers.StreamJobDriver \
	--packages org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.3,com.qubole.spark/spark-rocksdb-state-store_2.11/1.0.0 \
	--master Yarn --deploy-mode client --driver-memory 6G \
	--conf "spark.sql.crossJoin.enabled=true" \
	--conf "spark.sql.streaming.stateStore.providerClass=org.apache.spark.sql.execution.streaming.state.RocksDbStateStoreProvider" \
	--conf "spark.sql.streaming.stateStore.rocksDb.localDir=file:///opt/spark/RockDB/db" ./spark-dataTransformation-2.1.1.jar \
	hdfs://namenode:9000/xxxx.json

  
   Reference--
   https://docs.qubole.com/en/latest/user-guide/engines/spark/structured-streaming.html
   https://github.com/PingHao/spark-statestore-rocksdb
   https://github.com/qubole/spark-state-store


Help Links:-
-------------
https://databricks.com/blog/2020/12/16/a-step-by-step-guide-for-debugging-memory-leaks-in-spark-applications.html
https://unraveldata.com/common-failures-slowdowns-part-ii/
https://medium.com/@Iqbalkhattra85/optimize-spark-structured-streaming-for-scale-d5dc5dee0622
