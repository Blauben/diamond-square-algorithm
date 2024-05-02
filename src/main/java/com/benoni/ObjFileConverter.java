package com.benoni;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ObjFileConverter {

    private static final double xyScale = 1;

    public static void matrixToFile(double[][] matrix) throws IOException {
        String contents = buildFileContents(matrix);
        File out = new File("terrain.obj");
        if (out.exists()) {
            out.delete();
        }
        out.createNewFile();
        FileWriter fw = new FileWriter(out);
        fw.write(contents);
        fw.close();
    }

    private static String buildFileContents(double[][] matrix) {
        StringBuilder sb_vertex = new StringBuilder();
        StringBuilder sb_faces = new StringBuilder();
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                //sb_vertex.append("v " + x * xyScale + " " + y * xyScale + " " + matrix[x][y] + "\n");
                sb_vertex.append("v " + x * xyScale + " " + matrix[x][y] + " " + y * xyScale + "\n");
            }
        }

        for (int y = 0; y < matrix.length - 1; y++) {
            for (int x = 0; x < matrix.length - 1; x++) {
                sb_faces.append("f " + (y * matrix.length + x + 1) + " " + (y * matrix.length + x + matrix.length + 1) + " " + (y * matrix.length + x + matrix.length + 2) + "\n");
                sb_faces.append("f " + (y * matrix.length + x + 1) + " " + (y * matrix.length + x + 2) + " " + (y * matrix.length + x + matrix.length + 2) + "\n");
            }
        }
        return sb_vertex.toString() + sb_faces;
    }
}
