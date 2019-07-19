package com.sixestates.apiclient

import java.io.BufferedReader
import java.util

import com.lakeside.core.ArgOptions
import com.sixestates.apiclient.filecenter.check.ThuKeywordFileDataChecker
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.JSONObject;

object FilecenterClientTester {
  protected val log : Logger = LoggerFactory.getLogger(FilecenterClientTester.class);
  protected val fileCenterClient: FileCenterClient = FileCenterClientFactory.getInstance("common", "ceph_gateway", "sg-west", "qiumingming@6estates.com")

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

    val lines = getFileRdd(9L)

    var rdd = sc.parallelize(lines)


    val checker = new ThuKeywordFileDataChecker();
    checker.dataCheck(9L);

  }

  def getFileRdd(fileId : Long): List<String> = {
    val lines : List<String> = new util.ArrayList[]()
    val bufferedReader = this.fileCenterClient.bufferedReadFile(fileId)
    var order = 1
    var errorSize = 0

    var line = bufferedReader.readLine
    while ( {
      StringUtils.isNotEmpty(line)
    }) {
      val tokens = this.doCheck(line)
      if (tokens == null) {
        log.error("Data[{}] format error : {}", order, line)
        errorSize = errorSize + 1
      }else{
        lines.add(line);
      }
      if (order % 10000 == 0) log.info("Now check data size {}/{}, and find error data size : {}", Array[AnyRef](order, lineSize, errorSize))
      line = bufferedReader.readLine
      order = order + 1
    }

    bufferedReader.close();
    log.info("Totally check data size {}/{}, and find error data size : {}", Array[AnyRef](order, lineSize, errorSize))
    return lines;
  }

  def docheck(line : String) : String = {
        val jo: JSONObject = JSONObject.parseObject(line);
        if (jo.containsKey("_tokens") && jo.containsKey("cmi_id")) {
          return jo.getString("_tokens");
        } else {
          return null;
        }

  }
}