package pl.jakubdudek.fem.globaldata;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Node {
    private final double x;
    private final double y;

    @Setter
    private int BC;

    public Node(double x, double y){
        this.x = x;
        this.y = y;
        this.BC = 0;
    }

    @Override
    public String toString() {
        return x+" "+y+(BC == 1 ? " BC" : "");
    }
}
