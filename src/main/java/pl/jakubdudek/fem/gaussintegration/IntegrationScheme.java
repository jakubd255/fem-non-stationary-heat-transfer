package pl.jakubdudek.fem.gaussintegration;

import lombok.Getter;

public class IntegrationScheme {
    @Getter
    private static int points = 2;

    @Getter
    private static double[] X = new double[4];

    @Getter
    private static double[] W = new double[4];

    public static int getPointsSquare() {
        return points*points;
    }

    public static void setPoints(int points) {
        if(points == 2) {
            X = new double[]{
                    -1.0/Math.sqrt(3.0),
                    1.0/Math.sqrt(3.0),
                    /*-0.5774,
                    0.5774*/
            };
            W = new double[]{
                    1.0,
                    1.0
            };
        }
        else if(points == 3) {
            X = new double[]{
                    -Math.sqrt(3.0/5.0),
                    //-0.7746,
                    0.0,
                    //0.7746
                    Math.sqrt(3.0/5.0)
            };
            W = new double[]{
                    5.0/9.0,
                    8.0/9.0,
                    5.0/9.0
            };
        }
        else if(points == 4) {
            X = new double[]{

                    /*-0.9208,
                    -0.5108,
                    0.5108,
                    0.9208*/
                    -Math.sqrt(3.0/7.0 + (2.0/7.0)*Math.sqrt(6.0/5.0)),
                    -Math.sqrt(3.0/7.0 - (2.0/7.0)*Math.sqrt(6.0/5.0)),
                    Math.sqrt(3.0/7.0 - (2.0/7.0)*Math.sqrt(6.0/5.0)),
                    Math.sqrt(3.0/7.0 + (2.0/7.0)*Math.sqrt(6.0/5.0))
            };
            W = new double[]{
                    /*0.1923,
                    0.8077,
                    0.8077,
                    0.1923*/
                    (18.0-Math.sqrt(30.0))/36.0,
                    (18.0+Math.sqrt(30.0))/36.0,
                    (18.0+Math.sqrt(30.0))/36.0,
                    (18.0-Math.sqrt(30.0))/36.0
            };

        }
        else {
            throw new IllegalArgumentException("Poza zakresem liczby punktów całkowania");
        };

        IntegrationScheme.points = points;
    }
}
