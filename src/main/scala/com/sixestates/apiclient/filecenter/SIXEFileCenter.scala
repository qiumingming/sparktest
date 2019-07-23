package com.sixestates.apiclient.filecenter

import com.alibaba.fastjson.JSON
import com.sixestates.apiclient.exception.AppExecuteException
import com.sixestates.apiclient.filecenter.client.FileCenterClientFactory
import org.slf4j.LoggerFactory
import com.sixestates.apiclient.filecenter.ImproveUtil._

object SIXEFileCenter {
  val log=LoggerFactory.getLogger(SIXEFileCenter.getClass)
  /**
    * 从文件中心加载keywords的分析文件
    * @param serverCluster
    * @param s3Type
    * @param s3Cluster
    * @param token
    * @param fileId
    * @return
    */
  def loadKeyWordFile(serverCluster:String,s3Type:String,s3Cluster:String,token:String,fileId:Long):List[(Array[Byte],Map[String,Any])]={
    log.info("serverCluster:"+serverCluster+ " s3Type:"+s3Type+" s3Cluster:"+s3Cluster+" token:"+token+" fileId:"+fileId)
    val client=FileCenterClientFactory.getInstance(serverCluster,s3Type,s3Cluster,token)
    val bufferedReader=client.bufferedReadFile(fileId)
    var line:String=bufferedReader.readLine()
    var res=List[(Array[Byte],Map[String,Any])]()
    while(line!=null){
      try {
        val note = JSON.parseObject(line).toScala()
        res = res ::: List((DERowKeyUtil.convertCmiStr2ByteArr(note("cmi_id").asString), note))
        line = bufferedReader.readLine()
      } catch {
        case e:Exception =>{
          log.error("load file Exception:{}",e)
          log.error("error data record:"+line)
          throw new AppExecuteException("load file error",e)
        }
      }
    }
    bufferedReader.close()
    res
  }
}
