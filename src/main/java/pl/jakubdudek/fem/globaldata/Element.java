package pl.jakubdudek.fem.globaldata;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.stream.Collectors;


@Getter
@Setter
public class Element {
    public final int[] nodes;

    private double[][] H;
    private double[][] HBC;
    private double[] P;
    private double[][] C;

    public Element(int n1, int n2, int n3, int n4) {
        this.nodes = new int[]{n1-1, n2-1, n3-1, n4-1};
        this.H = null;
        this.HBC = null;
        this.P = null;
        this.C = null;
    }

    public void print() {
        System.out.println(Arrays.stream(nodes).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
    }
}