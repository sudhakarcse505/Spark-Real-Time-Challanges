DataSkew Issue - When the data is unevenly distributed and Some partitions has less data and Some partitions has huge data.
                 - When you do any Join or Aggregation Operations, huge data partition tasks can take long time and burden to CPU
                 - Salting Technique is Good to Solve this Issue by Evenly Distribute data using some Random values appending to Key columns
				 
  Salting Formula- 1.Find the Biggest Partitions/ or more duplicate Key in BigTable
                   2.Append _<randomValues> -> RandomeValues Limit of 10, So that this bigPartition can split as 10 different Partitions/
				   3.Duplicate The Seconds Tables Keys with 10 times Since we have splitted to 10 Parititions in same way of Salt key on above table
				   4.Apply Join Using Two Salted Keys from Two Tables
				   5.Use split(salted_key, '_')[0] to Get the Original Values
				   
  
	val df1 = Seq((1,"a"),(2,"b"),(1,"c"),(1,"x"),(1,"y"),(1,"g"),(1,"k"),(1,"u"),(1,"n")).toDF("ID","NAME") 
	df1.createOrReplaceTempView("fact")

	var df2 = Seq((1,10),(2,30),(3,40)).toDF("ID","SALARY")
	df2.createOrReplaceTempView("dim")

	val salted_df1 = spark.sql("""select concat(ID, '_', FLOOR(RAND(123456)*19)) as salted_key, NAME from fact """) //split common col to 19 uniqKeys
	salted_df1.createOrReplaceTempView("salted_fact")

	val exploded_dim_df = spark.sql(""" select ID, SALARY, explode(array(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19)) as salted_key from dim""")
	//val exploded_dim_df = spark.sql(""" select ID, SALARY, explode(array(0 to 19)) as salted_key from dim""")
	exploded_dim_df.createOrReplaceTempView("salted_dim")

	val result_df = spark.sql("""select split(fact.salted_key, '_')[0] as ID, dim.SALARY 
                                from salted_fact fact LEFT JOIN salted_dim dim 
                                ON fact.salted_key = concat(dim.ID, '_', dim.salted_key) """)
