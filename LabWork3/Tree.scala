package LabWork3

import scala.collection.mutable.Stack

case class Tree[T]() extends Trunk[T]
{
  var root: Node[T] = null

  def getRoot() = this.root

  def setRoot(root: Node[T]) = this.root = root

  override def insert(value: T, comparator: Comparator) =
  {
    val newNode = Node[T](value);
    //newNode.value = value;
    newNode.weight = 1;

    if(root == null)
    {
      root = newNode;
    }
    else
    {
      var curNode = root;
      var parNode = Node[T](value);
      var check = true
      while(check == true)
      {
        parNode = curNode;
        if(value == curNode.value)
        {
          check = false
        }
        else
        {
          if(comparator.compare(curNode.value, value) > 0)
          {
            curNode = curNode.left;
            if(curNode == null)
            {
              parNode.left = newNode;
              newNode.parent = parNode;
              weightPlace(root);
              check = false
            }
          }
          else
          {
            curNode = curNode.right;
            if(curNode == null)
            {
              parNode.right = newNode;
              newNode.parent = parNode;
              weightPlace(root);
              check = false
            }
          }
        }
      }
    }
  }

  def weightPlace(node: Node[T]): Unit =
  {
    if(node != null)
    {
      weightPlace(node.left);
      weightPlace(node.right);
      if(node.left == null || node.right == null)
      {
        if(node.left == null && node.right == null)
          node.weight = 1;
        else
          if(node.left == null)
            node.weight = 1 + node.right.weight;
          else
            if(node.right == null)
              node.weight = 1 + node.left.weight;
      }
      else
        node.weight = 1 + node.left.weight + node.right.weight;
    }
  }

  def findNodeByInd(index: Int): Node[T] =
  {
    var index_node = 0
    var search_node = this.root
    var answer = Node[T](search_node.value)

    if (search_node.left != null)
      index_node = search_node.left.getWeight()
    else
      index_node = 0
    //index_node = search_node.left.getWeight()

    var check = true

    while (check)
      if (index == index_node)
      {
        answer = search_node
        check = false
      }
      else
        if (index > index_node)
        {
          search_node = search_node.right
          try index_node += search_node.left.weight + 1
          catch {
            case e: NullPointerException =>
              index_node += 1
          }
        }
        else
        {
          search_node = search_node.left
          try index_node -= search_node.right.weight + 1
          catch {
            case e: NullPointerException =>
              index_node -= 1
          }
        }
    answer
  }

  private def deleteNode(search_node: Node[T]): Boolean =
  {
    val prev_node = search_node.parent
    if (search_node.left == null && search_node.right == null) { //удаляемый узел - лист
      if (prev_node == null)
        this.root = null
      else
        if (prev_node.left == search_node)
          prev_node.left = null
        else
          if (prev_node.right == search_node) prev_node.right = null
    }
    else
      if (search_node.left != null && search_node.right == null) { //удаляемый узел имеет только левого потомка
        if (prev_node == null) {
          this.root = search_node.left
          this.root.parent = null
        }
        else
          if (prev_node.left == search_node) {
            prev_node.left = search_node.left
            search_node.left.parent = prev_node
          }
          else
            if (prev_node.right == search_node) {
              prev_node.right = search_node.left
              search_node.left.parent = prev_node
            }
      }
      else
        if (search_node.left == null && search_node.right != null) {
          if (prev_node == null) {
            this.root = search_node.right
            this.root.parent = null
          }
          else
            if (prev_node.left == search_node) {
              prev_node.left = search_node.right
              search_node.right.parent = prev_node
            }
            else
              if (prev_node.right == search_node) {
                prev_node.right = search_node.right
                search_node.right.parent = prev_node
              }
        }
        else
          return false
    true
  }

  override def deleteNodeByInd(index: Int): Boolean =
  {
    if(this.root.getWeight() - 1 < index)
      return false

    var search_node = this.root
    var index_node = search_node.left.getWeight()

    var check = true
    while (check)
      if (index == index_node)
      {
        if (deleteNode(search_node) == true){}
        else
          if (search_node.left != null && search_node.right != null)
          {
            var del_node = search_node.right
            while (del_node.left != null && del_node.right != null)
              if (del_node.left != null)
              {
                //del_node.setWeight(del_node.getWeight - 1)
                del_node.weight = del_node.weight - 1
                del_node = del_node.left
              }
              else
              {
                del_node.weight = del_node.weight - 1
                del_node = del_node.right
              }
            //search_node.setValue(del_node.getValue)
            search_node.value = del_node.value
            //search_node.setWeight(search_node.getWeight - 1)
            search_node.weight = search_node.weight - 1
            deleteNode(del_node)
          }
        check = false
      }
      else
        if (index > index_node)
        {
          search_node.weight = search_node.getWeight() - 1
          search_node = search_node.right
          //index_node += search_node.left.getWeight() + 1
          try index_node += (search_node.left.weight + 1)
          catch {
            case e: NullPointerException =>
              index_node += 1
          }
        }
        else
        {
          search_node.weight = search_node.weight - 1
          search_node = search_node.left
          //index_node -= search_node.right.getWeight() + 1
          try index_node -= (search_node.right.weight + 1)
          catch {
            case e: NullPointerException =>
              index_node -= 1
          }
        }
    true
  }

  override  def printTree(): Unit =
  {
    val globalStack = Stack[Node[T]]()
    globalStack.push(root)
    var gaps = 32
    var isRowEmpty = false
    val separator = "-----------------------------------------------------------------"
    println(separator) // черта для указания начала нового дерева

    while (isRowEmpty == false)
    {
      val localStack = Stack[Node[T]]() // локальный стек для задания потомков элемента
      isRowEmpty = true
      var j = 0
      while (j < gaps)
      {
        print(' ')
        j += 1
      }
      while (globalStack.isEmpty == false)
      { // покуда в общем стеке есть элементы
        val temp = globalStack.pop.asInstanceOf[Node[T]] // берем следующий, при этом удаляя его из стека
        if (temp != null)
        {
          print(temp.value + ":" + temp.weight) // выводим его значение в консоли
          localStack.push(temp.left) // соохраняем в локальный стек, наследники текущего элемента
          localStack.push(temp.right)
          if (temp.left != null || temp.right != null) isRowEmpty = false
        }
        else
        {
          print("__") // - если элемент пустой

          localStack.push(null)
          localStack.push(null)
        }
        var j = 0
        while (j < gaps * 2 - 2)
        {
          print(' ')
          j += 1
        }
      }
      println()
      gaps /= 2 // при переходе на следующий уровень расстояние между элементами каждый раз уменьшается

      while (localStack.isEmpty == false)
        globalStack.push(localStack.pop) // перемещаем все элементы из локального стека в глобальный
    }
    println(separator) // подводим черту

  }

  private def createVine(): Unit =
  {
    var grandParent = Node[T](root.value)
    grandParent = null
    var parent = root
    var leftChild = Node[T](root.value)
    while (null != parent) {
      leftChild = parent.left
      if (null != leftChild) {
        grandParent = rotateRight(grandParent, parent, leftChild)
        parent = leftChild
      }
      else {
        grandParent = parent
        parent = parent.right
      }
    }
  }

  private def rotateRight(grandParent: Node[T], parent: Node[T], leftChild: Node[T]) =
  {
    if (null != grandParent) grandParent.right = leftChild
    else this.root = leftChild
    parent.left = leftChild.right
    leftChild.right = parent
    grandParent
  }

  private def rotateLeft(grandParent: Node[T], parent: Node[T], rightChild: Node[T]): Unit =
  {
    if (null != grandParent)
      grandParent.right = (rightChild)
    else
      setRoot(rightChild)
    parent.right = rightChild.left
    rightChild.left = parent
  }

  override def createCompleteBST(): Unit =
  {
    this.createVine()
    var n = 0
    var tmp = root
    while (null != tmp) {
      n += 1
      tmp = tmp.right
    }
    var m = greatestPowerOf2LessThanN(n + 1) - 1
    makeRotations(n - m)
    while (m > 1){
      m = m / 2
      makeRotations(m)

    }
    weightPlace(root)
  }

  private def greatestPowerOf2LessThanN(nn: Int): Int =
  {
    var n = nn
    var ndx = 0
    while (1 < n) {
      n = (n >> 1)
      ndx += 1
    }
    1 << ndx //2^x
  }

  private def makeRotations(bounds: Int): Unit = {
    var grandParent = Node[T](root.value)
    grandParent = null
    var parent = root
    var child = root.right
    var bound = bounds

    while (bound > 0) {
      try
        if (null != child) {
          rotateLeft(grandParent, parent, child)
          grandParent = child
          parent = grandParent.right
          child = parent.right
        }
        else
          bound = -1
      catch {
        case convenient: NullPointerException =>
          bound = -1
      }

      bound -= 1
    }
  }

  def iterator(): Iterator[T] = new Iterator[T] {
    var counter = 0
    var buf: Node[T] = root

    def hasNext: Boolean = return this.counter < root.getWeight

    def next: T =
    {
      if (counter != 0)
      {
        counter += 1;
        buf = findNodeByInd(counter - 1)
      }
      buf.value
    }
  }

  def forEach(action: Action[T]): Unit = {
    if (this.root.getWeight > 0) {
      var counter = 0
      var node = findNodeByInd(counter)
      do {
        action.toDo(node.value)
        counter += 1
        node = findNodeByInd(counter)
      } while ( node != null)
    }
    else println("Нет элементов в массиве")
  }
}