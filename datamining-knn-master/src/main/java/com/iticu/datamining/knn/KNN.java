package com.iticu.datamining.knn;

import java.io.File;
import java.io.FileReader;
import java.util.*;


public class KNN {

	//Default K value represents k nearest
	private int k = 3;
	Set<KNNVector> testSet ;
	Set<KNNVector> trainingSet ;

	public KNN(Set<KNNVector> testSet, Set<KNNVector> trainingSet){
	    this.testSet=testSet;
	    this.trainingSet=trainingSet;
	}

    /**
	 * This is the main algorithm. It will use an algorithm to compare this 
	 * vector to each in the training set in order to classify it.
	 * 
	 * @param iris
	 */
	private void findClass (KNNVector iris){
		HashMap<String,Integer> countForClasses=new HashMap<String, Integer>();

		//create and fill a priority queue sorted by distance from iris
		PriorityQueue<KNNVector> neighbours = new PriorityQueue<KNNVector>(trainingSet.size(), new KNNVectorComparator());

		//collect k nearest neighbours
		for (KNNVector tVec : trainingSet) {
			tVec.measure(iris);
			neighbours.add(tVec);
		}

        for (int i = 0; i < k; i++) {
            KNNVector vector = neighbours.poll();
            if (countForClasses.get(vector.getInputClass()) == null) {
                countForClasses.put(vector.getInputClass(), 0);
            }
            countForClasses.put(vector.getInputClass(), countForClasses.get(vector.getInputClass()) + 1);
        }
        iris.setCountForClasses(countForClasses);

		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : countForClasses.entrySet()) {
			if (maxEntry == null || entry.getValue()
					.compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		iris.setGuessedClass(maxEntry.getKey());
	}

	/**
	 * This method houses the scanner as it converts the test and training
	 * sets from raw data to a set of IrisVectors. 
	 * 

	 */


	public void scanAndFillList(String filePath,Set<KNNVector> setType){
		System.out.println("Scanning File " + filePath);
		try {
			Scanner read = new Scanner(new FileReader(new File(filePath)));
			while (read.hasNextLine()) {
				String line = read.nextLine();
				String label = "N/A";
				List<Double> vecs = new ArrayList<Double>();
				Scanner sc = new Scanner(line);
				while (sc.hasNextDouble()) {
					vecs.add(sc.nextDouble());
				}
				if (sc.hasNext()) {
					label = sc.next();
					KNNVector iVec = new KNNVector(label, vecs);
					setType.add(iVec);
				} else {
					System.out.println("Scanning File finished");
					read.close();
					sc.close();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void calculateClasses(){
        for(KNNVector iv : testSet){
            findClass(iv);
        }
    }

    public void printAllCalculations(){
        for(KNNVector iv : testSet){
            System.out.println(iv.getValues().toString()+" "+"Given Test Class:"+ iv.getInputClass()+" Calculation:"+iv.getGuessedClass());
            System.out.println(iv.getCountForClasses().toString());
        }
    }

	/*public void parseSets(String testName, String trainingName){
		
		//---PARSE2: TEST---//
		
		System.out.println("//---PARSE1: TEST---//");
		try {
			//read the test set. 
			Scanner read = new Scanner(new FileReader(new File(testName)));
			while(read.hasNextLine()){
				String iris = read.nextLine();
				
				//create a new IrisVector
				double v0, v1, v2, v3 = 0;
				String label = ".";
				List<Double> vecs = new ArrayList<Double>();

				//split vec into it's composite parts. 
				Scanner sc = new Scanner(iris);
				while(sc.hasNextDouble()){
					vecs.add(sc.nextDouble());
				}
				if(sc.hasNext()) {
					label = sc.next();
					//create the new iVector
					IrisVector iVec = new IrisVector(vecs.get(0), vecs.get(1), vecs.get(2), vecs.get(3), label);
					
					//add new vector to testSet
					iVec.setTest();
					testSet.add(iVec);
					}
				else{
					System.out.println("//---PARSE OVER---//");
					read.close();
					sc.close();
					break;
				}
			}
			
			//---PARSE2: TRAINING---//
			System.out.println("//---PARSE2: TRAINING---//");
			
			//read the training set. 
			Scanner read2 = new Scanner(new FileReader(new File(trainingName)));
			while(read2.hasNextLine()){
				String iris = read2.nextLine();
				
				//create a new IrisVector
				double v0, v1, v2, v3 = 0;
				String label = ".";
				List<Double> vecs = new ArrayList<Double>();

				//split vec into it's composite parts. 
				Scanner sc = new Scanner(iris);
				while(sc.hasNextDouble()){
					vecs.add(sc.nextDouble());
				}
				if(sc.hasNext()) {
					label = sc.next();
					//create the new iVector
					IrisVector iVec = new IrisVector(vecs.get(0), vecs.get(1), vecs.get(2), vecs.get(3), label);

					//add new vector to testSet
					iVec.setTest();
					trainingSet.add(iVec);
					}
				else{
					System.out.println("//---PARSE2 OVER---//");
					read2.close();
					sc.close();
					break;
				}
			}
			

		} catch (FileNotFoundException e) {
			System.out.println("Please check your inputs.");
		}
	}*/

	/**
	 * This method prints the output of the kNN algorithm to a .txt file.
	 */
	/*public void print(){
		System.out.println("printing?");
		
		int iter = 0;
		File output = new File("part1-output"+iter+".txt");

		//make sure there's a space for the new file and then make it.
		try {
			if(!output.createNewFile()){
				iter++;
				output = new File("part1-output"+iter+".txt");
				output.createNewFile();
			}
		} catch (IOException e) {
			System.out.println("Output file failed to load.");
		}

		//write the new data to the file
		try {
			FileWriter writer = new FileWriter(output);

			for(IrisVector iv : testSet){
				String irisString = iv.toString() + '\n';
				writer.write(irisString);
			}
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
*/

	/*public static void main (String[] args){
		new KNN("src/iris-test.txt", "src/iris-training.txt");
	}*/


	public class KNNVectorComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			KNNVector v1 = (KNNVector) o1;
			KNNVector v2 = (KNNVector) o2;

			if (v1.getDistance() < v2.getDistance()) return -1;
			else return 1;
		}
	}
}
