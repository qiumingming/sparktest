package com.sixestates.apiclient

import com.sixestates.apiclient.filecenter.check.ThuKeywordFileDataChecker;

object FilecenterClientTester {

  def main(args: Array[String]): Unit = {
    println("Hi!We are starting to do filecenter file test!")

    val checker = new ThuKeywordFileDataChecker();
    checker.dataCheck(9L);

  }

}