package Source;

import java.util.Vector;

/**
 * Copyright 2013 blastbeat syndicate gmbh
 * Author: Roger Jaggi <roger.jaggi@blastbeatsyndicate.com>
 * Date: 05.11.13
 * Time: 19:11
 */
public class CCollisionList {

    private Vector<CWalker> walkers = new Vector<CWalker>();
    private Vector<CObstacle> obstacles = new Vector<CObstacle>();

    public void addCollision(CObstacle obstacle, CWalker walker) {
        if(!this.walkers.contains(walker)) {
            this.walkers.add(walker);
        }

        if(!this.obstacles.contains(obstacle)) {
            this.obstacles.add(obstacle);
        }
    }

    public void addCollision(CWalker walkerA, CWalker walkerB) {
        if(!walkers.contains(walkerA)) {
            walkers.add(walkerA);
        }

        if(!walkers.contains(walkerB)) {
            walkers.add(walkerB);
        }
    }

    public Vector<CWalker> getWalkers() {
        return this.walkers;
    }

    public Vector<CObstacle> getObstacles() {
        return this.obstacles;
    }

    public void merge(CCollisionList other) {

        for(CWalker walker : other.getWalkers()) {
            if(!this.walkers.contains(walker)) {
                this.walkers.add(walker);
            }
        }

        for(CObstacle obstacle : other.getObstacles()) {
            if(!this.obstacles.contains(obstacle)) {
                this.obstacles.add(obstacle);
            }
        }

    }

    public void remove(CWalker walker) {
        this.walkers.remove(walker);
    }

    public Integer size() {
        return walkers.size() + obstacles.size();
    }

    public void clear() {
        walkers.clear();
        obstacles.clear();
    }

    public boolean hasCollisions() {
        return walkers.size() + obstacles.size() > 1;
    }
}
