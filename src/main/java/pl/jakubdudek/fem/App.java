package pl.jakubdudek.fem;

import pl.jakubdudek.fem.gaussintegration.GaussIntegrator;
import pl.jakubdudek.fem.gaussintegration.IntegrationScheme;
import pl.jakubdudek.fem.globaldata.GlobalData;
import pl.jakubdudek.fem.aggregation.Aggregator;
import pl.jakubdudek.fem.simulation.Simulator;

import java.io.File;
import java.io.IOException;

public class App {
    public static int FILE_INDEX = 3;

    public static void main(String[] args) throws IOException {
        IntegrationScheme.setPoints(4);

        String[] files = new String[]{
                "grid1.txt",
                "grid2.txt",
                "grid3.txt",
                "grid4.txt"
        };
        File file = new File("src/main/resources/"+files[FILE_INDEX]);

        GlobalData globalData = new GlobalData(file);

        Aggregator aggregator = new Aggregator(globalData);

        GaussIntegrator integrator = new GaussIntegrator(globalData, aggregator);
        integrator.integrate();

        Simulator simulator = new Simulator(globalData, aggregator);
        simulator.simulate();
    }
}