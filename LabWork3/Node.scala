package LabWork3

case class Node[T](var value: T)
{
  /*object Node
  {
    def apply(): Node[T] = new Node();
  }*/

  var left: Node[T] = null
  var right: Node[T] = null
  var parent: Node[T] = null
  var weight: Int = 0

  def printNode() = println("Значение выбранного узла: " + value);

  def getParent() = this.parent;

  def setParent(node: Node[T]) = this.parent = node;

  def getValue() = this.value;

  def setValue(value: T): Unit = this.value = value;

  def getWeight() =
  {
    try this.weight
    catch
    {
      case e: NullPointerException =>
        0
    }
  };

  def setWeight(weight: Int) = this.weight = weight;

  def getLeft() = this.left;

  def setLeft(left: Node[T]) = this.left=left;

  def getRight() = this.right;

  def setRight(right: Node[T]) = this.right = right;

  override def toString: String = "Узел{" + "Значение=" + value + '}'
}