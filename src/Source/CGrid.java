package Source;

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


    final int gridSizeC = 50;
    private Map<Integer,Map<Integer, Vector<CWalker>>> grid = new HashMap<Integer, Map<Integer, Vector<CWalker>>>();

    /**
     * Subscribe Walker Position in Grid
     */
    void subscribeWalker (CWalker walker)
    {
        Integer gridColumn = walker.getPosition().getY().intValue() / gridSizeC;
        Integer gridRow = walker.getPosition().getX().intValue() / gridSizeC;

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
}
