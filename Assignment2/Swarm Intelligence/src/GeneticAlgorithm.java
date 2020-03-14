import javafx.util.Pair;
import sun.security.util.ArrayUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * TSP problem solver using genetic algorithms.
 */
public class GeneticAlgorithm {
  private int generations;
  private int popSize;

  /**
   * Constructs a new 'genetic algorithm' object.
   *
   * @param generations the amount of generations.
   * @param popSize     the population size.
   */
  public GeneticAlgorithm(int generations, int popSize) {
    this.generations = generations;
    this.popSize = popSize;
  }


  /**
   * Knuth-Yates shuffle, reordering a array randomly
   *
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
   *
   * @param pd the TSP data.
   * @return the optimized product sequence.
   */
  public int[] solveTSP(TSPData pd) {
    // 1) Create the initial generation with shuffle
    int[][] distances = pd.getDistances();
    int[][] initGeneration = new int[popSize][distances.length];
    int[] initChild = IntStream.range(0, 18).toArray();

    initGeneration[0] = initChild;

    // Fill the initGeneration with randomly ordered children
    for (int i = 1; i < popSize; i++) {
      int[] temp = Arrays.copyOf(initGeneration[i - 1], distances.length);
      shuffle(temp);
      initGeneration[i] = temp;
    }

    int currentGen = 0;

    // Keep doing the process until we have reached the predefined number of generations
    while (currentGen < generations) {
      // 2) Determine the fitness of each chromosome in every generation
      List<Double> fitness = getFitness(initGeneration, pd, currentGen);
      List<Double> tempFitness = getFitness(initGeneration, pd, currentGen);

      // 3) Select the best (highest fitness) children via selection
      Stack<Integer> indicesMaxFitness = new Stack<>();

      for (int i = 0; i < fitness.size(); i += 2) {
        List<int[]> children = new ArrayList<>();

        // Get two children with current highest fitness probabilities
        int[] child1 = selection(tempFitness, indicesMaxFitness, initGeneration);
        int[] child2 = selection(tempFitness, indicesMaxFitness, initGeneration);

        children.add(child1);
        children.add(child2);

        Random randCrossOver = new Random();
        // Apply crossover with probability 0.7
        if (randCrossOver.nextFloat() <= 0.7) {
          children = crossOver(children);
        }

        // Apply mutation with probability 0.001
        Random randMutation = new Random();
        if(randMutation.nextFloat() <= 0.001) {
          mutation(children);
        }

        initGeneration[indicesMaxFitness.pop()] = children.get(0);
        initGeneration[indicesMaxFitness.pop()] = children.get(1);
      }
      currentGen++;
    }

    // 6) Repeat step 2 - 5 until max number of generations is reached
    // 7) Select the fitess child of the last generation
    List<Double> fitness = getFitness(initGeneration, pd, currentGen);

    double maxProbability = Collections.max(fitness);
    int indexMax = fitness.indexOf(maxProbability);

    return initGeneration[indexMax];
  }

    /**
     * Given two chromosomes, they are either mutated or cloned.
     * @param children chromosomes to consider for mutation
     */
  private void mutation(List<int[]> children) {
    for (int[] child : children) {
      int index1 = (int) (Math.random() * child.length);
      int index2 = (int) (Math.random() * child.length);

      int temp = child[index2];
      child[index2] = child[index1];
      child[index1] = temp;
    }
  }

  /**
   * Given two chromosomes, they are either crossed over or cloned.
   * @param children
   * @return children that crossover had been applied to
   */
  private List<int[]> crossOver(List<int[]> children) {

    List<int[]> tempChildren = new ArrayList<>();

    for (int child = 0; child < 2; child++) {
      Random r = new Random();
      int start = r.nextInt(children.get(0).length);
      int end = r.nextInt(children.get(0).length - start) + start + 1;

      List<Integer> newOffSpring = Arrays.stream(Arrays.copyOfRange(children.get(0), start, end)).boxed().collect(Collectors.toList());

      for (int gene : children.get(1)) {
        if (!newOffSpring.contains(gene)) {
          newOffSpring.add(gene);
        }
      }
      tempChildren.add(newOffSpring.stream().mapToInt(j -> (int) j).toArray());
    }

    return tempChildren;
  }

  /**
   * Given the fitness of a generation, selects the fittest chromosome/child.
   * @param fitness values calculated previously to be compared
   * @param indices the Stack in which we add fittest indices in order
   * @param generation the current generation
   * @return the fittest chromosome
   */
  private int[] selection(List<Double> fitness, Stack<Integer> indices, int[][] generation) {
    double maxProbability = Collections.max(fitness);
    int indexMax = fitness.indexOf(maxProbability);

    fitness.remove(indexMax);
    indices.push(indexMax);

    return generation[indexMax];
  }

  /**
   * Calculates the fitness of each chromosome in a generation.
   * @param generation the set of chromosomes
   * @param pd the given distance data
   * @param currentGen the current generation
   * @return an array with the fitness of each chromosome/child in the given generation
   */
  private List<Double> getFitness(int[][] generation, TSPData pd, int currentGen) {
    double[] fitnessOfGeneration = new double[popSize];
    int[] startDistances = pd.getStartDistances();
    int[] endDistances = pd.getEndDistances();

    // For each child of the generation determine the total distance, or the order a child
    for (int i = 0; i < popSize; i++) {
      double sum = 0.0;

      int[] child = generation[i];
      int childStartIndex = child[0];
      int childEndIndex = child[child.length - 1];
      int start = startDistances[childStartIndex];
      int end = endDistances[childEndIndex];

      // Assumption made here: the first elements is visited after the start & last element is visited last before the finish.
      // All products in between are the products that lay between the start and finish
      for (int j = 1; j < child.length; j++) {
        int previousProduct = child[j - 1]; // Need this reference to check the distance from previous to current element
        int tempProduct = child[j];
        int distanceBetween = pd.getDistances()[previousProduct][tempProduct];

        sum += distanceBetween;
      }

      // Add all of the distances between the first and last element as well as the start and finish distances.
      sum = sum + end + start;
      fitnessOfGeneration[i] = (1 / (Math.pow(sum, 8) + 1)) * Math.pow(10, currentGen);
    }

    double sumFitness = Arrays.stream(fitnessOfGeneration).sum();

    return Arrays.stream(fitnessOfGeneration).map(n -> n/sumFitness).boxed().collect(Collectors.toList());
  }


  /**
   * Assignment 2.b
   */
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    //parameters
    int populationSize = 1000;
    int generations = 1000;
    String persistFile = "./productMatrixDist";

    //setup optimization
    TSPData tspData = TSPData.readFromFile(persistFile);
    GeneticAlgorithm ga = new GeneticAlgorithm(generations, populationSize);

    //run optimzation and write to file
    int[] solution = ga.solveTSP(tspData);
    tspData.writeActionFile(solution, "./data/TSP solution.txt");
  }
}
