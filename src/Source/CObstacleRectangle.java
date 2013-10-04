package Source;

import Util.CPosition;

import java.util.Vector;

/**
 * Copyright 2013 blastbeat syndicate gmbh
 * Author: Roger Jaggi <roger.jaggi@blastbeatsyndicate.com>
 * Date: 04.10.13
 * Time: 10:58
 */
public class CObstacleRectangle extends CObstacle {
    public CObstacleRectangle(int upperleftX,int upperleftY, int bottomrightX, int bottomrightY) {
        super(new Vector<CPosition>());

        this.aoPosition.add(new CPosition(upperleftX, upperleftY));
        this.aoPosition.add(new CPosition(upperleftX, bottomrightY));
        this.aoPosition.add(new CPosition(bottomrightX, bottomrightY));
        this.aoPosition.add(new CPosition(bottomrightX, upperleftY));
    }
}
