object Main extends App
{
  def sortTest(number: Int):Unit = {

    //val builder = new IntegerType()
    val builder = new StringType()
    var tree = new Tree[String]()

    for(i <- 0 until number){
      tree.insert(builder.create, builder.getTypeComparator)
    }

    var lastTime = System.currentTimeMillis
    tree.createCompleteBST()
    println(s"На сортировку ${number} элементов было затрачено: " +
      s"${System.currentTimeMillis()-lastTime} миллисекунд")

  }

  sortTest(10000)
  sortTest(20000)
  sortTest(40000)
  sortTest(80000)
  sortTest(160000)
}