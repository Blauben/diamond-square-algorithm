package com.benoni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DiamondSquare {

    private static final Random rand = new Random();
    private static final double roughness = 1.3;
    private static double sigma = 15;

    public static Vertex[][] createLandscape(int size_exp) {
        int size = (int) Math.pow(2, size_exp) + 1;
        Vertex[][] landscape = new Vertex[size][size];
        landscape[0][0] = new Vertex(0, 0, 0);
        landscape[0][size - 1] = new Vertex(0, size - 1, 5);
        landscape[size - 1][0] = new Vertex(size - 1, 0, 10);
        landscape[size - 1][size - 1] = new Vertex(size - 1, size - 1, 5);
        double sigmaTmp = sigma;
        stepEngine(landscape);
        sigma = sigmaTmp;
        calculateNormals(landscape);
        calculateTextureCoord(landscape);
        return landscape;
    }

    public static String generateExtremaComment(Vertex[][] landscape) {
        double[] extrema = new double[]{landscape[0][0].xyz[Vertex.Z], landscape[0][0].xyz[Vertex.Z]};
        for (int i = 0; i < landscape.length; i++) {
            for (int j = 0; j < landscape.length; j++) {
                double z = landscape[i][j].xyz[Vertex.Z];
                extrema[0] = Math.min(extrema[0], z);
                extrema[1] = Math.max(extrema[1], z);
            }
        }
        String message = "Min: " + extrema[0] + ", Max: " + extrema[1] + "\n";
        System.out.println(message);
        return "# " + message;
    }

    private static void stepEngine(Vertex[][] landscape) {
        for (int stepSize = landscape.length - 1; stepSize > 1; stepSize = stepSize / 2) {
            diamondStep(landscape, stepSize);
            squareStep(landscape, stepSize);
            sigma = sigma / Math.pow(2, roughness);
        }
    }

    private static void diamondStep(Vertex[][] landscape, int stepSize) {
        for (int y = 0; y < landscape[0].length - 1; y = y + stepSize) {
            for (int x = 0; x < landscape.length - 1; x = x + stepSize) {
                double sum = landscape[x][y].xyz[Vertex.Z] + landscape[x + stepSize][y].xyz[Vertex.Z] + landscape[x][y + stepSize].xyz[Vertex.Z] + landscape[x + stepSize][y + stepSize].xyz[Vertex.Z];
                landscape[x + stepSize / 2][y + stepSize / 2] = new Vertex(x + stepSize / 2, y + stepSize / 2, sum / 4.0 + rand.nextGaussian() * sigma);
            }
        }
    }

    private static void squareStep(Vertex[][] landscape, int stepSize) {
        for (int y = 0; y < landscape[0].length; y = y + stepSize) {
            for (int x = 0; x < landscape.length; x = x + stepSize) {
                squareStepRight(landscape, x, y, stepSize);
                squareStepDown(landscape, x, y, stepSize);
            }
        }
    }

    private static void squareStepRight(Vertex[][] landscape, int x, int y, int stepSize) {
        if (x + stepSize / 2 >= landscape.length) {
            return;
        }

        int delimiter = 4;
        double sum = landscape[x][y].xyz[Vertex.Z];
        if (x + stepSize < landscape.length) {
            sum += landscape[x + stepSize][y].xyz[Vertex.Z];
        } else {
            delimiter--;
        }

        if (x + stepSize / 2 < landscape.length && y - stepSize / 2 >= 0) {
            sum += landscape[x + stepSize / 2][y - stepSize / 2].xyz[Vertex.Z];
        } else {
            delimiter--;
        }

        if (x + stepSize / 2 < landscape.length && y + stepSize / 2 < landscape[0].length) {
            sum += landscape[x + stepSize / 2][y + stepSize / 2].xyz[Vertex.Z];
        } else {
            delimiter--;
        }
        landscape[x + stepSize / 2][y] = new Vertex(x + stepSize / 2, y, sum / delimiter + rand.nextGaussian() * sigma);
    }

    private static void squareStepDown(Vertex[][] landscape, int x, int y, int stepSize) {
        if (y + stepSize / 2 >= landscape[0].length) {
            return;
        }

        int delimiter = 4;
        double sum = landscape[x][y].xyz[Vertex.Z];
        if (y + stepSize < landscape[0].length) {
            sum += landscape[x][y + stepSize].xyz[Vertex.Z];
        } else {
            delimiter--;
        }

        if (x - stepSize / 2 >= 0) {
            sum += landscape[x - stepSize / 2][y + stepSize / 2].xyz[Vertex.Z];
        } else {
            delimiter--;
        }

        if (x + stepSize / 2 < landscape.length) {
            sum += landscape[x + stepSize / 2][y + stepSize / 2].xyz[Vertex.Z];
        } else {
            delimiter--;
        }
        landscape[x][y + stepSize / 2] = new Vertex(x, y + stepSize / 2, sum / delimiter + rand.nextGaussian() * sigma);
    }

    private static void calculateNormals(Vertex[][] landscape) {
        List<double[]>[][] normals = setupNormalsList(landscape.length, landscape[0].length);
        calculateAdjacentNormals(landscape, normals);
        for (int i = 0; i < landscape.length; i++) {
            for (int j = 0; j < landscape[0].length; j++) {
                landscape[i][j].n = normalize(average(normals[i][j]));
            }
        }
    }

    private static double[] average(List<double[]> vectors) {
        double[] res = new double[3];
        for (int i = 0; i < res.length; i++) {
            final int index = i;
            res[index] = vectors.stream().map(v -> v[index]).reduce(0.0, Double::sum) / vectors.size();
        }
        return res;
    }

    private static double[] normalize(double[] vector) {
        double[] res = vector.clone();
        double vecLength = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        for (int i = 0; i < res.length; i++) {
            res[i] /= vecLength;
        }
        return res;
    }

    private static List<double[]>[][] setupNormalsList(int dimX, int dimY) {
        ArrayList<double[]>[][] normals = new ArrayList[dimX][dimY];
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                normals[i][j] = new ArrayList<>();
            }
        }
        return normals;
    }

    private static void calculateAdjacentNormals(Vertex[][] landscape, List<double[]>[][] normals) {
        assert landscape.length == landscape[0].length && landscape.length > 1;
        for (int y = 1; y < landscape[0].length; y++) {
            for (int x = 1; x < landscape.length; x++) {
                Vertex A = landscape[x][y];
                Vertex B = landscape[x - 1][y];
                Vertex C = landscape[x - 1][y - 1];
                double[] normal = normalize(calculateTriangleNormal(A, B, C));
                normals[x][y].add(normal);
                normals[x - 1][y].add(normal);
                normals[x - 1][y - 1].add(normal);
                B = landscape[x][y - 1];
                normal = normalize(calculateTriangleNormal(A, B, C));
                normals[x][y].add(normal);
                normals[x][y - 1].add(normal);
                normals[x - 1][y - 1].add(normal);
            }
        }
    }

    private static double[] calculateTriangleNormal(Vertex A, Vertex B, Vertex C) {
        double[] AB = subtractVectors(B.xyz, A.xyz);
        double[] CA = subtractVectors(C.xyz, A.xyz);
        return crossProduct(AB, CA);
    }

    private static double[] subtractVectors(double[] v1, double[] v2) {
        double[] res = Arrays.copyOf(v1, v1.length);
        for (int i = 0; i < res.length; i++) {
            res[i] -= v2[i];
        }
        return res;
    }

    private static double[] crossProduct(double[] v1, double[] v2) {
        double[] res = new double[3];
        res[0] = v1[1] * v2[2] - v1[2] * v2[1];
        res[1] = v1[2] * v2[0] - v1[0] * v2[2];
        res[2] = v1[0] * v2[1] - v1[1] * v2[0];
        return res;
    }

    private static void calculateTextureCoord(Vertex[][] landscape) {
        double terrainLengthX = Math.abs(landscape[0][0].xyz[Vertex.X] - landscape[landscape.length - 1][landscape[0].length - 1].xyz[Vertex.X]);
        double terrainLengthY = Math.abs(landscape[0][0].xyz[Vertex.Y] - landscape[landscape.length - 1][landscape[0].length - 1].xyz[Vertex.Y]);

        for (int i = 0; i < landscape.length; i++) {
            for (int j = 0; j < landscape[0].length; j++) {
                Vertex v = landscape[i][j];
                v.tc[0] = v.xyz[Vertex.X] / terrainLengthX;
                v.tc[1] = v.xyz[Vertex.Y] / terrainLengthY;
            }
        }
    }

    private static double dot(double[] a, double[] b) { //TODO delete
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }
}