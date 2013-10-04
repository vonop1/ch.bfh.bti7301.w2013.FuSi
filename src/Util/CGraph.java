package Util;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 04.10.13
 * Time: 08:30
 */

public class CGraph {
    private final List<CVertex> vertexes;
    private final List<CEdge> edges;

    public CGraph(List<CVertex> vertexes, List<CEdge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<CVertex> getVertexes() {
        return vertexes;
    }

    public List<CEdge> getEdges() {
        return edges;
    }

}
