package Source;

import Util.CPosition;

import java.util.Vector;

/**
 * Copyright 2013 blastbeat syndicate gmbh
 * Author: Roger Jaggi <roger.jaggi@blastbeatsyndicate.com>
 * Date: 04.10.13
 * Time: 11:37
 */
public class CObstacleFactory {

//    public static CObstacle createRectangle(int upperleftX,int upperleftY, int bottomrightX, int bottomrightY) {
//
//        Vector<CPosition> positions = new Vector<CPosition>();
//
//        positions.add(new CPosition(upperleftX, upperleftY));
//        positions.add(new CPosition(upperleftX, bottomrightY));
//        positions.add(new CPosition(bottomrightX, bottomrightY));
//        positions.add(new CPosition(bottomrightX, upperleftY));
//
//        return new CObstacle(positions);
//    }
//
//    public static CObstacle createTriangle(int ax, int ay, int bx, int by, int cx, int cy) {
//
//        Vector<CPosition> positions = new Vector<CPosition>();
//
//        positions.add(new CPosition(ax, ay));
//        positions.add(new CPosition(bx, by));
//        positions.add(new CPosition(cx, cy));
//
//        return new CObstacle(positions);
//    }
//
//    public static CObstacle createFuenfeck(int ax, int ay, int bx, int by, int cx, int cy, int dx, int dy, int ex, int ey) {
//
//        Vector<CPosition> positions = new Vector<CPosition>();
//
//        positions.add(new CPosition(ax, ay));
//        positions.add(new CPosition(bx, by));
//        positions.add(new CPosition(cx, cy));
//        positions.add(new CPosition(dx, dy));
//        positions.add(new CPosition(ex, ey));
//
//        return new CObstacle(positions);
//    }

    public static CWalker createWalker(int startX, int startY, int targetX, int targetY) {
        return new CWalker(new CPosition(startX, startY), new CPosition(targetX, targetY));
    }
}
