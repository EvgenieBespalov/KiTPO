package LabWork3

trait Action[T]
{
  def toDo(elem: T): Unit
}
