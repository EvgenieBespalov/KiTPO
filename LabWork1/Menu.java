package kitpolw1;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu 
{
    private Tree<Object> tree;
    private TypeBuilder typeBuilder;

    Menu() 
    {
        tree = new Tree();
    }

    public void start() 
    {
        boolean isExit = false;
        int input;

        Scanner in = new Scanner(System.in);

        while (true) 
        {
            System.out.println("Выберите тип данных");
            for (String type : TypeFactory.getTypeNameList())
                System.out.println(type);
            System.out.print(">> ");
            String str = in.nextLine();
            try 
            {
                typeBuilder = TypeFactory.getBuilder(Types.valueOf(str));
            } 
            catch (IllegalArgumentException exception) 
            {
                System.out.println(exception.getMessage());
            }
            if (typeBuilder == null)
                System.out.println("Попробуйте снова");
            else
                break;
        }

        printMenu();

        while (!isExit) 
        {
            System.out.print(">> ");

            try 
            {

                input = in.nextInt();

                switch (input) 
                {
                    case 1:
                    {
                        tree.forEach(System.out::println);
                    }
                    break;
                    case 2: 
                    {
                        System.out.print("Введите количество: ");
                        int count = in.nextInt();
                        for (int i = 0; i < count; i++)
                            tree.insertNode(typeBuilder.create(), typeBuilder.getTypeComparator());
                        System.out.println(count + " объектов были успешно добавлены");
                    }
                    break;
                    case 3:
                    {
                        System.out.print("Введите объект: ");
                        if (typeBuilder.getTypeName().equals("Integer")) 
                        {
                            int data = in.nextInt();
                            tree.insertNode(data, typeBuilder.getTypeComparator());
                        } 
                        else 
                            if (typeBuilder.getTypeName().equals("String")) 
                            {
                                in.nextLine();
                                String data = in.nextLine();
                                tree.insertNode(data, typeBuilder.getTypeComparator());
                            } 
                            else 
                            {
                                System.out.println("Ошибка в выбранном типе!");
                            }
                    }
                    break;
                    case 4: 
                    {
                        System.out.print("Введите индекс: ");
                        int index = in.nextInt();
                        tree.deleteNodeByInd(index);
                    }
                    break;
                    case 5: 
                    {
                        System.out.print("Введите индекс: ");
                        int index = in.nextInt();
                        System.out.println("Полученный объект: " + tree.findNodeByInd(index));
                    }
                    break;
                    case 6: 
                    {
                        long start = System.nanoTime();
                        tree.createCompleteBST();
                        double time = (double)(System.nanoTime() - start) / 1_000_000_000.0;
                        System.out.printf("Дерево было успешно сбалансировано за %.3f cек\n", time);
                    }
                    break;
                    case 7:
                    {
                        tree.save(new File(typeBuilder.getTypeName() + ".data"));
                        System.out.println("Данные были успешно сохранены в файл");
                    }
                    break;
                    case 8:
                    {
                        tree.load(new File(typeBuilder.getTypeName() + ".data"), typeBuilder.getTypeComparator());
                        System.out.println("Данные были успешно загружены из файла");
                    }
                    break;                    
                    case 9: 
                    {
                        tree.printTree();
                    }
                    break;
                    case 0: 
                        isExit = true;
                        break;
                    default: System.out.println("Попробуйте снова");
                }
            }
            catch (NullPointerException | IOException | ClassNotFoundException exception) 
            {
                System.out.println(exception.getMessage());
            }
            catch (InputMismatchException exception) 
            {
                System.out.println("Некорректный ввод");
                in.nextLine();
            }
        }
    }

    private void printMenu() {
        String[] str = new String[]{
                "Меню",
                "[1]  Вывести все объекты",
                "[2]  Добавить n-ое количество объектов",
                "[3]  Добавить объект",
                "[4]  Удалить объект по индексу",
                "[5]  Получить объект по индексу",
                "[6]  Сбалансировать дерево",
                "[7]  Сохранить объекты",
                "[8] Загрузить объекты",
                "[9] Вывести в виде дерева",
                "[0]  Выход из программы",
                };

        for(String string : str) 
        {
            System.out.println(string);
        }
    }
}
