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
    private static final int STOPPING = 10;
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
        int[] initPopulation = pd.getDistances()[0];
        int[][] totalPopulation = new int[popSize][initPopulation.length];
        double[] fitness = new double[popSize];

        // create starting population
        for(int i = 0; i < popSize; i++) {
            shuffle(initPopulation);
            totalPopulation[i] = initPopulation;
        }

        int[][] newGeneration = new int[popSize][initPopulation.length];
        int[] bestOfGen;
        // while loop or something else
        int counter = 0;
        while (counter < generations) {
            // 4 Compute the fitness for each child
            for (int j = 0; j < totalPopulation.length; j++) {
                fitness[j] = getFitness(totalPopulation[j], pd);
            }

            // 5 Selection
            int[] childA = selection(fitness, pd.getDistances());
            int[] childB = selection(fitness, pd.getDistances());
            // 6a Crossover
            int[] crossover = crossOver(childA, childB);
            // 6b Mutation
            int[] mutation = mutation(crossover);
            // 7 Put into new population
            newGeneration[((int) (Math.random() * popSize - 1)) + 1] = mutation;
            // 8 Replace the old population with the new population
            totalPopulation = newGeneration;
            counter++;
        }

        bestOfGen = selection(fitness, totalPopulation);

        return bestOfGen;
    }

    /**
     * Select the offspring with the highest probabilities, the ones that are not selected are cloned
     * @param fitness the fitness for one generation
     * @param population current population
     * @return the children with highest probability
     */
    private int[] selection(double[] fitness, int[][] population) {
        int index = 0;

        double r = Math.random();

        while (r > 0) {
            r = r - fitness[index];
            index++;
        }
        index--;

        return population[index];
    }

    private double getFitness(int[] child, TSPData pd) {
        double sum = 0.0;

        for (int value : child) {
            sum += value;
        }

        double start = findDistance(child, pd.getStartDistances());
        double end = findDistance(child, pd.getEndDistances());

        sum = sum - start - end;

        return (1/sum) * 1000;
    }

    private int findDistance(int[] child, int[] start) {
        int dist = 0;

        for (int value : child) {
            for (int i : start) {
                if (value == i) {
                    dist = value;
                    break;
                }
            }
        }

        return dist;
    }

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

        for (int temp : child2) {
            if (!newOffspring.contains(temp)) {
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
    	int populationSize = 18;
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
