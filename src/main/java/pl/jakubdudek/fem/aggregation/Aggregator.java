package pl.jakubdudek.fem.aggregation;

import lombok.Getter;
import pl.jakubdudek.fem.globaldata.Element;
import pl.jakubdudek.fem.globaldata.GlobalData;
import pl.jakubdudek.fem.matrix.Matrix;

public class Aggregator {
    @Getter
    private double[][] H;
    @Getter
    private double[][] C;
    @Getter
    private double[] P;

    private GlobalData globalData;

    public Aggregator(GlobalData globalData) {
        int size = globalData.getGrid().getNodes().length;

        this.H = new double[size][size];
        this.C = new double[size][size];
        this.P = new double[size];
    }

    public void agregateElement(Element element) {
        double[][] H = element.getH();
        double[][] HBC = element.getHBC();
        double[][] C = element.getC();
        double[] P = element.getP();

        for(int i=0; i<element.nodes.length; i++) {
            int iG = element.nodes[i];

            for(int j=0; j<element.nodes.length; j++) {
                int jG = element.nodes[j];

                this.H[iG][jG] += H[i][j] + HBC[i][j];
                this.C[iG][jG] += C[i][j];
            }

            this.P[iG] += P[i];
        }
    }
}
