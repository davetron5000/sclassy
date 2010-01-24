package com.naildrivin5.yuml

sealed abstract class Relation(source:C,destination:C,connector:String) {
  override def toString = {
    source.toString + source.label.getOrElse("") + (source.card match {
      case None => ""
      case Some(i) => source.constructor match {
        case Some(Composition) => "++" + i
        case _ => "+" + i
      }
    }) + (destination.card match {
      case None => ""
      case Some(i) => source.card match {
        case None => i
        case Some(x) => "-" + i
      }
    }) + connector + destination.label.getOrElse("") + destination.toString
  }
}
case class UnspecifiedRelation(s:C,d:C) extends Relation(s,d,"-")
case class Directional(s:C,d:C) extends Relation(s,d,"->")
case class Bidirectional(s:C,d:C) extends Relation(s,d,"<->")
case class Aggregation(owner:C,ownee:C) extends Relation(owner,ownee,"+-")
case class Composition(owner:C,ownee:C) extends Relation(owner,ownee,"++-")
case class Inheritance(sub:C,sup:C) extends Relation(sub,sup,"^")
case class Dependencies(name:String, user:C,usee:C) extends Relation(user,usee,name + "-.->")

