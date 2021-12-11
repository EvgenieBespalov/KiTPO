package kitpolw1;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import java.io.*;

class Tree<T> implements Trunk<T>, Serializable
{
    
    private class Node implements Serializable
    {
        private T value; // ключ узла
        private int weight; // вес узла
        private Node leftChild; // Левый узел потомок
        private Node rightChild; // Правый узел потомок
        private Node parent; // Родитель узла

        Node()
        {
            leftChild = null;
            rightChild = null;
            parent = null; 
        }
        
        public void printNode() { // Вывод значения узла в консоль
            System.out.println(" Выбранный узел имеет значение :" + value);
        }
          
        public Node getParent() {   return this.parent; }
   
        public void setParent(Node node)    {  this.parent = node;  }

        public T getValue()   {   return this.value;  }
   
        public void setValue(final T value)   { this.value = value;   }
   
        public int getWeight(){        
            try
            {
                int weight = this.weight;
                return weight; 
            }
            catch(NullPointerException e)
            {
                return 0; 
            }   
        }

        public void setWeight(final int weight) {   this.weight = weight;   }

        public Node getLeftChild()  {   return this.leftChild;  }

        public void setLeftChild(final Node leftChild)  {   this.leftChild = leftChild; }

        public Node getRightChild() {   return this.rightChild; }

        public void setRightChild(final Node rightChild)    {   this.rightChild = rightChild;   }

        @Override
        public String toString() {
            return "Узел{" +
                "Значение=" + value +
                '}';
        }
    }
    
    private Node rootNode; // корневой узел

    public Tree() { // Пустое дерево
       rootNode = null;
    }
    
    private Node getRoot() 
    {
        return this.rootNode;
    }
    
    private void setRoot(Node root) 
    {
        this.rootNode = root;
    }
    
    @Override
    public void insertNode(T value, Comparator comparator) // метод вставки нового элемента
    { 
        Node newNode = new Node(); // создание нового узла
        newNode.setValue(value); // вставка данных
        newNode.setWeight(1);
        
        if (rootNode == null) // если корневой узел не существует
        { 
            rootNode = newNode;// то новый элемент и есть корневой узел
            
        }
        else // корневой узел занят
        { 
            Node currentNode = rootNode; // начинаем с корневого узла
            Node parentNode;
            while (true) // мы имеем внутренний выход из цикла
            {
                parentNode = currentNode;
                if(value == currentNode.getValue()) 
                {   // если такой элемент в дереве уже есть, не сохраняем его
                    return;    // просто выходим из метода
                }
                else  
                {    
                    //if (value < currentNode.getValue())  // движение влево?
                    //if (currentNode.getValue().compareTo(value) > 0)  // движение влево?
                    if (comparator.compare(currentNode.getValue(), value) > 0)
                    {  
                        currentNode = currentNode.getLeftChild();
                        if (currentNode == null)// если был достигнут конец цепочки,
                        { 
                            parentNode.setLeftChild(newNode); //  то вставить слева и выйти из методы
                            newNode.setParent(parentNode);
                            weightPlacement(rootNode);
                            return;
                        }
                    }
                    else 
                    { // Или направо?           
                        currentNode = currentNode.getRightChild();    
                        if (currentNode == null) // если был достигнут конец цепочки,
                        {                
                            parentNode.setRightChild(newNode);  //то вставить справа  
                            newNode.setParent(parentNode);
                            weightPlacement(rootNode);
                            return; // и выйти
                        }
                    }
                }
            }
        }
        
    }
    
    private void weightPlacement(Node node) 
    {
        if (node != null) 
        {
            weightPlacement(node.getLeftChild());
            weightPlacement(node.getRightChild());
            if (node.getLeftChild()==null || node.getRightChild()==null)
            {
                if (node.getLeftChild()==null && node.getRightChild()==null)
                {
                    node.setWeight(1);
                }
                else 
                    if (node.getLeftChild()==null)
                    {
                        node.setWeight(1+node.getRightChild().getWeight());
                    }
                    else
                        if (node.getRightChild()==null)
                        {
                        node.setWeight(1+node.getLeftChild().getWeight());
                        }
            }
            else 
                    node.setWeight(1+node.getRightChild().getWeight()+node.getLeftChild().getWeight());
        }
    }
    
    // поиск по логическому номеру
    public Node findNodeByInd(int index) 
    {
        int index_node;
        Node search_node = this.rootNode, answer;
      
        if(search_node.getLeftChild()!=null)
            index_node = search_node.getLeftChild().getWeight();
        else
            index_node = 0;
        
        while(true)
        {
            if(index == index_node)
            {
                //answer = search_node.getValue();
                answer = search_node;
                break;
            }
            else
                if(index > index_node)
                {
                    search_node=search_node.getRightChild();
                    try
                    {
                        index_node += search_node.getLeftChild().getWeight() + 1;
                    }
                    catch(NullPointerException e)  
                    {
                        index_node += 1;
                    }
                }
                else
                {
                    search_node=search_node.getLeftChild();
                    try
                    {
                        index_node -= search_node.getRightChild().getWeight() + 1;    
                    }
                    catch(NullPointerException e)  
                    {
                        index_node -= 1;
                    }
                }
        }
        
        return answer;
    }
    
