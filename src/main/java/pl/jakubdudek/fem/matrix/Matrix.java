package pl.jakubdudek.fem.matrix;

public class Matrix {
    public static double[][] sum(double[][] matrix1, double[][] matrix2) {
        if((matrix1.length != matrix2.length) || (matrix1[0].length != matrix2[0].length))
            throw new IllegalArgumentException("Macierze muszą mieć taką samą wysokość i szerokość");

        double[][] matrix = new double[matrix1.length][matrix1[0].length];

        for(int i=0; i<matrix1.length; i++) {
            for(int j=0; j<matrix1[i].length; j++) {
                matrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }

        return matrix;
    }

    public static double[] sumVectors(double[] vector1, double[] vector2) {
        int length = vector1.length;
        if(length != vector2.length)
            throw new IllegalArgumentException("Tablice muszą mieć taką samą długość");

        double[] array = new double[length];

        for(int i=0; i<length; i++) {
            array[i] = vector1[i]+vector2[i];
        }

        return array;
    }


    public static double[][] transpose(double[][] matrix){
        int rows = matrix.length;
        int cols = matrix[0].length;

        double[][] transposed = new double[cols][rows];

        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }

        return transposed;
    }


    public static double[][] multiply(double[][] matrix, double a){
        int rows = matrix.length;
        int cols = matrix[0].length;

        double[][] newMatrix = new double[cols][rows];

        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                newMatrix[i][j] = a*matrix[i][j];
            }
        }

        return newMatrix;
    }

    public static double[] multiplyVector(double[] vector, double a) {
        int length = vector.length;
        double[] newVector = new double[length];

        for(int i=0; i<length; i++) {
            newVector[i] = vector[i]*a;
        }

        return newVector;
    }

    public static double[][] multiplyToMatrix(double[] matrix1, double[] matrix2){
        int rows = matrix1.length;
        int cols = matrix2.length;

        double[][] newMatrix = new double[rows][cols];

        for(int i=0; i<rows; i++) {
            for(int j=0; j<cols; j++) {
                newMatrix[i][j] = matrix1[i] * matrix2[j];
            }
        }

        return newMatrix;
    }

    public static double[][] multiply(double[][] matrix1, double[][] matrix2){
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;

        int rows2 = matrix2.length;
        int cols2 = matrix2[0].length;

        double[][] newMatrix = new double[rows1][cols2];

        for (int i=0; i<rows1; i++) {
            for (int j=0; j<cols2; j++) {
                newMatrix[i][j] = 0;

                for(int k=0; k<rows2; k++) {
                    newMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return newMatrix;
    }

    public static double det(double[][] matrix){
        double temporary[][];
        double result = 0;

        if(matrix.length == 1) {
            result = matrix[0][0];
            return result;
        }

        if(matrix.length == 2) {
            result = ((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]));
            return result;
        }

        for(int i=0; i<matrix[0].length; i++) {
            temporary = new double[matrix.length - 1][matrix[0].length - 1];

            for(int j=1; j<matrix.length; j++) {
                for(int k=0; k<matrix[0].length; k++) {
                    if(k<i) {
                        temporary[j-1][k] = matrix[j][k];
                    }
                    else if(k>i) {
                        temporary[j-1][k-1] = matrix[j][k];
                    }
                }
            }

            result += matrix[0][i] * Math.pow (-1, (double) i) * det(temporary);
        }
        return (result);
    }

    public static double[][] inverse(double[][] matrix) {
        int n = matrix.length;
        if(n == 0 || n != matrix[0].length) {
            return null;
        }

        double[][] augmentedMatrix = new double[n][2 * n];
        for (int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                augmentedMatrix[i][j] = matrix[i][j];
                augmentedMatrix[i][j+n] = (i == j) ? 1.0 : 0.0;
            }
        }

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j=i+1; j<n; j++) {
                if(Math.abs(augmentedMatrix[j][i]) > Math.abs(augmentedMatrix[maxRow][i]))
                    maxRow = j;
            }

            double[] temp = augmentedMatrix[i];
            augmentedMatrix[i] = augmentedMatrix[maxRow];
            augmentedMatrix[maxRow] = temp;

            double pivot = augmentedMatrix[i][i];

            for(int j=0; j<2*n; j++)
                augmentedMatrix[i][j] /= pivot;

            for(int j=0; j<n; j++) {
                if(j != i) {
                    double factor = augmentedMatrix[j][i];
                    for(int k=0; k<2*n; k++) {
                        augmentedMatrix[j][k] -= factor * augmentedMatrix[i][k];
                    }
                }
            }
        }

        double[][] inverseMatrix = new double[n][n];
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                inverseMatrix[i][j] = augmentedMatrix[i][j + n];
            }
        }

        return inverseMatrix;
    }

    public static double[] multiplyByVector(double[][] matrix, double[] vector) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        if (cols != vector.length) {
            throw new IllegalArgumentException("Invalid matrix and vector dimensions");
        }

        double[] result = new double[rows];

        for (int i = 0; i < rows; i++) {
            double sum = 0.0;
            for (int j = 0; j < cols; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }

        return result;
    }


    public static double[] gauss(double[][] A, double[] b) {
        int n = A.length;
        double[][] Ab = new double[n][n + 1];

        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, Ab[i], 0, n);
            Ab[i][n] = b[i];
        }

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(Ab[j][i]) > Math.abs(Ab[maxRow][i])) {
                    maxRow = j;
                }
            }

            if (maxRow != i) {
                double[] temp = Ab[i];
                Ab[i] = Ab[maxRow];
                Ab[maxRow] = temp;
            }

            for (int j = i + 1; j < n; j++) {
                double factor = Ab[j][i] / Ab[i][i];
                for (int k = i; k < n + 1; k++) {
                    Ab[j][k] -= factor * Ab[i][k];
                }
            }
        }

        if (Ab[n - 1][n - 1] == 0 && Ab[n - 1][n] != 0) {
            return null;
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = Ab[i][n] / Ab[i][i];
            for (int j = i - 1; j >= 0; j--) {
                Ab[j][n] -= Ab[j][i] * x[i];
            }
        }

        return x;
    }


    public static void print(double[][] matrix) {
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]+"\t");
            }
            System.out.println();
        }
    }

    public static void printVecotr(double[] vector) {
        for(int i = 0; i < vector.length; i++) {
            System.out.print(vector[i]+"\t");
        }
        System.out.println();
    }
}