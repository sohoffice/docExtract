package example

/**
 * FooClass
 *
 * @param name name
 */
class FooClass(
  name: String) {

  /**
   * method
   *
   * @return
   */
  def method: String = ""

  /**
    * method with nickname
    *
    * @param nickname nickname
    * @return
    */
  def method(nickname: String) = ""

  /**
   * value
   */
  lazy val lazyValue: Int = 0

}
