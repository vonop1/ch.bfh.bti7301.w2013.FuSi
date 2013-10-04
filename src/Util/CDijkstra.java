package Util;


import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 04.10.13
 * Time: 13:39
 */
public class CDijkstra {

    private Vector<CVertex> aoNodes;
    private Vector<CEdge> aoEdges;
    private Vector<CVertex> aoSettledNodes;
    private Vector<CVertex> aoUnSettledNodes;
    private Map<CVertex, CVertex> aoPredecessors;
    private Map<CVertex, Integer> aoDistance;

    public CDijkstra (CGraph oGraph)
    {
        // Create a copy of the array so that we can operate on this array
        this.aoNodes = new Vector<CVertex>(oGraph.getVertexes());
        this.aoEdges = new Vector<CEdge>(oGraph.getEdges());
    }

    /*
     * This method returns the path from the source to the selected target in the Graph and
     * NULL if no path exists
     */
    public LinkedList<CVertex> getPath(CVertex oSource, CVertex oTarget)
    {
        execute(oSource, oTarget);
        
        LinkedList<CVertex> oPath = new LinkedList<CVertex>();
        CVertex step = oTarget;
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

    //
    private void execute(CVertex oSource, CVertex oTarget) {
        aoSettledNodes = new Vector<CVertex>();
        aoUnSettledNodes = new Vector<CVertex>();
        aoDistance = new HashMap<CVertex, Integer>();
        aoPredecessors = new HashMap<CVertex, CVertex>();
        aoDistance.put(oSource, 0);
        aoUnSettledNodes.add(oSource);
        while (aoUnSettledNodes.size() > 0)
        {
            CVertex oNode = getMinimum(aoUnSettledNodes);
            aoSettledNodes.add(oNode);
            aoUnSettledNodes.remove(oNode);
            findMinimalDistances(oNode);

            if (oNode.equals(oTarget))
            {
                break;
            }
        }
    }

    private void findMinimalDistances(CVertex oNode) 
    {
        Vector<CVertex> aoAdjacentNodes = getNeighbors(oNode);
        for (CVertex oTarget : aoAdjacentNodes)
        {
            if (getShortestDistance(oTarget) > getShortestDistance(oNode) + getDistance(oNode, oTarget)) 
            {
                aoDistance.put(oTarget, getShortestDistance(oNode) + getDistance(oNode, oTarget));
                aoPredecessors.put(oTarget, oNode);
                aoUnSettledNodes.add(oTarget);
            }
        }

    }

    private int getDistance(CVertex node, CVertex target) {
        for (CEdge edge : aoEdges)
        {
            if (edge.getSource().equals(node) && edge.getDestination().equals(target)) 
            {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private Vector<CVertex> getNeighbors(CVertex oNode) {
        Vector<CVertex> oNeighbors = new Vector<CVertex>();
        for (CEdge oEdge : aoEdges)
        {
            if (oEdge.getSource().equals(oNode) && !isSettled(oEdge.getDestination()))
            {
                oNeighbors.add(oEdge.getDestination());
            }
        }
        return oNeighbors;
    }

    private CVertex getMinimum(Vector<CVertex> aoVertexes) 
    {
        CVertex oMinimum = null;
        for (CVertex oVertex : aoVertexes) 
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

    private boolean isSettled(CVertex oVertex) 
    {
        return aoSettledNodes.contains(oVertex);
    }

    private int getShortestDistance(CVertex oDestination) 
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
}
