package com.benoni;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        double[][] matrix = DiamondSquare.createLandscape(7);
        try {
            ObjFileConverter.matrixToFile(matrix);
        } catch (IOException e) {
            System.out.println("File Error!\n" + e.getMessage());
        }

    }
}