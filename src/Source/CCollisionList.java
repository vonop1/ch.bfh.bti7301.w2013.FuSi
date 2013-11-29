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
    private CWorld world = null;

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

    public void addCollision(CWorld world, CWalker walker) {
        if(!this.walkers.contains(walker)) {
            this.walkers.add(walker);
        }

        this.world = world;
    }

    public Vector<CWalker> getWalkers() {
        return this.walkers;
    }

    public Vector<CObstacle> getObstacles() {
        return this.obstacles;
    }

    public CWorld getWorld() {
        return this.world;
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

        if( this.world == null ) {
            this.world = other.getWorld();
        }
    }

    public void remove(CWalker walker) {
        this.walkers.remove(walker);
    }

    public Integer size() {
        return walkers.size() + obstacles.size() + (this.world != null ? 1 : 0);
    }

    public void clear() {
        walkers.clear();
        obstacles.clear();
        world = null;
    }

    public boolean hasCollisions() {
        return this.size() > 1;
    }
}
