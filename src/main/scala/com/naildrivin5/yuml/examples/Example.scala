package com.naildrivin5.yuml.example

import com.naildrivin5.yuml._

object Main extends Application {
  import YUML._
  var d = classDiagram
  d += C("Customer") +1-*> C("Order")
  d += C("Order")("newOrder") ++- (C("Line Item"))("items")
  d += C("Order") -"0..1"> C("Payment Method")
  d += C("Service") -->("uses",C("Customer"))
  d += C("Foo") -1-"1..2"> C("Bar")
  d.download("diagram.png")
}
