package com.mongo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.*;


public final class GettingStarted {

  public static void main(final String[] args) throws InterruptedException {
    /*
     * Create the SparkSession.
     * If config arguments are passed from the command line using --conf,
     * parse args for the values to set.
     */
    SparkSession spark = SparkSession.builder()
        .master("local")
        .appName("MongoSparkConnectorIntro")
        .config("spark.mongodb.read.connection.uri", "mongodb://127.0.0.1/matching-engine.orders")
        .config("spark.mongodb.write.connection.uri", "mongodb://127.0.0.1/matching-engine.orders")
        .getOrCreate();

    Dataset<Row> implicitDS = spark.read().format("mongodb").load();

    System.out.println("---------------------------bfore orderby------------------------");
    Dataset<Row> orderBy = implicitDS.orderBy(org.apache.spark.sql.functions.col("amount").desc());

    orderBy.persist();

    
    implicitDS.printSchema();
    implicitDS.show();
    
    // Application logic
    orderBy.show();
    
    orderBy.select(org.apache.spark.sql.functions.col("amount")).agg(sum("amount").as("sum")).show();

  }
}
