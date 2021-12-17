package LabWork3

import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application._
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.image.Image
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color.{Black, LightGray}

import java.awt.Desktop.Action
import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}


object Screen extends JFXApp3 {

  class NodeTableElement(val value: String) {
    val nodeValue = new StringProperty(value)
  }

  var box:VBox = null
  var nodeTable:TableView[NodeTableElement] = null
  var integerBuilder:IntegerType = null
  var stringBuilder:StringType = null
  var treeInt:Tree[Int] = null
  var treeString:Tree[String] = null

  //От сюда происходит запуск программы
  override def start(): Unit = {

    box = new VBox {
      autosize()
    }

    stage = new PrimaryStage {

      //Отрисовывем сцену в окне
      scene = new Scene(1000, 500) {
        //Название окна
        title = "Бинарное дерево ScalaFX"

        fill = LightGray
        root = box

      }
    }

    val choices = Seq("Integer", "String")

    val dialog = new ChoiceDialog(defaultChoice = "Integer", choices = choices) {
      initOwner(stage)
      title = "Тип дерева"
      headerText = "Введите тип данных вашего дерева"
    }

    val result = dialog.showAndWait()

    result match {
      case Some(choice) =>
        if (choice == "Integer") {
          integerBuilder = new IntegerType()
          treeInt = new Tree[Int]()
        }
        else {
          stringBuilder = new StringType()
          treeString = new Tree[String]()
        }
      case None =>
        println("Не выбрано")
        this.stopApp()
    }

    configureTable(box)

  }

  def configureTable(root: VBox): Unit = {

    val menuBar = new MenuBar()

    val fileMenu = new Menu("Файл")

    val saveFileItem = new MenuItem("Сохранить")



    saveFileItem.onAction = (event: ActionEvent) => {
      if (treeInt != null) {
        val oos = new ObjectOutputStream(new FileOutputStream("IntTree.txt"))
        oos.writeObject(treeInt)
        oos.close()
      } else {
        val oos = new ObjectOutputStream(
          new FileOutputStream("StrTree.txt"))
        oos.writeObject(treeString)
        oos.close()
      }
    }

    val loadFileItem = new MenuItem("Загрузка")

    loadFileItem.onAction = (event: ActionEvent) => {

      if (treeInt != null) {
        val ois = new ObjectInputStream(new FileInputStream("IntTree.txt"))
        treeInt = ois.readObject.asInstanceOf[Tree[Int]]
        ois.close()

      }
      else {
        val ois = new ObjectInputStream(new FileInputStream("StrTree.txt"))
        treeString = ois.readObject.asInstanceOf[Tree[String]]
        ois.close()
      }

      updateTables()
    }

    fileMenu.items = List(saveFileItem, loadFileItem)

    val actionsMenu = new Menu("Действия")
    val addItem = new MenuItem("Добавить несколько элементов")
    addItem.onAction = (event: ActionEvent) => {
      val dialog = new TextInputDialog(defaultValue = "number") {
        initOwner(stage)
        title = "Дополнение"
        headerText = "Количество эдементов"
        contentText = "Введите количество элементов:"
      }

      val result = dialog.showAndWait()

      result match {
        case Some(name) =>
          if (treeInt != null) {
            for(i <- 0 until name.toInt){
              treeInt.insert(integerBuilder.create(), integerBuilder.getTypeComparator)
            }
            //singleLInt.forEach(println)
          }
          else {
            for(i <- 0 until name.toInt){
              treeString.insert(stringBuilder.create(), stringBuilder.getTypeComparator())
            }
            //singleLString.forEach(println)
          }
        case None       =>
          new Alert(AlertType.Information) {
            initOwner(stage)
            title = "Информация"
            headerText = "Закрыть дополнение"
            contentText = "Вы отменили добавление элементов, так что ничего не произойдет"
          }.showAndWait()
      }

      updateTables()
    }

    val deleteItem = new MenuItem("Удаление элементов по индексу")
    deleteItem.onAction = (event: ActionEvent) => {
      val dialog = new TextInputDialog(defaultValue = "index") {
        initOwner(stage)
        title = "Удаление"
        headerText = "Индекс элемента"
        contentText = "Введите индекс удаляемого элемента:"
      }

      val result = dialog.showAndWait()

      result match {
        case Some(name) =>
          if (treeInt != null) {
            treeInt.deleteNodeByInd(name.toInt)
          }
          else {
            treeString.deleteNodeByInd(name.toInt)
          }
        case None =>
          new Alert(AlertType.Information) {
            initOwner(stage)
            title = "Информация"
            headerText = "Закрыть удаление"
            contentText = "Вы отменили удаление элементов, так что ничего не произойдет"
          }.showAndWait()
      }

      updateTables()
    }

    val searchItem = new MenuItem("Поиск элементов по индексу")
    searchItem.onAction = (event: ActionEvent) => {
      val dialog = new TextInputDialog(defaultValue = "index") {
        initOwner(stage)
        title = "Поиск"
        headerText = "Индекс элемента"
        contentText = "Введите индекс искомого элемента:"
      }

      val result = dialog.showAndWait()

      result match {
        case Some(name) =>
          if (treeInt != null) {
            if(treeInt.getRoot().getWeight() > name.toInt) {

              new Alert(AlertType.Information) {
                initOwner(stage)
                title = "Информация"
                headerText = "Поиск"
                contentText = s"Искомый элемент: ${treeInt.findNodeByInd(name.toInt)}"
              }.showAndWait()
              updateTables()
            }
            else{
              new Alert(AlertType.Information) {
                initOwner(stage)
                title = "Информация"
                headerText = "Элемента с данным индексом не существует"
                contentText = "Введите другой индекс"
              }.showAndWait()
            }
          }
          else {
            if(treeString.getRoot().getWeight() > name.toInt) {

              new Alert(AlertType.Information) {
                initOwner(stage)
                title = "Информация"
                headerText = "Поиск"
                contentText = s"Искомый элемент: ${treeString.findNodeByInd(name.toInt)}"
              }.showAndWait()
              updateTables()
            }
            else{
              new Alert(AlertType.Information) {
                initOwner(stage)
                title = "Информация"
                headerText = "Элемента с данным индексом не существует"
                contentText = "Введите другой индекс"
              }.showAndWait()
            }
          }
        case None =>
          new Alert(AlertType.Information) {
            initOwner(stage)
            title = "Информация"
            headerText = "Закрыть поиск"
            //contentText = "Вы отменили удаление элементов, так что ничего не произойдет"
          }.showAndWait()
      }

      updateTables()
    }

    val balItem = new MenuItem("Балансировка")
    balItem.onAction = (event: ActionEvent) => {
      if(treeInt!= null){
        if(treeInt.getRoot().getWeight() != 0) {
          var lastTime = System.currentTimeMillis
          treeInt.createCompleteBST()
          val number = System.currentTimeMillis() - lastTime

          new Alert(AlertType.Information) {
            initOwner(stage)
            title = "Информация"
            headerText = "Балансировка"
            contentText = s"Балансировка заняла ${number} мс"
          }.showAndWait()
          updateTables()
        }
        else{
          new Alert(AlertType.Information) {
            initOwner(stage)
            title = "Информация"
            headerText = "Дерево пусто"
            contentText = "Добавьте несколько элементов"
          }.showAndWait()
        }
      }
      else if(treeString!= null) {
        if(treeString.getRoot().getWeight() != 0) {
          var lastTime = System.currentTimeMillis
          treeString.createCompleteBST()
          val number = System.currentTimeMillis() - lastTime

          new Alert(AlertType.Information) {
            initOwner(stage)
            title = "Информация"
            headerText = "Балансировка"
            contentText = s"Балансировка заняла ${number} мс"
          }.showAndWait()
          updateTables()
        }
        else{
          new Alert(AlertType.Information) {
            initOwner(stage)
            title = "Информация"
            headerText = "Дерево пусто"
            contentText = "Добавьте несколько элементов"
          }.showAndWait()
        }
      }
    }

    actionsMenu.items = List(addItem, deleteItem, balItem, searchItem)

    menuBar.menus = List(fileMenu, actionsMenu)

    val nodeData = ObservableBuffer(
      new NodeTableElement("\t-")
    )

    nodeTable = createNodeTableView(nodeData)
    nodeTable.columnResizePolicy = TableView.ConstrainedResizePolicy

    var label: Label = null
    if (treeInt != null)
      label = new Label("\tInteger-дерево размер: " + 0)
    else if (treeString != null)
      label = new Label("\tString-дерево размер: " + 0)

    root.children = Seq(
      menuBar,
      label,
      nodeTable
    )

  }

