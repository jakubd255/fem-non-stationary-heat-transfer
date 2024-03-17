package pl.jakubdudek.fem.gaussintegration;

import pl.jakubdudek.fem.aggregation.Aggregator;
import pl.jakubdudek.fem.globaldata.Element;
import pl.jakubdudek.fem.globaldata.GlobalData;
import pl.jakubdudek.fem.globaldata.Node;
import pl.jakubdudek.fem.matrix.Matrix;
import pl.jakubdudek.fem.universalelement.Surface;
import pl.jakubdudek.fem.universalelement.UniversalElement;

public class GaussIntegrator {
    private GlobalData globalData;
    private Aggregator aggregator;
    private UniversalElement universalElement;

    public GaussIntegrator(GlobalData globalData, Aggregator aggregator) {
        this.globalData = globalData;
        this.aggregator = aggregator;
        this.universalElement =  new UniversalElement();
    }

    public void integrate() {
        double[][] N = universalElement.getN();
        double[][] dNdKsi = universalElement.getDNdKsi();
        double[][] dNdEta = universalElement.getDNdEta();

        double[][] HG = new double[4][4];

        double k = globalData.getConductivity();
        double alfa = globalData.getAlfa();
        double ambientTemp = globalData.getAmbientTemp();
        double density = globalData.getDensity();
        double specificHeat = globalData.getSpecificHeat();

        //Dla każdego elementu
        for(int elementIndex=0; elementIndex<globalData.getElementsNumber(); elementIndex++) {
            double[][] H = new double[4][4];
            double[][] HBC = new double[4][4];
            double[] P = new double[4];
            double[][] C = new double[4][4];

            Node[] nodes = globalData.getNodesOfElement(elementIndex);

            //Dla każdego punktu całkowania
            for(int i = 0; i<(IntegrationScheme.getPointsSquare()); i++) {
                double dXdKsi = dNdKsi[i][0]*nodes[0].getX() + dNdKsi[i][1]*nodes[1].getX() + dNdKsi[i][2]*nodes[2].getX() + dNdKsi[i][3]*nodes[3].getX();
                double dXdEta = dNdEta[i][0]*nodes[0].getX() + dNdEta[i][1]*nodes[1].getX() + dNdEta[i][2]*nodes[2].getX() + dNdEta[i][3]*nodes[3].getX();
                double dYdKsi = dNdKsi[i][0]*nodes[0].getY() + dNdKsi[i][1]*nodes[1].getY() + dNdKsi[i][2]*nodes[2].getY() + dNdKsi[i][3]*nodes[3].getY();
                double dYdEta = dNdEta[i][0]*nodes[0].getY() + dNdEta[i][1]*nodes[1].getY() + dNdEta[i][2]*nodes[2].getY() + dNdEta[i][3]*nodes[3].getY();

                double[][] J = new double[][]{{dXdKsi, dYdKsi}, {dXdEta, dYdEta}};

                double[][] invertedJ = Matrix.inverse(J);
                double detJ = Matrix.det(J);

                double[] dNdX = new double[4];
                double[] dNdY = new double[4];

                for(int j=0; j<4; j++) {
                    dNdX[j] = invertedJ[0][0]*dNdKsi[i][j] + invertedJ[0][1]*dNdEta[i][j];
                    dNdY[j] = invertedJ[1][0]*dNdKsi[i][j] + invertedJ[1][1]*dNdEta[i][j];
                }

                double[][] Hi = Matrix.sum(Matrix.multiplyToMatrix(dNdX, dNdX), Matrix.multiplyToMatrix(dNdY, dNdY));
                Hi = Matrix.multiply(Hi, k*detJ*universalElement.getW(i));
                H = Matrix.sum(H, Hi);

                double[][] Ci = Matrix.multiply(Matrix.multiplyToMatrix(N[i], N[i]), density*specificHeat*detJ*universalElement.getW(i));
                C = Matrix.sum(C, Ci);
            }

            //Dla każdej ściany
            for(int b=0; b<4; b++) {
                //Uproszczony detJ - stosunek długości boku w układzie globalnym do długości w układzie lokalnym (1 -(-1) = 2)
                double detJ = Math.sqrt(Math.pow(nodes[b%4].getX() - nodes[(b+1)%4].getX(), 2) + Math.pow(nodes[b%4].getY() - nodes[(b+1)%4].getY(), 2))/2;

                Surface surface = universalElement.getSurface(b);

                boolean isBC = (nodes[b%4].getBC() == 1 && nodes[(b+1)%4].getBC() == 1);
                if(isBC) {
                    double[][] HBCi = new double[4][4];
                    double[] Pi = new double[4];

                    for(int j = 0; j< IntegrationScheme.getPoints(); j++) {
                        HBCi = Matrix.sum(HBCi, Matrix.multiply(Matrix.multiplyToMatrix(surface.getN()[j], surface.getN()[j]), IntegrationScheme.getW()[j]*alfa*detJ));
                        Pi = Matrix.sumVectors(Pi, Matrix.multiplyVector(surface.getN()[j], ambientTemp*alfa*detJ* IntegrationScheme.getW()[j]));
                    }
                    HBC = Matrix.sum(HBC, HBCi);
                    P = Matrix.sumVectors(P, Pi);
                }
            }

            Element element = globalData.getGrid().getElements()[elementIndex];

            element.setH(H);
            element.setHBC(HBC);
            element.setP(P);
            element.setC(C);

            aggregator.agregateElement(element);
        }
    }
}