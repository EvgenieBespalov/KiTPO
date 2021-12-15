package LabWork3

trait Trunk[T] {

  def insert(value: T, comparator: Comparator): Unit
  def deleteNodeByInd(index: Int): Boolean
  def printTree(): Unit
  def createCompleteBST(): Unit
}