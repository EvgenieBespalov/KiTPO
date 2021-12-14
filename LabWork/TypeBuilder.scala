
trait TypeBuilder {
  def getTypeName: String

  def create(): Any

  def getTypeComparator: Comparator
}