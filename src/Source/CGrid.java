package Source;

import Util.CEdge;
import Util.CPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 18.10.13
 * Time: 13:55
 */
public class CGrid {


    final int gridSizeC = 20;
    private Map<Integer,Map<Integer, Vector<CWalker>>> grid = new HashMap<Integer, Map<Integer, Vector<CWalker>>>();

    /**
     * Subscribe Walker Position in Grid
     */
    void subscribeWalker (CWalker walker)
    {
        Integer gridColumn = walker.getDesiredNextPosition().getY().intValue() / gridSizeC;
        Integer gridRow = walker.getDesiredNextPosition().getX().intValue() / gridSizeC;

        Map<Integer, Vector<CWalker>> gridRowMap;
        Vector<CWalker> walkerInCell;
        if (grid.containsKey(gridColumn))
        {
            gridRowMap = grid.get(gridColumn);
        }
        else
        {
            gridRowMap = new HashMap<Integer, Vector<CWalker>>();
            grid.put(gridColumn, gridRowMap);
        }

        if (gridRowMap.containsKey(gridRow))
        {
            walkerInCell = gridRowMap.get(gridRow);
        }
        else
        {
            walkerInCell = new Vector<CWalker>();
            gridRowMap.put(gridRow, walkerInCell);
        }

        if (!walkerInCell.contains(walker))
        {
            walkerInCell.add(walker);
        }

    }

    /**
     * unsubscribe Walker Position in Grid
     */
    boolean unsubscribeWalker (CWalker walker)
    {
        Integer gridColumn = walker.getPosition().getY().intValue() / gridSizeC;
        Integer gridRow = walker.getPosition().getX().intValue() / gridSizeC;

        Map<Integer, Vector<CWalker>> gridRowMap;
        Vector<CWalker> walkerInCell;
        if (grid.containsKey(gridColumn))
        {
            gridRowMap = grid.get(gridColumn);

            if (gridRowMap.containsKey(gridRow))
            {
                walkerInCell = gridRowMap.get(gridRow);

                walkerInCell.remove(walker);

                if (walkerInCell.isEmpty())
                {
                    gridRowMap.remove(gridRow);
                }
            }
            else
            {
                return false;
            }

            if (gridRowMap.isEmpty())
            {
                grid.remove(gridColumn);
            }
        }
        else
        {
            return false;
        }
        return true;
    }

    /**
     * Get Neighbours of a Walker
     */
    Vector<CWalker> getNeighbours (CWalker walker)
    {
        Vector<CWalker> neighbours = new Vector<CWalker>();

        Integer gridColumn = walker.getDesiredNextPosition().getY().intValue() / gridSizeC;
        Integer gridRow = walker.getDesiredNextPosition().getX().intValue() / gridSizeC;

        Map<Integer, Vector<CWalker>> gridRowMap;
        for (int i = gridColumn - 1; i < gridColumn + 2; i++)
        {
            if (grid.containsKey(i))
            {
                gridRowMap = grid.get(gridColumn);

                for (int j = gridRow -1; j < gridRow + 2; j++)
                {
                    if (gridRowMap.containsKey(j))
                    {
                        neighbours.addAll(gridRowMap.get(j));
                    }
                }
            }
        }

        neighbours.remove(walker);

        return neighbours;
    }

    /**
     * Subscribe Obstacle Positions in Grid
     */
    void subscribeObstacle (Vector<CObstacle> obstacles)
    {

      //TODO subscribe obstacle in grid
    }
}
