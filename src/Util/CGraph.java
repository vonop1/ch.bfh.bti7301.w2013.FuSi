package Util;

import Source.CObstacle;
import Source.CWorld;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1, jaggr2
 * Date: 04.10.13
 * Time: 08:30
 */

public class CGraph {
    protected static int lastId = 0;

    private final List<CPosition> vertexes;
    private final List<CEdge> edges;
    private final List<CEdge> trashEdges;
    private final CWorld worldReference;

    public static int incrementId() {
        CGraph.lastId += 1;
        return CGraph.lastId;
    }

    /**
     * Creates an empty graph
     */
    public CGraph(CWorld worldReference) {
        this.vertexes = new LinkedList<CPosition>();
        this.edges = new LinkedList<CEdge>();
        this.trashEdges = new LinkedList<CEdge>();
        this.worldReference = worldReference;
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
     * removes a vertex point from the graph
     * @param position the vertex position
     */
    public void removeVertex(CPosition position) {

        Iterator<CEdge> iterEdges = this.edges.iterator();
        while(iterEdges.hasNext()){
            CEdge edge = iterEdges.next();
            if(edge.getSource().compareTo(position) == 0 || edge.getDestination().compareTo(position) == 0) {
                iterEdges.remove();
            }
        }

        Iterator<CEdge> iterTrash = this.trashEdges.iterator();
        while(iterTrash.hasNext()){
            CEdge edge = iterTrash.next();
            if(edge.getSource().compareTo(position) == 0 || edge.getDestination().compareTo(position) == 0) {
                iterTrash.remove();
            }
        }

        Iterator<CPosition> iterVertexes = this.vertexes.iterator();
        while(iterVertexes.hasNext()){
            CPosition vertex = iterVertexes.next();
            if(vertex.compareTo(position) == 0) {
                iterVertexes.remove();
            }
        }
    }

    /*
    public Boolean addWayPointLinkedToAll(CPosition position) {

        Boolean returnValue = false;

        Iterator<CPosition> iterVertexes = this.vertexes.iterator();
        while(iterVertexes.hasNext()){
            CPosition vertex = iterVertexes.next();
            returnValue |= this.addWayPointEdge(position, vertex);
        }

        return returnValue;
    } */


    /**
     * adds a Waypoint edge to the graph if it does not intersect with an obstacle edge
     * @param source the source point
     * @param destination the destination point
     */
    public Boolean addWayPointEdge(CPosition source, CPosition destination) {
        CPosition vertexSource = null;
        CPosition vertexDestination = null;

        if(source == null || destination == null) {
            throw new IllegalArgumentException("Die Punkte einer Edge dürfen nicht NULL sein!");
        }

        if(source.compareTo(destination) == 0) {
            return false;
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
