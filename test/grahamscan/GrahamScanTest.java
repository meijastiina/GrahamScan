/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grahamscan;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Meija
 */
public class GrahamScanTest {
    
  public GrahamScanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    

    @Test
    public void testGetLowestPoint() {
        Point a = new Point(3, 3);
        Point b = new Point(2, 2);
        Point c = new Point(3, 5);
        GrahamScan scan = new GrahamScan(Arrays.asList(a, b, c));
        Point retVal = scan.getLowestPoint();
        Point expectedVal = b;
        assertEquals(expectedVal, retVal);
    }
    
    
    @Test
    public void testSortPoints() {
        Point a = new Point(3, 3);
        Point b = new Point(2, 2);
        Point c = new Point(3, 5);
        
        GrahamScan scan = new GrahamScan(Arrays.asList(a, b, c));
        scan.sortPoints();
        List<Point> retVal = scan.getPoints();
        List<Point> expectedVal = Arrays.asList(b, a, c);
        assertEquals(expectedVal, retVal);
    }
    @Test
    public void testClockwiseTurn() {
        /*
            6
            5     c
            4
            3     a
            2   b
            1     
            0 1 2 3 4 5 6 
        */
        Point a = new Point(3, 3);
        Point b = new Point(2, 2);
        Point c = new Point(3, 5);
        
        assertEquals(GrahamScan.Turn.CLOCKWISE, GrahamScan.getTurn(a, b, c));
    }
    @Test
    public void testCounterClockwiseTurn() {
        /*
            6
            5     c
            4         b
            3     a
            2           
            1     
            0 1 2 3 4 5 6 
        */
        Point a = new Point(3, 3);
        Point b = new Point(5, 4);
        Point c = new Point(3, 5);
        
        assertEquals(GrahamScan.Turn.COUNTER_CLOCKWISE, GrahamScan.getTurn(a, b, c));
    }
    @Test
    public void testCollinear() {
        /*
            6
            5        c
            4
            3     a
            2   b
            1     
            0 1 2 3 4 5 6 
        */
        Point a = new Point(3, 3);
        Point b = new Point(2, 2);
        Point c = new Point(5, 5);
        
        assertEquals(GrahamScan.Turn.COLLINEAR, GrahamScan.getTurn(a, b, c));
    }
    @Test
    public void testGetConvexHullNotEnoughPoints() {
        Point a = new Point(3, 3);
        Point b = new Point(2, 2);
        GrahamScan scan = new GrahamScan(Arrays.asList(a, b));
        
        try {
            scan.getConvexHull();
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException IllegalArgumentException) {
            assertEquals("Convex hull calculation requires at least 3 unique points", IllegalArgumentException.getMessage());
        }
    }
    @Test
    public void testGetConvexHullWithCollinearPoints() {
        /*
            6 |       d
            5 |     b   g
            4 |   a   e   i
            3 |     c   h
            2 |       f
            1 |
            0 '------------
              0 1 2 3 4 5 6
        */
        Point a = new Point(2, 4);
        Point b = new Point(3, 5);
        Point c = new Point(3, 3);
        Point d = new Point(4, 6);
        Point e = new Point(4, 4);
        Point f = new Point(4, 2);
        Point g = new Point(5, 5);
        Point h = new Point(5, 3);
        Point i = new Point(6, 4);
        GrahamScan scan = new GrahamScan(Arrays.asList(a, b, c, d, e, f, g, h, i));
        scan.getConvexHull();
        List<Point> retVal = scan.getPoints();
        List<Point> expectedVal = Arrays.asList(f, i, d, a, f);
        assertEquals(expectedVal, retVal);
    }

}
