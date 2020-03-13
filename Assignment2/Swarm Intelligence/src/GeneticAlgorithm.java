import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    int[] initChild = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};

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
      double[] fitness = getFitness(initGeneration, pd, currentGen);

      // 3) Select the best (highest fitness) children via selection
      Stack<Integer> indicesMaxFitness = new Stack<>();

      ArrayList<Double> tempFitness = new ArrayList<>();
      for (double item : fitness) {
        tempFitness.add(item);
      }

      for (int i = 0; i < fitness.length; i += 2) {
        ArrayList<int[]> tempChildren = new ArrayList<>();

        int[] child1 = selection(tempFitness, indicesMaxFitness, initGeneration);
        int[] child2 = selection(tempFitness, indicesMaxFitness, initGeneration);

        tempChildren.add(child1);
        tempChildren.add(child2);

        ArrayList<int[]> crossover = crossOver(tempChildren);

        ArrayList<int[]> mutation = mutation(crossover);

        initGeneration[indicesMaxFitness.pop()] = mutation.get(0);
        initGeneration[indicesMaxFitness.pop()] = mutation.get(1);

      }

      currentGen++;
    }

    // 6) Repeat step 2 - 5 until max number of generations is reached
    double[] fitness = getFitness(initGeneration, pd, currentGen);
    ArrayList<Double> tempFitness = new ArrayList<>();
    for (double item : fitness) {
      tempFitness.add(item);
    }

    double maxProbability = Collections.max(tempFitness);
    int indexMax = tempFitness.indexOf(maxProbability);

    return initGeneration[indexMax];
  }

    /**
     * Given two chromosomes, they are mutated or not.
     *
     * @param children chromosomes to consider for mutation
     * @return the mutated or original chromosomes
     */
  private ArrayList<int[]> mutation(ArrayList<int[]> children) {
    Random rand = new Random();
    ArrayList<int[]> tempChildren = new ArrayList<>();

    // Apply with probability of mutation = 0.001
    if (rand.nextFloat() <= 0.001) {
      for (int[] child : children) {
        int index1 = (int) (Math.random() * child.length);
        int index2 = (int) (Math.random() * child.length);

        int temp = child[index2];
        child[index2] = child[index1];
        child[index1] = temp;
        tempChildren.add(child);
      }
      return tempChildren;
    }
    return children;
  }

  /**
   * Given two chromosomes, they are crossed over or cloned.
   *
   * @param children to be crossed over
   * @return the crossed-over or cloned chromosomes
   */
  private ArrayList<int[]> crossOver(ArrayList<int[]> children) {
    Random rand = new Random();
    ArrayList<int[]> tempChildren = new ArrayList<>();

    // Apply crossover with probability of crossover = 0.7
    if (rand.nextFloat() <= 0.7) {
      for (int i = 0; i < 2; i++) {
        Random r = new Random();
        int start = r.nextInt(children.get(0).length);
        int end = r.nextInt(children.get(0).length - start) + start + 1;
        int[] newOffspringArray = Arrays.copyOfRange(children.get(i), start, end);
        List<Integer> newOffspring = Arrays.stream(newOffspringArray).boxed().collect(Collectors.toList());

        for (int temp : children.get(1)) {
          if (!newOffspring.contains(temp)) {
            newOffspring.add(temp);
          }
        }

        tempChildren.add(newOffspring.stream().mapToInt(j -> j).toArray());
      }
      return tempChildren;
    }

    // If crossover did not occur, return the children as is (ie "cloned")
    return children;
  }

  /**
   * Given the fitness of a generation, selects the fittest chromosome.
   *
   * @param fitness values calculated previously to be compared
   * @param indices the Stack in which we add fittest indices in order
   * @param generation the current generation
   * @return the fittest chromosome
   */
  private int[] selection(ArrayList<Double> fitness, Stack<Integer> indices, int[][] generation) {
    double maxProbability = Collections.max(fitness);
    int indexMax = fitness.indexOf(maxProbability);

    fitness.remove(indexMax);
    indices.push(indexMax);

    return generation[indexMax];
  }

  /**
   * Calculates the fitness of each chromosome in a generation.
   *
   * @param generation the set of chromosomes
   * @param pd the given distance data
   * @param currentGen the current generation
   * @return the fitness of each chromosome in the given generation
   */
  private double[] getFitness(int[][] generation, TSPData pd, int currentGen) {
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

    double sumFitness = 0.0;

    for (double item : fitnessOfGeneration) {
      sumFitness += item;
    }

    for (int i = 0; i < fitnessOfGeneration.length; i++) {
      fitnessOfGeneration[i] = (fitnessOfGeneration[i] / sumFitness);
    }

    return fitnessOfGeneration;
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
    tspData.writeActionFile(solution, "./Assignment2/Swarm Intelligence/data/TSP solution.txt");
  }
}
