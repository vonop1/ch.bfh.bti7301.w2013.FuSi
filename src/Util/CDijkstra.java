package Util;


import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 04.10.13
 * Time: 13:39
 */
public class CDijkstra {

    private Vector<CPosition> aoNodes;
    private Vector<CEdge> aoEdges;
    private Vector<CPosition> aoSettledNodes;
    private Vector<CPosition> aoUnSettledNodes;
    private Map<CPosition, CPosition> aoPredecessors;
    private Map<CPosition, Integer> aoDistance;

    public CDijkstra (CGraph oGraph)
    {
        // Create a copy of the array so that we can operate on this array
        this.aoNodes = new Vector<CPosition>(oGraph.getVertexes());
        this.aoEdges = new Vector<CEdge>(oGraph.getEdges());
    }

    /*
     * This method returns the path from the source to the selected target in the Graph and
     * NULL if no path exists
     */
    public LinkedList<CPosition> getShortestPath(CPosition oSource, CPosition oTarget)
    {
        execute(oSource, oTarget);
        
        LinkedList<CPosition> oPath = new LinkedList<CPosition>();
        CPosition step = oTarget;
        // Check if a path exists
        if (aoPredecessors.get(step) == null)
        {
            return null;
        }
        oPath.add(step);
        while (aoPredecessors.get(step) != null)
        {
            step = aoPredecessors.get(step);
            oPath.add(step);
        }
        // Put it into the correct order
        Collections.reverse(oPath);
        return oPath;
    }

    /**
     * executes the dijskta on the instance variables and save the result in the predecessors instance vector
     * @param oSource
     * @param oTarget
     */
    private void execute(CPosition oSource, CPosition oTarget) {
        aoSettledNodes = new Vector<CPosition>();
        aoUnSettledNodes = new Vector<CPosition>();
        aoDistance = new HashMap<CPosition, Integer>();
        aoPredecessors = new HashMap<CPosition, CPosition>();
        aoDistance.put(oSource, 0);
        aoUnSettledNodes.add(oSource);
        while (aoUnSettledNodes.size() > 0)
        {
            CPosition oNode = getMinimum(aoUnSettledNodes);
            aoSettledNodes.add(oNode);
            aoUnSettledNodes.remove(oNode);
            findMinimalDistances(oNode);

            if (oNode.equals(oTarget))
            {
                break;
            }
        }
    }

    /**
     * Finds the minimal distance of a node to all neighbor nodes
     * @param oNode
     */
    private void findMinimalDistances(CPosition oNode) 
    {
        Vector<CPosition> aoAdjacentNodes = getNeighbors(oNode);
        for (CPosition oTarget : aoAdjacentNodes)
        {
            if (getShortestDistance(oTarget) > getShortestDistance(oNode) + getDistance(oNode, oTarget)) 
            {
                aoDistance.put(oTarget, getShortestDistance(oNode) + getDistance(oNode, oTarget));
                aoPredecessors.put(oTarget, oNode);
                aoUnSettledNodes.add(oTarget);
            }
        }

    }

    /**
     * get the Distance (weight) between two vertexes
     * @param node the source node
     * @param target the target node
     * @return weight
     */
    private int getDistance(CPosition node, CPosition target) {
        for (CEdge edge : aoEdges)
        {
            if (edge.getSource().equals(node) && edge.getDestination().equals(target)) 
            {
                return edge.getWeight();
            }
            // Erweiterung durch Roger für Unterstützung von ungerichteten Graphen
            if (edge.getDestination().equals(node) && edge.getSource().equals(target))
            {
                return edge.getWeight();
            }
            // ENDE Erweiterung durch Roger für Unterstützung von ungerichteten Graphen
        }
        throw new RuntimeException("Should not happen");
    }

    /**
     * Get all unsettled neighbors of a node
     * @param oNode
     * @return
     */
    private Vector<CPosition> getNeighbors(CPosition oNode) {
        Vector<CPosition> oNeighbors = new Vector<CPosition>();
        for (CEdge oEdge : aoEdges)
        {
            if (oEdge.getSource().equals(oNode) && !isSettled(oEdge.getDestination()))
            {
                oNeighbors.add(oEdge.getDestination());
            }
            // Erweiterung durch Roger für Unterstützung von ungerichteten Graphen
            if (oEdge.getDestination().equals(oNode) && !isSettled(oEdge.getSource()))
            {
                oNeighbors.add(oEdge.getSource());
            }
            // ENDE Erweiterung durch Roger für Unterstützung von ungerichteten Graphen
        }
        return oNeighbors;
    }

    /**
     * returns the node with the minimal shortest distance from the list
     * @param aoVertexes list of vertexes
     * @return the min(vertex.distance)
     */
    private CPosition getMinimum(Vector<CPosition> aoVertexes) 
    {
        CPosition oMinimum = null;
        for (CPosition oVertex : aoVertexes) 
        {
            if (oMinimum == null) 
            {
                oMinimum = oVertex;
            } 
            else 
            {
                if (getShortestDistance(oVertex) < getShortestDistance(oMinimum)) 
                {
                    oMinimum = oVertex;
                }
            }
        }
        return oMinimum;
    }

    /**
     * returns if a node is settled (besucht)
     * @param oVertex
     * @return
     */
    private boolean isSettled(CPosition oVertex) 
    {
        return aoSettledNodes.contains(oVertex);
    }

    /**
     * get the calculated shortest distance from start to oDestinationParameter or MAX_Value when not calculated
     * @param oDestination the vertex of the graph
     * @return the current shortest distance
     */
    private int getShortestDistance(CPosition oDestination) 
    {
        Integer iDistance = aoDistance.get(oDestination);
        if (iDistance == null) 
        {
            return Integer.MAX_VALUE;
        }
        else
        {
            return iDistance;
        }
    }

// Alternative Dijsktra algorithm
//    private static final int INFINITY = -1;
//    public void dijkstra(Graphnode<T> src) {
//        Set<Graphnode<T>> t = new HashSet<Graphnode<T>>();
//        // t is the set of nodes n for which n.getDistance() is "tentative".
//        // For all other nodes, n.getDistance() is the actual distance from src.
//        for (Graphnode<T> n : nodes) {
//            t.add(n);
//            if (n == src) {
//                n.setDistance(0);
//            } else {
//                n.setDistance(INFINITY);
//            }
//        }
//        while (! t.isEmpty()) {
//            Graphnode<T> n = removeNodeWithSmallestDistance(t);
//            int nDist = n.getDistance();
//            if (nDist != INFINITY) {
//                for (Graphnode<T> m : n.getSuccessors()) {
//                    int oldDist = m.getDistance();
//                    int newDist = nDist + edgeWeight(n, m);
//                    if (oldDist == INFINITY || newDist < oldDist) {
//                        m.setDistance(newDist);
//                    }
//                }
//            }
//        }
}


