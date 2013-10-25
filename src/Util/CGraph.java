package Util;

import Source.CObstacle;

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

    private final List<CPosition> vertexes;
    private final List<CEdge> edges;
    private final List<CEdge> trashEdges;
    private final List<CEdge> obstacleEdges;

    public static int incrementId() {
        CGraph.lastId += 1;
        return CGraph.lastId;
    }

    /**
     * Creates an empty graph
     */
    public CGraph() {
        this.vertexes = new LinkedList<CPosition>();
        this.edges = new LinkedList<CEdge>();
        this.trashEdges = new LinkedList<CEdge>();
        this.obstacleEdges = new LinkedList<CEdge>();
    }

    /**
     * Creates a predefined graph
     * @param vertexes the vertexes
     * @param edges the edges
     */
    public CGraph(List<CPosition> vertexes, List<CEdge> edges, List<CEdge> obstacleEdges) {
        this.vertexes = vertexes;
        this.edges = edges;
        this.trashEdges = new LinkedList<CEdge>();
        this.obstacleEdges = obstacleEdges;
    }

    public List<CPosition> getVertexes() {
        return vertexes;
    }

    public List<CEdge> getEdges() {
        return edges;
    }

    public List<CEdge> getTrashEdges() {
        return trashEdges;
    }

    public CPosition getVertexByPosition(CPosition pos) {

        for(CPosition v : this.vertexes) {
            if(v.compareTo(pos) == 0)  {   // we have found an already existing source
                return v;
            }
        }

        return null;
    }

    /**
     * adds an obstacle edge. an obstacle edge is only relevant to the addWayPointEdge Method to check if a Waypoint
     * intersects with an obstacle
     * @param source
     * @param destination
     */
    public void addObstacleEdge(CPosition source, CPosition destination) {
        this.obstacleEdges.add(new CEdge(source, destination, null));
    }

    /**
     * adds a Waypoint edge to the graph if it does not intersect with an obstacle edge
     * @param source
     * @param destination
     */
    public void addWayPointEdge(CPosition source, CPosition destination) {
        CPosition vertexSource = null;
        CPosition vertexDestination = null;

        // find already existing source/destination vertex
        for(CPosition v : vertexes) {
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
            vertexSource = new CPosition(source);
            this.vertexes.add(vertexSource);
        }

        if(vertexDestination == null) {
            vertexDestination = new CPosition(destination);
            this.vertexes.add(vertexDestination);
        }

        CEdge newEdge = new CEdge(vertexSource, vertexDestination, null);

        // Check if the edge crosses an existing objectedge
        // obstacle edges will be added everytime
        for(CEdge e : this.obstacleEdges) {
            if( e.hasIntersectionWith(newEdge)) {
                this.trashEdges.add(newEdge);
                return;
            }
        }

        // no crossing line, add it to the graph
        this.edges.add(newEdge);
    }

}
