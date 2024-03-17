package pl.jakubdudek.fem.universalelement;

import lombok.Getter;
import pl.jakubdudek.fem.gaussintegration.IntegrationScheme;

@Getter
public class Surface {
    private final double[][] N;

    public Surface(double[] ksi, double[] eta) {
        N = new double[IntegrationScheme.getPoints()][4];

        for(int i = 0; i< IntegrationScheme.getPoints(); i++) {
            N[i][0] = 0.25*(1 - ksi[i])*(1 - eta[i]);
            N[i][1] = 0.25*(1 + ksi[i])*(1 - eta[i]);
            N[i][2] = 0.25*(1 + ksi[i])*(1 + eta[i]);
            N[i][3] = 0.25*(1 - ksi[i])*(1 + eta[i]);
        }
    }
}
