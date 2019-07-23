/*
 *  Copyright (C) 2014 - 2017 6ESTATES PTE. LTD - All Rights Reserved
 *
 */

package com.sixestates.apiclient.exception

/**
 * @author zhufb <zhufengbin@6estates.com>
 * @since 2/14/17 9:41 AM
 */
class AppExecuteException(message: String, throwable: Throwable) extends Exception {

  def this(message: String) {
    this(message, null)
  }

}
