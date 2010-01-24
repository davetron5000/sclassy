# SClassy

Scala DSL for the awesome [yuml.me](yuml.me) class diagrams.

# Install

    git clone git://github.com/davetron5000/sclassy.git
    sbt run
    open diagram.png

This will download the source and run the example.

# Usage

This is actually something that makes great use of Scala's ability to create incomprehensible symbol-filled names.  So, the syntax
is very close to the yuml.me "official" syntax.  Let's take a simple domain.  A person has one or more addresses.  An employee and a manager
are both types of people and an employee has a manager (called the "boss").  Employees and Managers are part of the Company.  The yuml syntax for this is:

    [Manager]boss+-[Employee]
    [Person]++1-*-[Address]
    [Person]^[Employee]
    [Person]^[Manager]
    [Company]<>-[Employee]
    [Company]<>-[Manager]

The SClassy syntax is very similar:

    import com.naildrivin5.yuml._

    object Main extends Application {
      import YUML._
      var d = classDiagram
      d += C("Manager")("boss")+-C("Employee")
      d += C("Person")++"1"-*C("Address")
      d += C("Person")^C("Employee")
      d += C("Person")^C("Manager")
      d += C("Company")<>-C("Employee")
      d += C("Company")<>-C("Manager")
      d.download("company.png")
    }

## Not Handled

 * Some of the ways yuml lets you specify relations were hard to do, so things like <code>[Foo]1<->2[Bar]</code> won't work.
 * Can't define full classes yet (though it's easy to see how to support this by passing in a <code>Class</code> instance and not a name)
 * Colors and notes not implemented
 * Other diagram types
