package com.naildrivin5.yuml

class ClassDiagram(val relations:List[Relation], isScruffy:Boolean) extends YUML(isScruffy) {
  def makeOrderly = new ClassDiagram(relations,false)
  def makeScruffy = new ClassDiagram(relations,true)

  def +(r:Relation) = new ClassDiagram(r :: relations,scruffy)
  override def toString = asString("\n")
  def asString(delim:String) = relations.mkString(delim)
  def diagramType = "class"
}

