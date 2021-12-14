
class IntegerType(val range: Int = 100000) extends TypeBuilder {

  override def getTypeName = "Integer"

  override def create() = (Math.random * range).toInt

  override def getTypeComparator:
  Comparator = (o1: Any, o2: Any) => o1.asInstanceOf[Int] - o2.asInstanceOf[Int]
}