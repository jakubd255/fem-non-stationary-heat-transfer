package pl.jakubdudek.fem.simulation;

import lombok.Getter;
import pl.jakubdudek.fem.aggregation.Aggregator;
import pl.jakubdudek.fem.globaldata.GlobalData;
import pl.jakubdudek.fem.matrix.Matrix;
import pl.jakubdudek.fem.paraview.VTKWriter;

import java.io.IOException;

public class Simulator {
    @Getter
    private double[] t0;

    private int dTau ;

    private Aggregator aggregator;
    private GlobalData globalData;
    private VTKWriter vtkWriter;

    public Simulator(GlobalData globalData, Aggregator aggregator) {
        this.aggregator = aggregator;
        this.globalData = globalData;
        this.vtkWriter = new VTKWriter(globalData);

        int size = globalData.getGrid().getNodes().length;

        this.t0 = new double[size];
        this.dTau = globalData.getSimulationStepTime();

        for(int i=0; i<size; i++) {
            this.t0[i] = (double)globalData.getInitialTemp();
        }
    }

    private void countTemperature(int time) throws IOException {
        double[][] dividedC = Matrix.multiply(aggregator.getC(), 1.0/(double)dTau);

        double[][] A = Matrix.sum(aggregator.getH(), dividedC);
        double[] B = Matrix.sumVectors(Matrix.multiplyByVector(dividedC, t0), aggregator.getP());

        double[] solution = Matrix.gauss(A, B);

        double min = solution[0];
        double max = solution[0];

        for(int i=0; i<solution.length; i++) {
            if(solution[i] > max) max = solution[i];
            if(solution[i] < min) min = solution[i];
        }

        System.out.printf("Time: %d    Min = %f   Max = %f\n", time, min, max);
        vtkWriter.write(solution, time);

        t0 = solution;
    }

    public void simulate() throws IOException {
        int tau0 = dTau;
        int tauEnd = globalData.getSimulationTime();

        while(tau0 <= tauEnd) {
            countTemperature(tau0);
            tau0 += dTau;
        }
    }
}
