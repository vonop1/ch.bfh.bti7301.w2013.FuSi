package Util;

import Source.CObstacle;
import Source.CWorld;

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
    //private final List<CEdge> obstacleEdges;
    //private Integer maxWidth;
    //private Integer maxHeight;
    private final CWorld worldReference;

    public static int incrementId() {
        CGraph.lastId += 1;
        return CGraph.lastId;
    }

    public static void resetId() {
        CGraph.lastId = 0;
    }

    /**
     * Creates an empty graph
     */
    public CGraph(CWorld worldReference) {
        this.vertexes = new LinkedList<CPosition>();
        this.edges = new LinkedList<CEdge>();
        this.trashEdges = new LinkedList<CEdge>();
        this.worldReference = worldReference;
        //this.obstacleEdges = new LinkedList<CEdge>();
        //this.maxWidth = maxWidth;
        //this.maxHeight = maxHeight;
    }

    /**
     * Creates a predefined graph
     * @param vertexes the vertexes
     * @param edges the edges
     */
    public CGraph(List<CPosition> vertexes, List<CEdge> edges, List<CEdge> obstacleEdges, CWorld worldReference) {
        this.vertexes = vertexes;
        this.edges = edges;
        this.trashEdges = new LinkedList<CEdge>();
        this.worldReference = worldReference;
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
     * adds a Waypoint edge to the graph if it does not intersect with an obstacle edge
     * @param source
     * @param destination
     */
    public Boolean addWayPointEdge(CPosition source, CPosition destination) {
        CPosition vertexSource = null;
        CPosition vertexDestination = null;

        if(source == null || destination == null) {
            throw new IllegalArgumentException("Die Punkte einer Edge dürfen nicht NULL sein!");
        }


        if(worldReference != null && (!worldReference.isPointInWorld(source) || !worldReference.isPointInWorld(destination))) {
            // one point is not in the world, refuse to add the edge
            return false;
        }

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
                    return true;
                }
            }
        }

        if(vertexSource == null) {
            vertexSource = source;
            this.vertexes.add(vertexSource);
        }

        if(vertexDestination == null) {
            vertexDestination = destination;
            this.vertexes.add(vertexDestination);
        }

        CEdge newEdge = new CEdge(vertexSource, vertexDestination, null);

        // Check if the edge crosses an existing objectedge
        // obstacle edges will be added everytime
        if(worldReference != null) {
            for(CObstacle obstacle : worldReference.getObstacles()) {
                if(obstacle.hasIntersectionWithEdge(newEdge.getSource(), newEdge.getDestination())) {
                    this.trashEdges.add(newEdge);
                    return false;
                }
            }
        }

        // no crossing line, add it to the graph
        this.edges.add(newEdge);
        return true;
    }

}
