_______________________________________________________________________________
Spark POC on Applying different Transformations
_______________________________________________________________________________

./bin/spark-shell --packages org.apache.spark:spark-avro_2.11:2.4.3

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{DataType, StructType}
import org.apache.spark.sql.avro._
spark.sparkContext.hadoopConfiguration.set("avro.mapred.ignore.inputs.without.extension", "false")

val tab = "/hadoop/fs/path/aro/file"
val tabledata = spark.read.format("avro").load(s"$tab")

val a = tabledata.orderBy(desc("SRI_Number"))
val b = a.filter("SRI_Number == '1-2124718050'").select($"SRI_Number",$"Last_Activity_Date")

val df = groupBy($"SRI_Number").agg(max("Last_Activity_Date"))

df.show()

//Working with different date/time colums
c.withColumn("t2",col("t1").cast(DateType))
val d= b.withColumn("Last_Activity_Date",to_date($"Last_Activity_Date", "MM/dd/yyyy"))
d.groupBy($"SRI_Number").agg(max("Last_Activity_Date")).show

b.withColumn("Last_Activity_Date", to_date(unix_timestamp($"date", dd/MM/yyyy").cast("timestamp")))

val DF = Seq((1, "tomato",450)).toDF("ID","NAME","SAL")
val input="ipValue|0,newcol|''"

val col1="startDate"
df.withColumn(col1, date_format(,"yyyyMMddHHmmss")).show()