  def updateTables() = {
    var nodeData: ObservableBuffer[NodeTableElement] = null

    if (integerBuilder!=null) {
      var size = treeInt.getRoot().getWeight()
      for(i <- 0 until size){
        if (nodeData == null)
          nodeData = ObservableBuffer(
            new NodeTableElement(
              treeInt.findNodeByInd(i).toString()
            )
          )
        else
          nodeData.addOne(
            new NodeTableElement(
              treeInt.findNodeByInd(i).toString()
            )
          )
      }
    }
    else if (stringBuilder!=null) {
      var size = treeString.getRoot().getWeight()
      for(i <- 0 until size){
        if (nodeData == null)
          nodeData = ObservableBuffer(
            new NodeTableElement(
              treeString.findNodeByInd(i).toString()
            )
          )
        else
          nodeData.addOne(
            new NodeTableElement(
              treeString.findNodeByInd(i).toString()
            )
          )
      }
    }

    var label: Label = null
    if (treeInt != null)
      label = new Label("\tInteger-дерево размер: " + treeInt.getRoot().getWeight())
    else if (treeString != null)
      label = new Label("\tString-дерево размер: " + treeString.getRoot().getWeight())

    nodeTable = createNodeTableView(nodeData)
    nodeTable.columnResizePolicy = TableView.ConstrainedResizePolicy

    box.children.set(1, label)
    box.children.set(2, nodeTable)
  }

  def createNodeTableView(data: ObservableBuffer[NodeTableElement]): TableView[NodeTableElement] = {
    val table = new TableView[NodeTableElement] {
      columns ++= Seq(
        new TableColumn[NodeTableElement, String] {
          text = "Элементы"
          prefWidth = 10
          cellValueFactory = _.value.nodeValue
        }
      )
      items = data
    }
    table
  }

}