/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package grahamscan;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

/**
 *
 * @author Meija
 */
public class GrahamScan {
    /**
     * Enum käännöksen suunnalle: CLOCKWISE=myötäpäivään, COUNTER_CLOCKWISE=
     * vastapäivään, COLLINEAR=samalla suoralla oleva
     */
    protected static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }
    private List<Point> points;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    /**
     * Palauttaa pisteen, jolla alin y-koordinaatti. Jos on useita pisteitä, 
     * joilla sama alin y-koordinaatti, palauttaa niistä sen, jolla on alin 
     * x-koordinaatti,
     * @return Piste jolla alin y-koordinaatti
     */
    public Point getLowestPoint() {
        Point retVal;
        retVal = points.get(0);
        for(int i = 1; i < points.size(); i++) {
            Point temp = points.get(i);
            if(temp.y < retVal.y || (temp.y == retVal.y && temp.x < retVal.x)) {
                retVal = temp;
            }
        }
        return retVal;
    }

    /**
     *
     * @param points
     */
    public GrahamScan(List<Point> points) {
        this.points = points;
    }
    
    /**
     * Järjestää koordinaatit nousevaan järjestykseen sen mukaan, kuinka suuren
     * kulman piste ja alin piste muodostavat x-akselin kanssa.
     */
    public void sortPoints(){
        final Point lowest = getLowestPoint();
        
        TreeSet<Point> set = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {

                if(a == b || a.equals(b)) {
                    return 0;
                }

                // use longs to guard against int-underflow
                double thetaA = Math.atan2((long)a.y - lowest.y, (long)a.x - lowest.x);
                double thetaB = Math.atan2((long)b.y - lowest.y, (long)b.x - lowest.x);

                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    // collinear with the 'lowest' point, let the point closest to it come first

                    // use longs to guard against int-over/underflow
                    double distanceA = Math.sqrt((((long)lowest.x - a.x) * ((long)lowest.x - a.x)) +
                                                (((long)lowest.y - a.y) * ((long)lowest.y - a.y)));
                    double distanceB = Math.sqrt((((long)lowest.x - b.x) * ((long)lowest.x - b.x)) +
                                                (((long)lowest.y - b.y) * ((long)lowest.y - b.y)));

                    if(distanceA < distanceB) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });
        set.addAll(points);
        points = new ArrayList<Point> (set);
    }
    /**
     * Palauttaa pisteiden koordinaatit
     * @return Pisteiden koordinaatit listana
     */
    public List<Point> getPoints(){
        return points;
    }
    /**
     * Tutkii edetäänkö kolmen pisteen välillä myötä- vai vastapäivään.
     * @param a
     * @param b
     * @param c
     * @return 
     */
    protected static Turn getTurn(Point a, Point b, Point c) {

        // use longs to guard against int-over/underflow
        long crossProduct = (((long)b.x - a.x) * ((long)c.y - a.y)) -
                            (((long)b.y - a.y) * ((long)c.x - a.x));

        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }
    public void getConvexHull() throws IllegalArgumentException {
        this.sortPoints();
        if(points.size() < 3) {
            throw new IllegalArgumentException("Convex hull calculation requires at least 3 unique points");
        }

        Stack<Point> stack = new Stack<Point>();
        stack.push(points.get(0));
        stack.push(points.get(1));

        for (int i = 2; i < points.size(); i++) {

            Point head = points.get(i);
            Point middle = stack.pop();
            Point tail = stack.peek();

            Turn turn = getTurn(tail, middle, head);

            switch(turn) {
                case COUNTER_CLOCKWISE:
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(head);
                    break;
            }
        }

        stack.push(points.get(0));

        points = new ArrayList<Point>(stack);
    }

}
