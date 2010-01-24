package com.naildrivin5.yuml

class C(val name:String,val card:Option[String], val label:Option[String], val constructor:Option[Function2[C,C,Relation]]) {

  case class CWithDestCard(me:C,destCard:String) {
    def >(c:C) = Directional(me,C(c.name,destCard))
    /** This means that our destCard was really the cardinality of "me" not the to-be-named
      * destination class, so we recreate our CWithDestCard as such */
    def -(realDestCard:String) = CWithDestCard(C(me.name,destCard),realDestCard)
  }

  def apply(newLabel:String) = C(name,card,newLabel)
  def ->(c:C) = Directional(this,c)
  def -*>(c:C) = Directional(this,C(c.name,"*"))
  def -*(c:C) = UnspecifiedRelation(this,C(c.name,"*"))
  def <->(c:C) = Bidirectional(this,c)
  def +- (c:C) = Aggregation(this,c)
  def <>-(c:C) = +-(c)
  def -+ (c:C) = Aggregation(c,this)
  def -<>(c:C) = -+(c)
  def ++-(c:C) = Composition(this,c)
  def ^(c:C) = Inheritance(this,c)
  def -->(depName:String,c:C) = Dependencies(depName,this,c)
  def -(c:C) = UnspecifiedRelation(this,c)
  def ++(num:String) = new C(name,Some(num),label,Some(Composition))
  def +(num:String) = new C(name,Some(num),label,Some(Aggregation))
  def -(destCard:String) = CWithDestCard(this,destCard)

  override def toString = "[" + name + "]"
}

/** Factory for class descriptions */
object C {
  def apply(name:String) = new C(name,None,None,None)
  def apply(name:String,card:String) = new C(name,Some(card),None,None)
  def apply(name:String,card:String,label:String) = new C(name,Some(card),Some(label),None)
  def apply(name:String,card:Option[String],label:String) = new C(name,card,Some(label),None)
}
