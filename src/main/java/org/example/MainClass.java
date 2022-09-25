package org.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class MainClass {

    final char[][] finalState = {{'1','2','3'},
            {'8',' ','4'},
            {'7','6','5'}};

    final char[][] initState = {{'4','8','1'},
            {' ','3','6'},
            {'2','7','5'}};
    LinkedList<Node> traversal = new LinkedList<>();
    static int stepCount=0, nodeCount=0;

    public class Node {
        char[][] state;
        Node parent;
        int[] action;
        int depth;
        int h;

        Node(char[][] state, Node parent, int[] action) {
            this.state = state;
            this.parent = parent;
            this.action = action;
            this.depth = parent != null ? parent.depth + 1 : 1;
        }

        public void printNode() {
            System.out.print("\tГлубина: ");
            System.out.print(depth+"\n");
            for (int i = 0; i < 3; i++) {
                System.out.print("\t\t");
                for (int j = 0; j < 3; j++)
                    System.out.print(state[i][j]+" ");
                System.out.println("");
            }
            System.out.println("");
        }
    }

    public static void main (String[] args) {
        int selection;
        Scanner input = new Scanner(System.in);
        MainClass mainClass = new MainClass();
        System.out.print("Выбери алгоритм:\n\t1. Жадный поиск\n\t2. А* поиск\n\n");
        selection = input.nextInt();
        System.out.print("Выбери режим:\n\t1. Автомат\n\t2. Пошаговый\n\n");
        //algoritm selection
        switch (selection){
            case 1:
                selection = input.nextInt();
                System.out.println("Выберите эвристическую функцию:\n\t1. h1\n\t2. h2\n\n");
                //mode selection
                switch (selection)
                {
                    case 1:
                        selection = input.nextInt();
                        //function selection
                        switch (selection)
                        {
                            case 1:
                                System.out.println('\n'+mainClass.greedySearch(false,false));
                                break;
                            case 2:
                                System.out.println('\n'+mainClass.greedySearch(false,true));
                                break;
                            default:
                                System.out.println("Нет такого пункта меню");
                                break;
                        }
                        break;
                    case 2:
                        selection = input.nextInt();
                        //function selection
                        switch (selection)
                        {
                            case 1:
                                System.out.println('\n'+mainClass.greedySearch(true,false));
                                break;
                            case 2:
                                System.out.println('\n'+mainClass.greedySearch(true,true));
                                break;
                            default:
                                System.out.println("Нет такого пункта меню");
                                break;
                        };
                        break;
                    default:
                        System.out.println("Нет такого пункта меню");
                        break;
                }
                break;

            case 2:
                selection = input.nextInt();
                System.out.println("Выберите эвристическую функцию:\n\t1. h1\n\t2. h2\n");
                //mode selection
                switch (selection)
                {
                    case 1:
                        selection = input.nextInt();
                        //function selection
                        switch (selection)
                        {
                            case 1:
                                System.out.println('\n'+mainClass.aSearch(false,false));
                                break;
                            case 2:
                                System.out.println('\n'+mainClass.aSearch(false,true));
                                break;
                            default:
                                System.out.println("Нет такого пункта меню");
                                break;
                        }

                        break;
                    case 2:
                        selection = input.nextInt();
                        //function selection
                        switch (selection)
                        {
                            case 1:
                                System.out.println('\n'+mainClass.aSearch(true,false));
                                break;
                            case 2:
                                System.out.println('\n'+mainClass.aSearch(true,true));
                                break;
                            default:
                                System.out.println("Нет такого пункта меню");
                                break;
                        };
                        break;
                    default:
                        System.out.println("Нет такого пункта меню");
                        break;
                }
                break;

            default:
                System.out.println("Нет такого пункта меню");
                break;
        }
        System.out.println("Шагов сделано: " + stepCount);
        System.out.println("Единицы памяти: " + nodeCount);
    }

    public String greedySearch(boolean stepByStep, boolean selectedFunc) {
        LinkedList<Node> list = new LinkedList<>();
        Node root = new Node(initState, null, null);
        Node current;
        if (!selectedFunc) {
            root.h =h1(root.state, finalState);
        }
        else {
            root.h =h2(root.state, finalState);
        }
        list.add(root);
        nodeCount++;
        while(!list.isEmpty()) {
            current = list.removeLast();
            stepCount++;
            //if step by step, then output
            if (stepByStep)
            {
                System.out.println("Текущий узел: ");
                System.out.println("\th=" + current.h);
                current.printNode();
                System.out.println("----------------------------------");
            }
            traversal.add(current);
            if (ifReachedTarget(current.state)) {
                printPath(current, false);
                return "Решение найдено";
            }
            else {
                Stack<Node> newStates = findPossibleStates(current);
                if (stepByStep)
                    System.out.println("\nВарианты перемещений: ");
                while(!newStates.isEmpty()) {
                    Node newNode = newStates.pop();
                    if (!ifRepeated(newNode.state, traversal) ) {
                        if (!selectedFunc) {
                            newNode.h =h1(newNode.state, finalState);
                        }
                        else {
                            newNode.h =h2(newNode.state, finalState);
                        }
                        boolean insertion = false;
                        for (Node i : list) {
                            if (i.h < newNode.h) {
                                list.add(list.indexOf(i),newNode);
                                insertion = true;
                                break;
                            }
                        }
                        if (!insertion) {
                            list.add(newNode);
                        }
                        nodeCount++;
                        if (stepByStep) {
                            System.out.println("  Разрешенное: ");
                            newNode.printNode();
                            System.out.println("\th=" + newNode.h);
                        }
                    }
                    else {
                        if (stepByStep) {
                            System.out.println("\n  Запрещенное: ");
                            newNode.printNode();
                        }
                    }
                }
                if (stepByStep) {
                    System.out.println("----------------------------------");
                    System.out.println("Состояние 'каймы': " + list.size());
                    System.out.println("Следующая вершина: ");
                    list.getLast().printNode();
                    System.out.println("Нажмите Enter, чтобы перейти далее>>");
                    //new java.util.Scanner(System.in).nextLine();
                    try {
                        System.in.skip(System.in.available());
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "Решение не найдено";
    }

    public String aSearch(boolean stepByStep, boolean selectedFunc) {
        LinkedList<Node> list = new LinkedList<>();
        Node root = new Node(initState, null, null);
        Node current;
        if (!selectedFunc) {
            root.h =h1(root.state, finalState);
        }
        else {
            root.h =h2(root.state, finalState);
        }
        list.add(root);
        nodeCount++;
        while(!list.isEmpty()) {
            current = list.removeLast();
            stepCount++;
            if (stepByStep)
            {
                System.out.println("Текущий узел: ");
                System.out.println("\tf=g+h=" + current.depth + "+" +
                        current.h + "=" + (current.depth + current.h));
                current.printNode();
                System.out.println("----------------------------------");
            }
            traversal.add(current);
            if (ifReachedTarget(current.state)) {
                printPath(current,true);
                return "Решение найдено";
            }
            else {
                Stack<Node> newStates = findPossibleStates(current);
                if (stepByStep)
                    System.out.println("\nВарианты перемещений: ");
                while(!newStates.isEmpty()) {
                    Node newNode = newStates.pop();
                    if (!ifRepeated(newNode.state, traversal) ) {
                        if (!selectedFunc) {
                            newNode.h =h1(newNode.state, finalState);
                        }
                        else {
                            newNode.h =h2(newNode.state, finalState);
                        }
                        boolean insertion = false;
                        int f = Math.max(newNode.h + newNode.depth, current.h + current.depth);
                        for (Node i : list) {
                            if (i.depth + i.h < f) {
                                list.add(list.indexOf(i),newNode);
                                insertion = true;
                                break;
                            }
                        }
                        if (!insertion) {
                            list.add(newNode);
                        }
                        nodeCount++;
                        if (stepByStep) {
                            System.out.println("  Разрешенное: ");
                            newNode.printNode();
                            System.out.println("\tf=g+h=" + newNode.depth + "+" +
                                    newNode.h + "=" + (newNode.depth + newNode.h));
                        }
                    }
                    else {
                        if (stepByStep) {
                            System.out.println("\n  Запрещенное: ");
                            newNode.printNode();
                        }
                    }
                }
                if (stepByStep) {
                    System.out.println("----------------------------------");
                    System.out.println("Состояние 'каймы': " + list.size());
                    System.out.println("Следующая вершина: ");
                    list.getLast().printNode();
                    System.out.println("Нажмите Enter, чтобы перейти далее>>");
                    //new java.util.Scanner(System.in).nextLine();
                    try {
                        System.in.skip(System.in.available());
                        System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "Решение не найдено";
    }

    public Stack<Node> findPossibleStates(Node currentNode) {
        Stack<Node> possibleStates = new Stack<Node>();
        int[] coordinates = findCoordinates(currentNode.state, ' ');
        int i = coordinates[0];
        int j = coordinates[1];

        //If possible to move left
        if (j - 1 >= 0){
            optionNode(possibleStates, i, j - 1, coordinates,currentNode);
        }
        //If possible to move right
        if (j + 1 <= 2){
            optionNode(possibleStates, i, j + 1, coordinates, currentNode);
        }
        //If possible to move up
        if (i - 1 >= 0){
            optionNode(possibleStates, i - 1, j, coordinates, currentNode);
        }
        //If possible to move down
        if (i + 1 <= 2){
            optionNode(possibleStates, i + 1, j, coordinates,currentNode);
        }
        return possibleStates;
    }

    public Stack<Node> optionNode(Stack<Node> possibleStates, int i, int j, int[] coordinates, Node currentNode){
        char[][] newState = copy2DArray(currentNode.state);
        char temp = ' ';
        newState[coordinates[0]][coordinates[1]] = newState[i][j];
        newState[i][j] = temp;
        Node newNode = new Node(newState,currentNode,new int[]{i,j,i,j});
        possibleStates.push(newNode);
        return possibleStates;
    }

    public int[] findCoordinates(char[][] state, char object) {
        int col = 0;
        int row = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j]==object) {
                    col = i;
                    row = j;
                }
            }
        }
        return new int[] {col,row};
    }

    public char[][] copy2DArray(char[][] from) {
        char[][] newArray = new char[3][3];
        for (int i = 0; i < 3; i++) {
            newArray[i] = Arrays.copyOf(from[i], 3);
        }
        return newArray;
    }

    public boolean ifRepeated(char[][] state, LinkedList<Node> traversal) {
        for (Node i:traversal) {
            if (ifStateEquals(i.state, state))
                return true;
        }
        return false;
    }

    public boolean ifReachedTarget(char[][] state) {
        boolean ifEquals = Arrays.equals(state[0], finalState[0])
                && Arrays.equals(state[1], finalState[1])
                && Arrays.equals(state[2], finalState[2]);
        return ifEquals;
    }

    public boolean ifStateEquals(char[][] state1, char[][] state2) {
        boolean ifEquals = Arrays.equals(state1[0],state2[0])
                && Arrays.equals(state1[1],state2[1])
                && Arrays.equals(state1[2],state2[2]);
        return ifEquals;
    }

    public void printPath(Node resultNode, boolean selectedFunc) {
        Node tempNode = resultNode;
        while (tempNode!=null) {
            for (int i=0; i<10000 && tempNode!=null;i++) {
                tempNode.printNode();
                if (!selectedFunc) {
                    System.out.println("h="+tempNode.h);
                }
                else {
                    System.out.println("\tf=g+h=" + tempNode.depth + "+" +
                            tempNode.h + "=" + (tempNode.depth + tempNode.h));
                }
                if (tempNode.parent!=null) {
                    System.out.println("\n\t\t▲");
                    System.out.println("\t\t|");
                    System.out.println("\t\t|");
                }
                tempNode=tempNode.parent;
            }
            if (tempNode!=null) {
                System.out.println("Чтобы перейти к другой части вывода, нажмите Enter");
                try {
                    System.in.skip(System.in.available());
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int h1(char[][] state1, char[][] state2) {
        int dist = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state1[i][j] != state2[i][j])
                    dist++;
            }
        }
        return dist;
    }

    public int h2(char[][] state1, char[][] state2) {
        int distManhattan = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] coordinates = findCoordinates(state2, state1[i][j]);
                distManhattan += Math.abs(i - coordinates[0]) + Math.abs(j - coordinates[1]);
            }
        }
        return distManhattan;
    }
}
