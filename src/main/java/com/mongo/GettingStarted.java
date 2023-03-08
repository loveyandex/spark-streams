package com.mongo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

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
        .config("spark.mongodb.read.connection.uri", "mongodb://127.0.0.1/test.myCollection")
        .config("spark.mongodb.write.connection.uri", "mongodb://127.0.0.1/test.myCollection")
        .getOrCreate();

    Dataset<Row> implicitDS = spark.read().format("mongodb").load();
    implicitDS.printSchema();
    implicitDS.show();

    // Application logic

  }
}
