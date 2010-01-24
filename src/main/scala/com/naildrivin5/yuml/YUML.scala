package com.naildrivin5.yuml

import java.net._
import java.io._

object YUML {
  implicit def intToString(i:Int) = i.toString
  def classDiagram = new ClassDiagram(Nil,true)
  def orderlyClassDiagram = new ClassDiagram(Nil,false)
}

abstract class YUML(val scruffy:Boolean) {
  val baseUrl = "http://yuml.me/diagram"

  def diagramType:String
  def asString(delim:String):String

  def toURL = new URL(baseUrl + (if (scruffy) "/scruffy" else "") + "/" + diagramType + "/" + 
    (URLEncoder.encode(asString(", "),"utf-8").replaceAll("\\+","%20")))

  def download(filename:String) = {
    val is = toURL.openConnection.getInputStream
    val os = new FileOutputStream(new File("diagram.png"))
    var ch = is.read
    while (ch != -1) {
      os.write(ch)
      ch = is.read
    }
    is.close
    os.close
  }
}
