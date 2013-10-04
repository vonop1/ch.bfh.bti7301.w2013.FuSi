import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.both;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.matchers.JUnitMatchers.everyItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import Source.CObstacle;
import Util.*;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CWorldTest {

    @Test
    public void testWaypointRectangle()
    {
        double dDeltaC = 0.01;

        Vector<CPosition> aoPos = new Vector<CPosition>();
        //generate Rectangle
        aoPos.add(new CPosition(10,50));
        aoPos.add(new CPosition(110,50));
        aoPos.add(new CPosition(110,100));
        aoPos.add(new CPosition(10,100));
        CObstacle oTestObj = new CObstacle(aoPos);

        Vector<CPosition> aoWaypoint = oTestObj.getWaypoints();

        org.junit.Assert.assertEquals(   1.05, aoWaypoint.elementAt(0).getX(), dDeltaC);
        org.junit.Assert.assertEquals(  45.52, aoWaypoint.elementAt(0).getY(), dDeltaC);

        org.junit.Assert.assertEquals( 118.95, aoWaypoint.elementAt(1).getX(), dDeltaC);
        org.junit.Assert.assertEquals(  45.52, aoWaypoint.elementAt(1).getY(), dDeltaC);

        org.junit.Assert.assertEquals( 118.95, aoWaypoint.elementAt(2).getX(), dDeltaC);
        org.junit.Assert.assertEquals( 104.48, aoWaypoint.elementAt(2).getY(), dDeltaC);

        org.junit.Assert.assertEquals(   1.05, aoWaypoint.elementAt(3).getX(), dDeltaC);
        org.junit.Assert.assertEquals( 104.48, aoWaypoint.elementAt(3).getY(), dDeltaC);
    }

//    @Test
//    public void testDijkstra()
//    {
//        CVertex oVertex1 = new CVertex(1);
//        CVertex oVertex2 = new CVertex(2);
//        CVertex oVertex3 = new CVertex(3);
//
//        Vector<CVertex> aoVertex = new Vector<CVertex>();
//
//        aoVertex.add(oVertex1);
//        aoVertex.add(oVertex2);
//        aoVertex.add(oVertex3);
//
//        Vector<CEdge> aoEdge = new Vector<CEdge>();
//
//        aoEdge.add(new CEdge(1, oVertex1, oVertex2, 5));
//        aoEdge.add(new CEdge(2, oVertex2, oVertex3, 8));
//        aoEdge.add(new CEdge(3, oVertex1, oVertex3, 36));
//
//        CDijkstra oDijkstra = new CDijkstra(new CGraph(aoVertex, aoEdge));
//
//        List<CVertex> oResult = oDijkstra.getPath(oVertex1, oVertex3);
//
//        org.junit.Assert.assertEquals(0, oResult.indexOf(oVertex1));
//        org.junit.Assert.assertEquals(1, oResult.indexOf(oVertex2));
//        org.junit.Assert.assertEquals(2, oResult.indexOf(oVertex3));
//
//    }

//    @Test
//    public void testAssertArrayEquals() {
//        byte[] expected = "trial".getBytes();
//        byte[] actual = "trial".getBytes();
//        org.junit.Assert.assertArrayEquals("failure - byte arrays not same", expected, actual);
//    }
//
//    @Test
//    public void testAssertEquals() {
//        org.junit.Assert.assertEquals("failure - strings not same", 5l, 5l);
//    }
//
//    @Test
//    public void testAssertFalse() {
//        org.junit.Assert.assertFalse("failure - should be false", false);
//    }
//
//    @Test
//    public void testAssertNotNull() {
//        org.junit.Assert.assertNotNull("should not be null", new Object());
//    }
//
//    @Test
//    public void testAssertNotSame() {
//        org.junit.Assert.assertNotSame("should not be same Object", new Object(), new Object());
//    }
//
//    @Test
//    public void testAssertNull() {
//        org.junit.Assert.assertNull("should be null", null);
//    }
//
//    @Test
//    public void testAssertSame() {
//        Integer aNumber = Integer.valueOf(768);
//        org.junit.Assert.assertSame("should be same", aNumber, aNumber);
//    }

    // JUnit Matchers assertThat
    @Test
    public void testAssertThatBothContainsString() {
        org.junit.Assert.assertThat("albumen", both(containsString("a")).and(containsString("b")));
    }

    @Test
    public void testAssertThathasItemsContainsString() {
        org.junit.Assert.assertThat(Arrays.asList("one", "two", "three"), hasItems("one", "three"));
    }

    @Test
    public void testAssertThatEveryItemContainsString() {
        org.junit.Assert.assertThat(Arrays.asList(new String[] { "fun", "ban", "net" }), everyItem(containsString("n")));
    }

    // Core Hamcrest Matchers with assertThat
    @Test
    public void testAssertThatHamcrestCoreMatchers() {
        assertThat("good", allOf(equalTo("good"), startsWith("good")));
        assertThat("good", not(allOf(equalTo("bad"), equalTo("good"))));
        assertThat("good", anyOf(equalTo("bad"), equalTo("good")));
        assertThat(7, not(CombinableMatcher.<Integer> either(equalTo(3)).or(equalTo(4))));
        assertThat(new Object(), not(sameInstance(new Object())));
    }
}