package LabWork3

class StringType(val range: Int = 10000) extends TypeBuilder {

  override def getTypeName = "String"

  override def create() = {
    var numberOfChar = 0
    var i = 0
    val s = new StringBuilder
    while (i < range)
    {
      i += 1
      if (Math.random * 1 < 0.5)
        numberOfChar = Math.round(Math.random() * 25 + 65).toInt
      else
        numberOfChar = Math.round(Math.random() * 25 + 97).toInt
      s.append(numberOfChar.toChar)
    }
    s.toString
  }

  override def getTypeComparator(): Comparator = (o1: Any, o2: Any) => ((o1.asInstanceOf[String]).compareTo(o2.asInstanceOf[String]))
}
