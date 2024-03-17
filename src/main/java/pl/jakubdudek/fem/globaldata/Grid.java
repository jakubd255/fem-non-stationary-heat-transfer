package pl.jakubdudek.fem.globaldata;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Grid {
    private final Node[] nodes;
    private final Element[] elements;

    public Node[] getNodesOfElement(int i) {
        int[] nodes = elements[i].nodes;

        return new Node[]{this.nodes[nodes[0]], this.nodes[nodes[1]], this.nodes[nodes[2]], this.nodes[nodes[3]]};
    }
}
