import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TSP problem solver using genetic algorithms.
 */
public class GeneticAlgorithm {

    private int generations;
    private int popSize;

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
        int[] initPopulation = pd.getDistances()[0];
        int[][] totalPopulation = new int[popSize][initPopulation.length];

        for(int i = 0; i < popSize; i++) {
            shuffle(initPopulation);
            totalPopulation[i] = initPopulation;
        }



        return null;
    }

//    private double getFitness(int[] child) {
//
//    }

    private int[] mutation(int[] child) {
        // double pm = 0.001; <-- Ask this
        // Bit flip or swapping two products?
        int index1 = (int)(Math.random() * child.length);
        int index2 = (int)(Math.random() * child.length);

        int temp = child[index2];
        child[index2] = child[index1];
        child[index1] = temp;

        return child;
    }

    private int[] crossOver(int[] child1, int[] child2) {
        /// double crossoverProb = 0.7; <-- Ask this
        int start = (int)(Math.random() * child1.length);
        int end = (int)(Math.random() * child1.length) + 1 + start;
        int[] newOffspringArray = Arrays.copyOfRange(child1, start, end);
        List<Integer> newOffspring = Arrays.stream(newOffspringArray).boxed().collect(Collectors.toList());

        for(int i = 0; i < child2.length; i++) {
            int temp = child2[i];
            if(!newOffspring.contains(temp)) {
                newOffspring.add(temp);
            }
        }

        return newOffspring.stream().mapToInt(i -> (int) i).toArray();
    }

    /**
     * Assignment 2.b
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
    	//parameters
    	int populationSize = 20;
        int generations = 20;
        String persistFile = "./tmp/productMatrixDist";
        
        //setup optimization
        TSPData tspData = TSPData.readFromFile(persistFile);
        GeneticAlgorithm ga = new GeneticAlgorithm(generations, populationSize);
        
        //run optimzation and write to file
        int[] solution = ga.solveTSP(tspData);
        tspData.writeActionFile(solution, "./data/TSP solution.txt");
    }
}
