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

public final class MongoStructuredStreamingWordCount {

  public static void main(final String[] args) {
    /*
     * Create the SparkSession.
     * If config arguments are passed from the command line using --conf,
     * parse args for the values to set.
     */
    SparkSession spark = SparkSession.builder()
        .master("local")
        .appName("mongo_word_count")
        .config("spark.mongodb.read.connection.uri",
            "mongodb://127.0.0.1:30001,127.0.0.1:30002,127.0.0.1:30003/?replicaSet=my-replica-set")

        // .config("spark.mongodb.write.connection.uri",
        // "mongodb://127.0.0.1/matching-engine.orders")
        .getOrCreate();

    // define a streaming query
    Dataset<Row> load = spark.readStream()
        .format("mongodb")
        .option("database", "words").option("collection", "word")
        .option("spark.mongodb.change.stream.publish.full.document.only", "true")
        .option("forceDeleteTempCheckpointLocation", "true")

        .load();
    load.printSchema();


    Dataset<Row> wordCounts = load.groupBy("word").count();

    DataStreamWriter<Row> dataStreamWriter = wordCounts
        // manipulate your streaming data
        .writeStream()
        .format("console")
        .trigger(Trigger.ProcessingTime("15 seconds"))
        .outputMode("complete");

    // run the query
    try {
      StreamingQuery query = dataStreamWriter.start();
      query.awaitTermination();
      System.out.println("query.isActive()" + query.isActive());
    } catch (TimeoutException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (StreamingQueryException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
