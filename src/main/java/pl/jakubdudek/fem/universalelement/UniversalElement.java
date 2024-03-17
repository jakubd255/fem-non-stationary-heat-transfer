package pl.jakubdudek.fem.universalelement;

import lombok.Getter;
import pl.jakubdudek.fem.gaussintegration.IntegrationScheme;
import pl.jakubdudek.fem.matrix.Matrix;

import java.util.stream.DoubleStream;

@Getter
public class UniversalElement {
    private final double[] w;

    private final double[][] N;
    private final double[][] dNdKsi;
    private final double[][] dNdEta;

    private final Surface[] surfaces;

    public UniversalElement() {
        double[] ksi = new double[IntegrationScheme.getPointsSquare()];
        double[] eta = new double[IntegrationScheme.getPointsSquare()];
        w = new double[IntegrationScheme.getPointsSquare()];

        N = new double[IntegrationScheme.getPointsSquare()][4];
        dNdKsi = new double[IntegrationScheme.getPointsSquare()][4];
        dNdEta = new double[IntegrationScheme.getPointsSquare()][4];

        surfaces = new Surface[]{
                new Surface(
                        IntegrationScheme.getX(),
                        DoubleStream.generate(() -> -1.0).limit(IntegrationScheme.getPoints()).toArray()
                ),
                new Surface(
                        DoubleStream.generate(() -> 1.0).limit(IntegrationScheme.getPoints()).toArray(),
                        IntegrationScheme.getX()
                ),
                new Surface(
                        Matrix.multiplyVector(IntegrationScheme.getX(), -1.0),
                        DoubleStream.generate(() -> 1.0).limit(IntegrationScheme.getPoints()).toArray()
                ),
                new Surface(
                        DoubleStream.generate(() -> -1.0).limit(IntegrationScheme.getPoints()).toArray(),
                        Matrix.multiplyVector(IntegrationScheme.getX(), -1.0)
                ),
        };

        for(int i = 0; i<(IntegrationScheme.getPointsSquare()); i += IntegrationScheme.getPoints()) {
            for(int j = 0; j< IntegrationScheme.getPoints(); j++) {
                ksi[i+j] = IntegrationScheme.getX()[j];
                eta[i+j] = IntegrationScheme.getX()[i/IntegrationScheme.getPoints()];
                w[i+j] = IntegrationScheme.getW()[j] * IntegrationScheme.getW()[i/IntegrationScheme.getPoints()];
            }
        }

        for(int i = 0; i<(IntegrationScheme.getPointsSquare()) ; i++) {
            N[i][0] = 0.25 * (1- ksi[i]) * (1- eta[i]);
            N[i][1] = 0.25 * (1+ ksi[i]) * (1- eta[i]);
            N[i][2] = 0.25 * (1+ ksi[i]) * (1+ eta[i]);
            N[i][3] = 0.25 * (1- ksi[i]) * (1+ eta[i]);

            dNdKsi[i][0] = -0.25 * (1 - eta[i]);
            dNdKsi[i][1] = 0.25 * (1 - eta[i]);
            dNdKsi[i][2] = 0.25 * (1 + eta[i]);
            dNdKsi[i][3] = -0.25 * (1 + eta[i]);

            dNdEta[i][0] = -0.25 * (1 - ksi[i]);
            dNdEta[i][1] = -0.25 * (1 + ksi[i]);
            dNdEta[i][2] = 0.25 * (1 + ksi[i]);
            dNdEta[i][3] = 0.25 * (1 - ksi[i]);
        }
    }

    public void printdNdKsi(){
        Matrix.print(dNdKsi);
    }

    public void printdNdEta(){
        Matrix.print(dNdEta);
    }

    public double getW(int index) {
        return w[index];
    }

    public Surface getSurface(int index) {
        return surfaces[index];
    }
}