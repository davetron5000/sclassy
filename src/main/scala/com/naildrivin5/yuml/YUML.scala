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
class ClassDiagram(val relations:List[Relation], isScruffy:Boolean) extends YUML(isScruffy) {
  def makeOrderly = new ClassDiagram(relations,false)
  def makeScruffy = new ClassDiagram(relations,true)

  def +(r:Relation) = new ClassDiagram(r :: relations,scruffy)
  override def toString = asString("\n")
  def asString(delim:String) = relations.mkString(delim)
  def diagramType = "class"
}

sealed abstract class Relation(source:C,destination:C,connector:String) {
  override def toString = {
    source.toString + source.label.getOrElse("") + (source.card match {
      case None => ""
      case Some(i) => "+" + i
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

class C(val name:String,val card:Option[String], val label:Option[String]) {

  case class CWithDestCard(me:C,destCard:String) {
    def >(c:C) = Directional(me,C(c.name,destCard))
    /** This means that our destCard was really the cardinality of "me" not the to-be-named
      * destination class, so we recreate our CWithDestCard as such */
    def -(realDestCard:String) = CWithDestCard(C(me.name,destCard),realDestCard)
  }

  def apply(newLabel:String) = C(name,card,newLabel)
  def ->(c:C) = Directional(this,c)
  def -*>(c:C) = Directional(this,C(c.name,"*"))
  def <->(c:C) = Bidirectional(this,c)
  def +- (c:C) = Aggregation(this,c)
  def <>-(c:C) = +-(c)
  def -+ (c:C) = Aggregation(c,this)
  def -<>(c:C) = -+(c)
  def ++-(c:C) = Composition(this,c)
  def ^(c:C) = Inheritance(this,c)
  def -->(depName:String,c:C) = Dependencies(depName,this,c)
  def -(c:C) = UnspecifiedRelation(this,c)

  def +(num:String) = C(name,num)
  def -(destCard:String) = CWithDestCard(this,destCard)

  override def toString = "[" + name + "]"
}

/** Factory for class descriptions */
object C {
  def apply(name:String) = new C(name,None,None)
  def apply(name:String,card:String) = new C(name,Some(card),None)
  def apply(name:String,card:String,label:String) = new C(name,Some(card),Some(label))
  def apply(name:String,card:Option[String],label:String) = new C(name,card,Some(label))
}
