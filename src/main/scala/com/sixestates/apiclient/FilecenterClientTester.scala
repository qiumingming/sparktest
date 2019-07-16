package com.sixestates.apiclient

import com.lakeside.core.ArgOptions
import com.sixestates.apiclient.filecenter.check.ThuKeywordFileDataChecker
import org.apache.spark.{SparkConf, SparkContext};

object FilecenterClientTester {

  def main(args: Array[String]): Unit = {
    println("Hi!We are starting to do filecenter file test!")
    val loc = System.getenv("INFRA_SERVER_LOCATION")
    println("find systen env INFRA_SERVER_LOCATION : "+loc)


    val argsOp = new ArgOptions(args)
    val conf = new SparkConf().setAppName("6ESTATES Filecenter Test - Qiumingming").setMaster(argsOp.get("master"))
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.eventLog.enabled", "true")
      .set("spark.io.compression.codec", "snappy")
      .set("spark.rdd.compress", "true")
      .set("spark.kryoserializer.buffer", "131072") //128M
      .set("spark.kryoserializer.buffer.max", "1024")
      .set("spark.default.parallelism", "200")
      .set("spark.scheduler.mode", "FAIR")

    val sc = new SparkContext(conf);

    val checker = new ThuKeywordFileDataChecker();
    checker.dataCheck(9L);

  }

}