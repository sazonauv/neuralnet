package org.dlminer.nn.poc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by slava on 10/09/17.
 */
public class Dataset {

    public double[][] points;

    public double[] labels;

    private int classCount;

    public Dataset(double[][] points, double[] labels) {
        if (labels.length != points.length) {
            throw new IllegalArgumentException("The number of labels is not equal to the number of points!");
        }
        this.points = points;
        this.labels = labels;
        countClasses();
    }

    public double[] getPoint(int index) {
        return points[index];
    }

    public double getLabel(int index) {
        return labels[index];
    }

    public int size() {
        return labels.length;
    }

    public int getFeatureCount() {
        return points[0].length;
    }

    private void countClasses() {
        Set<Double> classSet = new HashSet<>();
        for (int i=0; i<labels.length; i++) {
            classSet.add(labels[i]);
        }
        classCount = classSet.size();
    }

    public int getClassCount() {
        return classCount;
    }


    @Override
    public String toString() {
        String str = "Dataset " + points.length + " x " + points[0].length;
        for (int i=0; i<points.length; i++) {
            str += "\n" + Arrays.toString(points[i]) + " [ " + labels[i] + " ] ";
        }
        return str;
    }


    public static Dataset getZeroOneSequences(int pointnumber, int featureNumber) {
        double[][] points = new double[pointnumber][featureNumber];
        double[] labels = new double[pointnumber];
        for (int i=0; i<pointnumber; i++) {
            if (i % 2 == 0) {
                Arrays.fill(points[i], 1);
                labels[i] = 1;
            } else {
                Arrays.fill(points[i], 0);
                labels[i] = 0;
            }
        }
        return new Dataset(points, labels);
    }


    public static Dataset getIncreasingSequences(int pointNumber, int featureNumber, int maxInteger) {
        double[][] points = new double[pointNumber][featureNumber];
        double[] labels = new double[pointNumber];
        int i = 0;
        while (i < pointNumber) {
            for (int j=0; j<maxInteger; j++) {
                Arrays.fill(points[i], j);
                labels[i] = j;
                i++;
                if (i >= pointNumber) {
                    break;
                }
            }
        }
        return new Dataset(points, labels);
    }





}
