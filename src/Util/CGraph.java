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
    protected static int lastId = 0;

    private final List<CVertex> vertexes;
    private final List<CEdge> edges;
    private final List<CEdge> trashEdges;

    public static int incrementId() {
        CGraph.lastId += 1;
        return CGraph.lastId;
    }

    /**
     * Creates an empty graph
     */
    public CGraph() {
        this.vertexes = new LinkedList<CVertex>();
        this.edges = new LinkedList<CEdge>();
        this.trashEdges = new LinkedList<CEdge>();
    }

    /**
     * Creates a predefined graph
     * @param vertexes the vertexes
     * @param edges the edges
     */
    public CGraph(List<CVertex> vertexes, List<CEdge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
        this.trashEdges = new LinkedList<CEdge>();
    }

    public List<CVertex> getVertexes() {
        return vertexes;
    }

    public List<CEdge> getEdges() {
        return edges;
    }

    public List<CEdge> getTrashEdges() {
        return trashEdges;
    }

    public CVertex getVertexByPosition(CPosition pos) {

        for(CVertex v : this.vertexes) {
            if(v.compareTo(pos) == 0)  {   // we have found an already existing source
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
             if(v.compareTo(source) == 0)  {   // we have found an already existing source
                vertexSource = v;
                if(vertexDestination != null) {
                    // we have found both source and dest, so finish the search
                    break;
                }
             }
             if(v.compareTo(destination) == 0) {  // we have found an already existing destination
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
        // obstacle edges will be added everytime
        if(!isObstacleEdge) {
            for(CEdge e : this.edges) {
                if(e.isObstacleEdge()) {
                    if( e.calcIntersectionWith(newEdge) != null) {
                        this.trashEdges.add(newEdge);
                        return;
                    }
                }
            }
        }

        // no crossing line, add it to the graph
        this.edges.add(newEdge);
    }

}