    private boolean deleteNode(Node search_node)
    {
        Node prev_node = search_node.getParent();
          
        if(search_node.getLeftChild()==null && search_node.getRightChild()==null) //удаляемый узел - лист
        {
            if(prev_node==null)
            {
                this.rootNode = null;
            }
            else
                if(prev_node.getLeftChild() == search_node)
                {
                    prev_node.setLeftChild(null);
                }
                else
                    if(prev_node.getRightChild() == search_node)
                    {
                        prev_node.setRightChild(null);
                    }
        }
        else
            if(search_node.getLeftChild() != null && search_node.getRightChild() == null) //удаляемый узел имеет только левого потомка
            {
                if(prev_node==null)
                {
                    this.rootNode = search_node.getLeftChild();
                    this.rootNode.setParent(null);
                }
                else
                    if(prev_node.getLeftChild() == search_node)
                    {
                        prev_node.setLeftChild(search_node.getLeftChild());
                        search_node.getLeftChild().setParent(prev_node);
                    }
                    else
                        if(prev_node.getRightChild() == search_node)
                        {
                            prev_node.setRightChild(search_node.getLeftChild());
                            search_node.getLeftChild().setParent(prev_node);
                        }
            }
            else
                if(search_node.getLeftChild() == null && search_node.getRightChild() != null)
                {
                    if(prev_node==null)
                    {
                        this.rootNode = search_node.getRightChild();
                        this.rootNode.setParent(null);
                    }
                    else
                        if(prev_node.getLeftChild() == search_node)
                        {
                            prev_node.setLeftChild(search_node.getRightChild());
                            search_node.getRightChild().setParent(prev_node);
                        }
                        else
                            if(prev_node.getRightChild() == search_node)
                            {
                                prev_node.setRightChild(search_node.getRightChild());
                                search_node.getRightChild().setParent(prev_node);
                            }
                }
                else
                {
                    return false;
                }
        return true;
    }
 
    @Override
    public boolean deleteNodeByInd(int index) 
    {
        Node search_node=this.rootNode;
        int index_node;
        try
        {
            index_node = search_node.getLeftChild().getWeight();
        }
        catch(NullPointerException e)  
        {
            index_node = 0;
        }       
        while(true)
        {
            if(index==index_node)
            {
                if(deleteNode(search_node)==true)
                {
                    ///+
                }
                else
                    if(search_node.getLeftChild() != null && search_node.getRightChild() != null)
                    {

                        Node del_node = search_node.getRightChild();
                        
                        while(del_node.getLeftChild() != null && del_node.getRightChild() != null)
                        {
                            if(del_node.getLeftChild() != null)
                            {
                                del_node.setWeight(del_node.getWeight()-1);
                                del_node = del_node.getLeftChild();
                            }
                            else
                            {
                                del_node.setWeight(del_node.getWeight()-1);
                                del_node = del_node.getRightChild();
                            }
                        }
                        
                        search_node.setValue(del_node.getValue());
                        search_node.setWeight(search_node.getWeight()-1);
                        
                        deleteNode(del_node);
                    }
                return true;
            }
            else
                if(index > index_node)
                {
                    search_node.setWeight(search_node.getWeight()-1);
                    search_node = search_node.getRightChild();
                    try
                    {
                        index_node += (search_node.getLeftChild().getWeight() + 1);
                    }
                    catch(NullPointerException e)  
                    {
                        index_node += 1;
                    }
                }
                else
                {
                    search_node.setWeight(search_node.getWeight()-1);
                    search_node = search_node.getLeftChild();
                    try
                    {
                        index_node -= (search_node.getRightChild().getWeight() + 1);    
                    }
                    catch(NullPointerException e)  
                    {
                        index_node -= 1;
                    }
                }
        }
    }
     
    @Override
    public void printTree() 
    { // метод для вывода дерева в консоль      
        Stack globalStack = new Stack(); // общий стек для значений дерева      
        globalStack.push(rootNode);      
        int gaps = 32; // начальное значение расстояния между элементами      
        boolean isRowEmpty = false;      
        String separator = "-----------------------------------------------------------------";    
        System.out.println(separator);// черта для указания начала нового дерева     
        while (isRowEmpty == false) 
        {          
            Stack localStack = new Stack(); // локальный стек для задания потомков элемента         
            isRowEmpty = true;
            for (int j = 0; j < gaps; j++)   
                System.out.print(' ');
            while (globalStack.isEmpty() == false) 
            { // покуда в общем стеке есть элементы      
                Node temp = (Node) globalStack.pop(); // берем следующий, при этом удаляя его из стека
                if (temp != null) 
                {
                    System.out.print(temp.getValue() + ":" + temp.getWeight()); // выводим его значение в консоли    
                    localStack.push(temp.getLeftChild()); // соохраняем в локальный стек, наследники текущего элемента   
                    localStack.push(temp.getRightChild());
                    if (temp.getLeftChild() != null || temp.getRightChild() != null)  
                        isRowEmpty = false;              
                }               
                else 
                {                 
                    System.out.print("__");// - если элемент пустой                 
                    localStack.push(null);                 
                    localStack.push(null);              
                }              
                for (int j = 0; j < gaps * 2 - 2; j++)                 
                    System.out.print(' ');          
            }          
            System.out.println();          
            gaps /= 2;// при переходе на следующий уровень расстояние между элементами каждый раз уменьшается          
            while (localStack.isEmpty() == false)              
                globalStack.push(localStack.pop()); // перемещаем все элементы из локального стека в глобальный            
        }    
        System.out.println(separator);// подводим черту  
    }
    
