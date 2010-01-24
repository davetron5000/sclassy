package com.naildrivin5.yuml.example

import com.naildrivin5.yuml._

object Main extends Application {
  import YUML._
  var d = classDiagram
  d += C("Company")<>-C("Manager")
  d += C("Company")<>-C("Employee")
  d += C("Person")^C("Manager")
  d += C("Person")^C("Employee")
  d += C("Person")++"1"-*C("Address")
  d += C("Manager")("boss")+-C("Employee")
  println(d.toString)
  println(d.toURL.toString)
  d.download("company.png")
}
