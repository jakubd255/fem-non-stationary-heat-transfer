package pl.jakubdudek.fem.globaldata;

import lombok.Getter;
import pl.jakubdudek.fem.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;


@Getter
public class GlobalData {
    private int simulationTime;
    private int simulationStepTime;

    private int conductivity;
    private int alfa;

    private int ambientTemp;
    private int initialTemp;

    private int density;
    private int specificHeat;

    private int nodesNumber;
    private int elementsNumber;

    private final Grid grid;
    private int[] borderConditions;

    public GlobalData(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        Node[] nodes = {};
        Element[] elements = {};

        while(scanner.hasNext()){
            String[] line = scanner.nextLine().split(" ");

            switch (line[0]){
                case "SimulationTime": simulationTime = Integer.parseInt(line[1]); break;
                case "SimulationStepTime": simulationStepTime = Integer.parseInt(line[1]); break;
                case "Conductivity": conductivity = Integer.parseInt(line[1]); break;
                case "Alfa": alfa = Integer.parseInt(line[1]); break;
                case "Tot": ambientTemp = Integer.parseInt(line[1]); break;
                case "InitialTemp": initialTemp = Integer.parseInt(line[1]); break;
                case "Density": density = Integer.parseInt(line[1]); break;
                case "SpecificHeat": specificHeat = Integer.parseInt(line[1]); break;
                case "Nodes": nodesNumber = Integer.parseInt(line[2]); break;
                case "Elements": elementsNumber = Integer.parseInt(line[2]); break;
                case "*Node": {
                    nodes = new Node[nodesNumber];

                    for(int i=0; i<nodesNumber; i++){
                        String nodeLine[] = scanner.nextLine().split(",");

                        //int index = Integer.parseInt(nodeLine[0].trim());
                        double x = Double.parseDouble(nodeLine[1]);
                        double y = Double.parseDouble(nodeLine[2]);

                        nodes[i] = new Node(x, y);
                    }
                    break;
                }
                case "*Element,": {
                    elements = new Element[elementsNumber];

                    for(int i=0; i<elementsNumber; i++){
                        String elementLine[] = scanner.nextLine().split(",");

                        int n1 = Integer.parseInt(elementLine[1].trim());
                        int n2 = Integer.parseInt(elementLine[2].trim());
                        int n3 = Integer.parseInt(elementLine[3].trim());
                        int n4 = Integer.parseInt(elementLine[4].trim());

                        elements[i] = new Element(n1, n2, n3, n4);
                    }
                    break;
                }
                case "*BC": {
                    String borderConditionsLine[] = scanner.nextLine().split(",");
                    borderConditions = new int[borderConditionsLine.length];

                    for(int i=0; i<borderConditionsLine.length; i++)
                        borderConditions[i] = Integer.parseInt(borderConditionsLine[i].trim());
                    break;
                }
            }
        }

        for(int BC : borderConditions) {
            nodes[BC-1].setBC(1);
        }

        grid = new Grid(nodes, elements);
    }

    public void print(){
        //Global Data
        System.out.println("Czas symulacji: "+simulationTime);
        System.out.println("Krok czasowy: "+simulationStepTime);
        System.out.println("Przewodnictwo cieplne: "+conductivity);
        System.out.println("Alfa: "+alfa);
        System.out.println("Temperatura otoczenia: "+ambientTemp);
        System.out.println("Temperatura początkowa: "+initialTemp);
        System.out.println("Gęstość: "+density);
        System.out.println("Ciepło właściwe: "+specificHeat);
        System.out.println("Temperatura otoczenia: "+ambientTemp);
        System.out.println("Liczba węzłów: "+grid.getNodes().length);
        System.out.println("Liczba elementów: "+grid.getElements().length);

        //Węzły
        System.out.println();
        System.out.println("Węzły:");
        for(Node node : grid.getNodes())
            System.out.println(node);

        //Elementy
        System.out.println();
        System.out.println("Elementy:");
        for(Element element : grid.getElements())
            element.print();
    }

    public Node[] getNodesOfElement(int i) {
        return grid.getNodesOfElement(i);
    }
}