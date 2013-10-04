package Util;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 04.10.13
 * Time: 08:30
 */

public class CGraph {
    public static int lastId = 0;

    private final List<CVertex> vertexes;
    private final List<CEdge> edges;

    /**
     * Creates an empty graph
     */
    public CGraph() {
        this.vertexes = new LinkedList<CVertex>();
        this.edges = new LinkedList<CEdge>();
    }

    /**
     * Creates a predefined graph
     * @param vertexes the vertexes
     * @param edges the edges
     */
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

    public CVertex getVertexByPosition(CPosition pos) {

        for(CVertex v : this.vertexes) {
            if(v.getPos().compareTo(pos) == 0)  {   // we have found an already existing source
                return v;
            }
        }

        return null;
    }

    public void addEdge(CPosition source, CPosition destination, boolean isObstacleEdge) {
        CVertex vertexSource = null;
        CVertex vertexDestination = null;

        // find already existing source/destination vertex
        for(CVertex v : vertexes) {
             if(v.getPos().compareTo(source) == 0)  {   // we have found an already existing source
                vertexSource = v;
                if(vertexDestination != null) {
                    // we have found both source and dest, so finish the search
                    break;
                }
             }
             if(v.getPos().compareTo(destination) == 0) {  // we have found an already existing destination
                 vertexDestination = v;
                 if(vertexSource != null) {
                     // we have found both source and dest, so finish the search
                     break;
                 }
             }
        }

        if(vertexSource != null && vertexDestination != null) {
            // we already have both vertexes in our graph, let's check if there is already an edge between those
            for(CEdge e : this.edges) {
                if(e.getDestination().compareTo(vertexDestination) == 0 && e.getSource().compareTo(vertexSource) == 0) {
                    return;
                }
            }
        }

        if(vertexSource == null) {
            vertexSource = new CVertex(source);
            this.vertexes.add(vertexSource);
        }

        if(vertexDestination == null) {
            vertexDestination = new CVertex(destination);
            this.vertexes.add(vertexDestination);
        }

        CEdge newEdge = new CEdge(vertexSource, vertexDestination, null, isObstacleEdge);

        // Check if the edge crosses an existing objectedge
        for(CEdge e : this.edges) {
            if(e.isObstacleEdge()) {
                if( e.calcIntersectionWith(newEdge) != null) {
                    return;
                }
            }
        }

        // no crossing line, add it to the graph
        this.edges.add(newEdge);
    }

}
