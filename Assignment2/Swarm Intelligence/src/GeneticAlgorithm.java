import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TSP problem solver using genetic algorithms.
 */
public class GeneticAlgorithm {
    private int generations;
    private int popSize;
    //private static final int STOPPING = 10;
    private int currentGenCount;

    /**
     * Constructs a new 'genetic algorithm' object.
     * @param generations the amount of generations.
     * @param popSize the population size.
     */
    public GeneticAlgorithm(int generations, int popSize) {
        this.generations = generations;
        this.popSize = popSize;
    }


    /**
     * Knuth-Yates shuffle, reordering a array randomly
     * @param chromosome array to shuffle.
     */
    private void shuffle(int[] chromosome) {
        int n = chromosome.length;
        for (int i = 0; i < n; i++) {
            int r = i + (int) (Math.random() * (n - i));
            int swap = chromosome[r];
            chromosome[r] = chromosome[i];
            chromosome[i] = swap;
        }
    }

    /**
     * This method should solve the TSP. 
     * @param pd the TSP data.
     * @return the optimized product sequence.
     */
    public int[] solveTSP(TSPData pd) {
        // 1) Create the initial generation with shuffle

        int[][] distances = pd.getDistances();
        int[][] initGeneration = new int[popSize][distances.length];
        int[] initChild = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};

        initGeneration[0] = initChild;
        // Fill the initGeneration with random children
        for(int i = 1; i < popSize; i++) {
            int[] temp = Arrays.copyOf(initGeneration[i - 1], distances.length);
            shuffle(temp);
            initGeneration[i] = temp;
        }

        // 2) Determine the fitness of this generation
        int[][]
        // 3) Select the best (children with the highest fitness) children -> selection
        // 4) Do crossover and mutation on these children
        // 5) Put the children back into the initial generation
        // 6) Repeat step 1 - 5 until max number of generations is reached

        return null;
    }


    /**
     * Assignment 2.b
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	//parameters
    	int populationSize = 20;
        int generations = 20;
        String persistFile = "./productMatrixDist";
        
        //setup optimization
        TSPData tspData = TSPData.readFromFile(persistFile);
        GeneticAlgorithm ga = new GeneticAlgorithm(generations, populationSize);
        
        //run optimzation and write to file
        int[] solution = ga.solveTSP(tspData);
        tspData.writeActionFile(solution, "./data/TSP solution.txt");
    }
}
