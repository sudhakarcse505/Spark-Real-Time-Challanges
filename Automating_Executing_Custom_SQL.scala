NOTE: Working in Spark-Shall
------------------------------------
./bin/spark-shell --packages org.apache.spark:spark-avro_2.11:2.4.3

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{DataType, StructType}
import org.apache.spark.sql.avro._
spark.sparkContext.hadoopConfiguration.set("avro.mapred.ignore.inputs.without.extension", "false")


val table = List('TABLEA','TABLEB','TABLEC')

/**
* Below Method can Read the List of avro files and create tables
*/
val sparkTables=table.map(tab=> {
val tabledata=spark.read.format("avro").load(s"$tab")
val sqlTableName=tab.split("/").filter(_.contains('.'))(0).toString.split('.')(1).toString
tabledata.createOrReplaceTempView(s"$sqlTableName") 
println(s"$sqlTableName")
})

val sqlString=" Write Some SQL Using Above created tables"

val result_df =spark.sql(sqltxt)
result_df.show()