    public void forEach(Action<T> action){
        if(this.rootNode.getWeight() > 0) 
        {
            int counter = 0;
            Node node = this.rootNode;
            do 
            {
                action.toDo(node.value);
                node = findNodeByInd(++counter);
            } while (node != null);
        } 
        else 
        {
            System.out.println("Нет элементов в массиве");
        }
    }
    
    public Iterator<T> iterator() 
    {
        return new Iterator<T>() 
        {
            int counter = 0;
            Node buf = rootNode;

            @Override
            public boolean hasNext() 
            {
                return this.counter < rootNode.getWeight();
            }

            @Override
            public T next() 
            {
                if(counter++ != 0)
                    buf = findNodeByInd(counter-1);
                return buf.value;
            }
        };
    }
    
    private void createVine() 
    {
        Node grandParent = null;
        Node parent = rootNode;
        Node leftChild;

        while (null != parent) 
        {
            leftChild = parent.getLeftChild();
            if (null != leftChild) 
            {
                grandParent = rotateRight(grandParent, parent, leftChild);
                parent = leftChild;
            } else 
            {
                grandParent = parent;
                parent = parent.getRightChild();
            }
        }
    }
    
    private Node rotateRight(Node grandParent, Node parent, Node leftChild) 
    {
        if (null != grandParent) 
        {
            grandParent.setRightChild(leftChild);
        } 
        else 
        {
            setRoot(leftChild);
        }
        parent.setLeftChild(leftChild.getRightChild());
        leftChild.setRightChild(parent);
        return grandParent;
    }
    
    private void rotateLeft(Node grandParent, Node parent, Node rightChild) 
    {
        if (null != grandParent) 
        {
            grandParent.setRightChild(rightChild);
        } 
        else 
        {
            setRoot(rightChild);
        }
        parent.setRightChild(rightChild.getLeftChild());
        rightChild.setLeftChild(parent);
    }

    @Override
    public void createCompleteBST() 
    {
        this.createVine();
        int n = 0;
        for (Node tmp = rootNode; null != tmp; tmp = tmp.getRightChild()) 
        {
            n++;
        }
        int m = greatestPowerOf2LessThanN(n + 1) - 1;
        makeRotations(n - m);

        while (m > 1) 
        {
            makeRotations(m /= 2);
        }
        
        weightPlacement(rootNode);
    }   
    
    private int greatestPowerOf2LessThanN(int n) 
    {
        int ndx = 0;
        while (1 < n) 
        {
            n = (n >> 1);
            ndx++;
        }
        return (1 << ndx);//2^x
    }
    
    private void makeRotations(int bound) 
    {
        Node grandParent = null;
        Node parent = rootNode;
        Node child = rootNode.getRightChild();
        for (; bound > 0; bound--) 
        {
            try 
            {
                if (null != child) 
                {
                    rotateLeft(grandParent, parent, child);
                    grandParent = child;
                    parent = grandParent.getRightChild();
                    child = parent.getRightChild();
                } 
                else 
                {
                    break;
                }
            } 
            catch (NullPointerException convenient) 
            {
                break;
            }
        }
    }
    
    public void save(File file) throws IOException 
    {
        ArrayList<Node> lists=new ArrayList<>();
        Queue<Node> queue=new LinkedList<>();
        queue.offer(rootNode);
        while(!queue.isEmpty())
        {
            Node tree=queue.poll();
            if(tree.getLeftChild() != null)
                queue.offer(tree.getLeftChild());
            if(tree.getRightChild() != null)
                queue.offer(tree.getRightChild());
            lists.add(tree);
        }
        
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false));

        oos.writeInt(rootNode.getWeight());
        for(int i = 0; i < lists.size(); ++i)
            oos.writeObject(lists.get(i));
        
        oos.close();
    }

    public void load(File file, Comparator comparator) throws IOException, ClassNotFoundException 
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

        this.rootNode = null;

        int counts = ois.readInt();
        for(int i = 0; i < counts; ++i) 
        {
            Node tmp = (Node) ois.readObject();
            insertNode(tmp.getValue(), comparator);
        }
    }
}