package com.iticu.datamining.knn;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kerem
 * 1/2/2020
 */
public class Main {

    public static void main(String[] args) {
        Set<KNNVector> testSet=new HashSet<KNNVector>();
        Set<KNNVector> trainingSet=new HashSet<KNNVector>();

        KNN knn=new KNN(testSet,trainingSet);
        knn.scanAndFillList("src/main/resources/iris-training.knn",trainingSet);
        knn.scanAndFillList("src/main/resources/iris-test.knn",testSet);
        knn.calculateClasses();
        knn.printAllCalculations();

    }
}
