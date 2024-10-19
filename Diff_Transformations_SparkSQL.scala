Spark POC's on Applying different Transformations
-------------------------------------------------------------------------------
./bin/spark-shell --packages org.apache.spark:spark-avro_2.11:2.4.3,org.mongodb.spark:mongo-spark-connector_2.11:2.4.2
import com.mongodb.spark.config.{ReadConfig, WriteConfig}
import com.mongodb.spark.sql._
import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.types.{DataType, StructType}
import org.apache.spark.sql.avro._
spark.sparkContext.hadoopConfiguration.set("avro.mapred.ignore.inputs.without.extension", "false")


val a=spark.read.format("avro").load("/edp/PMOFiles/edp.PMOFullExport/20200824/PMOFullExport_2020_08_23_213015EDPXL_GamingSystemsProjectDashboard.r.csv_20200824054335499_TBR_PD")
val tab="/edp/SRTrendAnalysis/ExcelAvro/edp.Service_Request_Escalation_Report/20200902/*_TBR_PD"
val tab="/edp/SRTrendAnalysis/ExcelAvro/edp.SR_Summary_Rpt_Global_Support_DBA_L2_Final/20200717/*_TBR_PD"
val tab="/edp/haphak/schema/uws.transactions"
val a=spark.read.format("avro").load(s"$tab")

val tab="/edp/SRTrendAnalysis/ExcelAvro/edp.Service_Request_Escalation_Report/20200726/*_TBR_PD"
val tabledata=spark.read.format("avro").load(s"$tab")
tabledata.count
tabledata.select($"SR_Num").show

val mongodata=spark.read.mongo(ReadConfig(Map("uri" -> "mongodb://user:pwd@xxxxx:27017/SRReportDB?authSource=admin","collection"->"SRTrendAnalysis")))

val tab="/edp/SRTrendAnalysis/ExcelAvro/edp.Service_Request_Escalation_Report/20200902/*_TBR"

val tabledata=spark.read.format("avro").load(s"$tab")

tabledata.createOrReplaceTempView("Service_Request_Escalation_Report")
mongodata.createOrReplaceTempView("SRTrendAnalysis")

//Single line query
val q="Select a.SR_Num,Service_Ticket,Summary,SR_Details,Account,Child_Account,Region,Originating_Group,
Destination_Group,date_format(to_timestamp(replace(Closed,'\\\\',''),'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') AS Closed
,Days_Open,Product,Release,Area,SubArea,Priority,Severity,Severity_Impacting,SR_Complexity,Customer_Priority,Resource_1,Manager_Work_Assignment,
Manager_Work_Assignment_Comments,a.Group_Name,SR_Type,Substatus,Resolution,P1_P2_Resolution,SAM_Approved,
date_format(to_timestamp(replace(SAM_Approved_Date,'\\\\',''),'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') AS SAM_Approved_Date,
Description,date_format(to_timestamp(replace(Last_Activity_Date,'\\\\',''),'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') AS Last_Activity_Date,Last_UpdatedBy,
Owner_First_Name,Owner_Last_Name,Activity_Type,Targeted_Fix,Issue_Category,Comments,Current_Status,Closed_By,
date_format(to_timestamp(replace(Customer_Closed_Date,'\\\\',''),'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') AS Customer_Closed_Date,
Customer_Closed_By,Strategic_Account,Case when Status = 'Pending' Then 'Opened' when status = 'Closed' then 'Closed' Else 'Opened' End as Status,
Case when coalesce(mongo.Functional_Area,'') = '' Then '' Else mongo.Functional_Area End AS Functional_Area, 
Case when coalesce(mongo.Root_Cause,'') = '' Then '' Else mongo.Root_Cause End AS Root_Cause, 
Case when coalesce(mongo.Owner,'') = '' Then '' Else mongo.Owner End AS Owner, 
Case when coalesce(mongo.Dev_Team_Info,'NA') = 'NA' Then 'NA' Else mongo.Dev_Team_Info End AS Dev_Team_Info, 
Case when coalesce(mongo.Support_Team_Info,'NA') = 'NA' Then 'NA' Else mongo.Support_Team_Info End AS Support_Team_Info, 
date_format(to_timestamp(current_timestamp(),'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') AS Updated_Datetime,
date_format(to_timestamp(replace(SR_Created_Date,'\\\\',''),'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') AS SR_Created_Date,
date_format(to_timestamp(replace(a.Date_Time_of_Escalation,'\\\\',''),'yyyy-MM-dd HH:mm:ss'),'yyyy-MM-dd HH:mm:ss') AS Date_Time_of_Escalation from  
edp_Service_Request_Escalation_Report  as a inner join (Select SR_Num,Max(Date_Time_of_Escalation) as Date_Time_of_Escalation from  
edp_Service_Request_Escalation_Report  group by SR_Num) as b on a.SR_Num = b.SR_Num AND a.Date_Time_of_Escalation = b.Date_Time_of_Escalation 
Left join (select SR_Num,Functional_Area,Root_Cause,Owner,Dev_Team_Info,Support_Team_Info FROM  SRTrendAnalysis ) mongo ON mongo.SR_Num=b.SR_Num"

val res=spark.sq(q)
res.select($"SR_Created_Date",$"Date_Time_of_Escalation",$"Last_Activity_Date",$"SAM_Approved_Date").show

val a=tabledata.orderBy(desc("SR_Num"))
val b=a.filter("SR_Num == '1-2124718050'").select($"SR_Num",$"Last_Activity_Date")
b.groupBy($"SR_Num").agg(max("Last_Activity_Date")).show

c.withColumn("t2",col("t1").cast(DateType))
val d= b.withColumn("Last_Activity_Date",to_date($"Last_Activity_Date", "MM/dd/yyyy"))
d.groupBy($"SR_Num").agg(max("Last_Activity_Date")).show()

b.withColumn("Last_Activity_Date", to_date(unix_timestamp($"date", dd/MM/yyyy").cast("timestamp"))).show()
