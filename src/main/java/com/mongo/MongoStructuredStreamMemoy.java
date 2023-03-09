package com.mongo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;

import static org.apache.spark.sql.functions.*;

import java.util.concurrent.TimeoutException;

public final class MongoStructuredStreamMemoy {

  public static void main(final String[] args) throws TimeoutException, StreamingQueryException {
    /*
     * Create the SparkSession.
     * If config arguments are passed from the command line using --conf,
     * parse args for the values to set.
     */
    SparkSession spark = SparkSession.builder()
        .master("local")
        .appName("jisodjsoidj")
        .config("spark.mongodb.read.connection.uri", "mongodb://127.0.0.1:27117,127.0.0.1:27118/matching-engine.orders")
        // .config("spark.mongodb.write.connection.uri",
        // "mongodb://127.0.0.1/matching-engine.orders")
        .getOrCreate();

    Dataset<Row> streamingDataset = spark.readStream()
        .format("mongodb")
        .load();
    DataStreamWriter<Row> dataStreamWriter = streamingDataset.writeStream()
        .trigger(Trigger.Continuous("100 seconds"))
        .format("memory")
        // .option("checkpointLocation", "./checkpointDir")
        .outputMode("append")
        .queryName("memoryname");
    StreamingQuery query = dataStreamWriter.start();
    System.out.println(query.isActive());
    query.awaitTermination();

  }
}
