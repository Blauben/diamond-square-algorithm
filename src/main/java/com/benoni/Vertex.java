package com.benoni;

public class Vertex {

    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    double[] xyz;
    double[] n;
    double[] tc;

    public Vertex() {
        xyz = new double[3];
        n = new double[3];
        tc = new double[2];
    }

    public Vertex(double x, double y, double z) {
        this();
        this.xyz[0] = x;
        this.xyz[1] = y;
        this.xyz[Vertex.Z] = z;
    }

    public String toWavefrontObjStrV() {
        return String.format("v %s %s %s\n", xyz[0], xyz[1], xyz[2]);
    }

    public String toWavefrontObjStrVn() {
        return String.format("vn %s %s %s\n", n[0], n[1], n[2]);
    }

    public String toWavefrontObjStrVt() {
        return String.format("vt %s %s\n", tc[0], tc[1]);
    }

}
