package com.sixestates.apiclient.filecenter

import java.nio.charset.StandardCharsets

object DERowKeyUtil {

  /**
    * transform cmi_id into byte Array
    * @param cmi_id
    * @return
    */
  def convertCmiStr2ByteArr(cmi_id:String):Array[Byte]={
    if(cmi_id==null||cmi_id.length==0) return new Array[Byte](0)
    cmi_id.getBytes("utf-8")
  }

  /**
    * transform
    * @param cmi_id
    * @return
    */
  def convertBytes2CmiId(cmi_id:Array[Byte]):String={
    new String(cmi_id,StandardCharsets.UTF_8)
  }
}
