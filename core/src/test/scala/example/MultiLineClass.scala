package example

/**
 * MultiLineClass has line1.
 * It also has line2.
 *
 * @param name name has line1.
 *             And line2
 */
class MultiLineClass(
  name: String) {

  /**
   * method has line1.
   * And line2
   *
   * @return
   */
  def method: String = ""

  /**
   * value has line1.
   * And line2
   */
  lazy val lazyValue: Int = 0

}
