package com.iticu.datamining.knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class KNNVector {

	List<Double> values = new ArrayList<Double>();
    HashMap<String,Integer> countForClasses;
	protected String inputClass;
	protected String guessedClass;
	protected double distance = 0;    //storing the length to the neighbour in quesiton.

	//Storing the vectors as both an array and separately until I figure out wtf to do.
	public KNNVector(String inputClass,List<Double> vectorParameters){
		this.values=vectorParameters;
		this.inputClass = inputClass;
        countForClasses=new HashMap<String, Integer>();
	}

    public HashMap<String, Integer> getCountForClasses() {
        return countForClasses;
    }

    public void setCountForClasses(HashMap<String, Integer> countForClasses) {
        this.countForClasses = countForClasses;
    }

    /**
	 * Measure: A method to compare how 'near' this vector is to a given alternative
	 * IrisVector, in order to find the nearest neighbors using kNN.
	 * 
	 * @param other vector to compare
	 * @return distance between the two
	 */
	public double measure (KNNVector other){
		
		//d^2 = sqrt(sum(1:4) (ai - bi))
		//keeping track of the sigma sum over the series.
		double sum = 0;
		
		//getting (a-b)^2 for each part of the series.
		for (int i = 0; i < other.getValues().size(); i++){
			double dist = this.values.get(i) - other.values.get(i);
			sum = sum + Math.pow(dist, 2);
		}
		
		//taking square root to get difference/distance
		double dist = Math.sqrt(sum);
		this.distance = dist;

		return dist;
	}

	public String toString() {
		String iris = "";
		for (double d : values) {
			iris = iris + d + " ";
		}
		iris = iris + inputClass;
		return iris;
	}

	public List<Double> getValues() {
		return values;
	}

	public void setValues(List<Double> values) {
		this.values = values;
	}

	public String getInputClass() {
		return inputClass;
	}

	public void setInputClass(String inputClass) {
		this.inputClass = inputClass;
	}

	public String getGuessedClass() {
		return guessedClass;
	}

	public void setGuessedClass(String guessedClass) {
		this.guessedClass = guessedClass;
	}
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
