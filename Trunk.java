package kitpolw1;

public interface Trunk<T>
{
    void insertNode(T value, Comparator comparator);
    boolean deleteNodeByInd(int index);
    void printTree();
    void createCompleteBST();
}