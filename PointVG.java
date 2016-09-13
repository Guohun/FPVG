/*
 * This is the source code for a paper 
 *  G. Zhu, J. Hunter,  Efficient Construction of Visibility Graphs from Geometry Domain and Time Domain.
 *  2016.9.13
 */
package ChaosSignal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Guohun Zhu   
 */
public class PointVG {

    int adj[][] = null;
    HashMap<Double, Integer> slopMap = null;
    int Finite[] = {-1, -1};//  positive negative;

    public PointVG(int n) {
        adj = new int[n][n];
        slopMap = new HashMap<Double, Integer>();
    }

    public void clear() {
        Finite[0] = Finite[1] = -1;
        slopMap.clear();
    }

    public int pushSlop(double xstep, double maxslope, int nextpoint) {
        if (xstep == 0) {
            if (maxslope < 0) {
                if (Finite[1] != -1) {
                    return Finite[1];
                } else {
                    Finite[1] = nextpoint;
                    return -1;
                }
            } else if (Finite[0] != -1) {
                return Finite[0];
            } else {
                Finite[0] = nextpoint;
                return -1;
            }
        } else {
            maxslope /= xstep;
            Integer x = slopMap.get(maxslope);
            if (x != null) {
                return x;
            }
            slopMap.put(maxslope, nextpoint);
        }

        return -1;
    }

    public int[][] Run(ArrayList<Point2D> arrayList) {
        Point2D array[] = new Point2D[arrayList.size()];
        arrayList.toArray(array);
        return Run(array);
    }

    public int[][] Run(Point2D array[]) {

        int max_loc = 1;
        double Global_Slop = Integer.MAX_VALUE;
        for (int i = 0; i < array.length - 1; i++) {
            clear();
            double maxslope = array[i + 1].y - array[i].y;

            double xstep = array[i + 1].x - array[i].x;
            pushSlop(xstep, maxslope, i + 1);
            for (int j = i + 2; j < array.length; j++) {
                double weight = array[j].y - array[i].y;
                double xstep1 = array[j].x - array[i].x;
                if (pushSlop(xstep1, weight, j) != -1) {
                    adj[i][j] = 1;
                    adj[j][i] = 1;
                }
            }

        }

        return adj;

    }

    public static void main(String[] args) {

        double yinput[] = {1, 3, 4, 1, 2, 1, 3, 1, 2, 3,};
        double xinput[] = {1, 1, 1, 2, 2, 3, 3, 4, 4, 4};

        PointVG tempVg = new PointVG(xinput.length);
        Point2D pointinput[] = tempVg.createPointArray(xinput, yinput);

        int[][] output = tempVg.Run(pointinput);
        for (int[] x : output) {
            for (int y : x) {
                System.out.printf("%d\t", y);
            }
            System.out.println();
        }
    }

    public Point2D[] createPointArray(double[] xinput, double[] yinput) {
        ArrayList<Point2D> temp = new ArrayList<Point2D>();
        for (int i = 0; i < xinput.length; i++) {
            temp.add(new Point2D(xinput[i], yinput[i]));
        }
        Collections.sort(temp, new Comparator<Point2D>() {
            public int compare(Point2D p1, Point2D p2) {
                if (p1.x == p2.x) {
                    if (p1.y == p2.y) {
                        return 0;
                    } else if (p1.y > p2.y) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else if (p1.x > p2.x) {
                    return 1;
                } else {
                    return -1;
                }

            }
        });

        Point2D array[] = new Point2D[temp.size()];
        temp.toArray(array);
        return array;
    }

    private void clearAdj() {
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj.length; j++) {
                adj[i][j] = 0;
            }
        }
        clear();
    }

}
