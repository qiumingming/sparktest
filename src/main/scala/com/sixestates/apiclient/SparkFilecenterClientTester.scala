package com.sixestates.apiclient

import com.alibaba.fastjson
import com.alibaba.fastjson.JSON
import com.lakeside.core.ArgOptions
import com.sixestates.apiclient.filecenter.SIXEFileCenter
import com.sixestates.apiclient.filecenter.client.{FileCenterClient, FileCenterClientFactory}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer


object FilecenterClientTester {
  protected val fileCenterClient: FileCenterClient = FileCenterClientFactory.getInstance("common", "ceph_gateway", "sg-west", "qiumingming@6estates.com")

  def main(args: Array[String]): Unit = {
    println("********** Hi!We are starting to do filecenter file test!")
    val loc = System.getenv("INFRA_SERVER_LOCATION")
    println("********** find systen env INFRA_SERVER_LOCATION : "+loc)


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

    val lines : List[(Array[Byte],Map[String,Any])] = SIXEFileCenter.loadKeyWordFile("common","ceph_gateway", "sg-west", "qiumingming@6estates.com", 9L)
    println("********** find file lines count : "+lines.length)

    val rdd : RDD[(Array[Byte],Map[String,Any])] = sc.parallelize(lines)

    val wordRdd = rdd.filter{ case (rk, note) => {
      var notEmpty: Boolean = true
      if(note.get("_tokens").isEmpty){
        notEmpty = false
      }
      notEmpty
    }}.flatMap{
      case( rk, note) =>{
        String.valueOf(note.get("_tokens")).split(" ")
      }
    }.filter(word => word.endsWith("/n"))

    val wordCount = wordRdd.count()
    println("********** find word count : "+wordCount)

    val sortWordRdd = wordRdd.map(wp => {
      val word = wp.split("/").apply(0)
      (word, 1)
    }).reduceByKey((a, b) => a + b).sortBy(
      _._2, false
    ).take(100)
    val topN = sortWordRdd.count(x => true)
    println("********** find Top word count : "+ topN)

    sortWordRdd.foreach(wc =>{
      println(wc)
    })

  }


}