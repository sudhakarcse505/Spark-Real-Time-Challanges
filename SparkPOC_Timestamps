park POC On Timestamp Conversions
----------------------------------------------------------------------
val dt = Seq("2019-03-16T16:54:42.968Z").toDF("ts_str")
dt.withColumn("ts_str",to_timestamp('ts_str,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).show

import org.apache.spark.sql.functions.{to_date, to_timestamp}

val dt1 = Seq("20200810080247000").toDF("ts")
val dt2 = Seq("20200810080247").toDF("ts")
val res= dt2.withColumn("ts2", date_format(to_timestamp($"ts","yyyyMMddHHmmss"),"MM-yyyy-dd HH"))

date_format- can convert date to required format and returns in stringtype
to_timestamp- can convert the string/date/timestamp values to standard format
