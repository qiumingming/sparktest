package com.sixestates.apiclient.filecenter

import com.lakeside.core.utils.time.DateFormat

import scala.collection.JavaConverters._
import scala.util.control.Exception._

/**
 * Created by zhufb on 15-7-28.
 */
object ImproveUtil {

  implicit class improveStr(val s: String) {

    def asIntOpt = catching(classOf[NumberFormatException]) opt s.toInt

    def asInt(default: Int): Int = {
      s.asIntOpt.getOrElse(default)
    }

    def asLongOpt = catching(classOf[NumberFormatException]) opt s.toLong

    def asLong(default: Long): Long = {
      s.asLongOpt.getOrElse(default)
    }

    def asDoubleOpt = catching(classOf[NumberFormatException]) opt s.toDouble

    def asDouble(default: Double): Double = {
      s.asDoubleOpt.getOrElse(default)
    }

    def asDate = DateFormat.strToDate(s)

    def asBooleanOpt = catching(classOf[IllegalArgumentException]) opt s.toBoolean

    def asBoolean(default: Boolean): Boolean = {
      s.asBooleanOpt.getOrElse(default)
    }
  }

  implicit class improveAny(val s: Any) {

    def empty(): Boolean = {
      s.asString match {
        case "" => true
        case _ => false
      }
    }

    def asPureVal: Any = {
      s match {
        case None => null
        case Some(v) => v.asPureVal
        case _ => s
      }
    }

    def asString: String = {
      s match {
        case null => ""
        case None => ""
        case Some(v) => v.asString
        case _ => s.toString
      }
    }

    def asIntOpt: Option[Int] = s.asString.asIntOpt

    def asInt(default: Int): Int = {
      s.asIntOpt.getOrElse(default)
    }

    def asLongOpt: Option[Long] = s.asString.asLongOpt

    def asLong(default: Long): Long = {
      s.asLongOpt.getOrElse(default)
    }

    def asDoubleOpt: Option[Double] = s.asString.asDoubleOpt

    def asDouble(default: Double): Double = {
      s.asDoubleOpt.getOrElse(default)
    }

    def asBooleanOpt: Option[Boolean] = s.asString.asBooleanOpt

    def asBoolean(default: Boolean): Boolean = {
      s.asBooleanOpt.getOrElse(default)
    }

    def asDate = DateFormat.strToDate(s.asPureVal)

    def asOrdering(): Any = {
      s match {
        case _: Int => s
        case _: Long => s
        case _: Double => s
        case _: Array[Byte] => s.asInstanceOf[Array[Byte]].mkString("")
        case _ => s.asString
      }
    }

    def asBase64String(): String = {
      if (s.isInstanceOf[Array[Byte]]) {
        new sun.misc.BASE64Encoder().encode(s.asInstanceOf[Array[Byte]])
      } else {
        s.toString
      }
    }

  }

  implicit class improveJavaMap[A, B](map: java.util.Map[A, B]) {

    def toScala(): Map[A, Any] = {
      map.asScala.map {
        case (k, v) => {
          val converted = v match {
            case m: java.util.Map[Any, Any] => m.toScala()
            case l: java.util.List[Any] => l.toScala
            case _ => v
          }
          (k, converted)
        }
      }.toMap
    }
  }

  implicit class improveJavaList[A](list: java.util.List[A]) {
    def toScala(): List[Any] = {
      list.asScala.map(xs => {
        xs match {
          case m: java.util.Map[Any, Any] => m.toScala
          case l: java.util.List[Any] => l.toScala
          case _ => xs
        }
      }
      ).toList
    }
  }

  implicit class improveJavaObject[A,B](o: Object) {
    def toScala(): Any = {
      o match {
        case m: java.util.Map[A, B] => m.toScala
        case l: java.util.List[Any] => l.toScala
        case _ => o
      }
    }
  }

  implicit class improveScalaMap[A, B](map: Map[A, B]) {

    def toJava(): java.util.Map[A, Any] = {
      map.map {
        case (k, v) => {
          val converted = v match {
            case m: Map[Any, Any] => m.toJava
            case l: List[Any] => l.toJava
            case _ => v
          }
          (k, converted)
        }
      }.asJava
    }
  }


  implicit class improveScalaList[A](list: List[A]) {
    def toJava(): java.util.List[Any] = {
      list.map(xs => {
        xs match {
          case m: Map[Any, Any] => m.toJava
          case l: List[Any] => l.toJava
          case _ => xs
        }
      }
      ).asJava
    }
  }


}
