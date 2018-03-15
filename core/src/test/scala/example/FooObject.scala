package example

/**
  * FooObject
  */
object FooObject {

  /**
    * FooType
    */
  type FooType = String

  /**
    * method
    *
    * @param arg arg
    * @return
    */
  def method(arg: Int): String = ""

  /**
    * method
    */
  val method: String = ""

  /**
    * FooBarObject
    */
  object FooBarObject {

    /**
      * FooBarBazCaseClass
      *
      * @param name name
      */
    case class FooBarBazCaseClass(
      name: String
    )

  }
}
