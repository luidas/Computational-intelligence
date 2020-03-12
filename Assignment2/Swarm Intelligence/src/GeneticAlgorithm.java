import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TSP problem solver using genetic algorithms.
 */
public class GeneticAlgorithm {
    private int generations;
    private int popSize;
    //private static final int STOPPING = 10;
    // Why do we need this?
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

        int numGeneration = 0;

        // Keep doing the process until we have reached the total number of generations
        // However, we must decide if we in one generation mutate all children or only some of them
        // because we need another for loop in the while loop and it becomes very expensive operation
        while(numGeneration < generations) {
            // 2) Determine the fitness of this generation
            double[] fitness = getFitness(initGeneration, pd, numGeneration);
            // 3) Select the best (children with the highest fitness) children -> selection

            Stack<Integer> indicesProbabilities = new Stack<>();

            ArrayList<Double> tempFitness = new ArrayList<>();
            for(double item : fitness){
                tempFitness.add(item);
            }

            for(int i = 0; i < fitness.length; i+=2) {
                ArrayList<int[]> tempChildren = new ArrayList<>();

                int[] child1 = selection(tempFitness, indicesProbabilities, initGeneration);
                int[] child2 = selection(tempFitness, indicesProbabilities, initGeneration);

                tempChildren.add(child1);
                tempChildren.add(child2);

                ArrayList<int[]> crossover = crossOver(tempChildren);

                ArrayList<int[]> mutation = mutation(crossover);

                initGeneration[indicesProbabilities.pop()] = mutation.get(0);
                initGeneration[indicesProbabilities.pop()] = mutation.get(1);

            }

            numGeneration++;
        }
        // 6) Repeat step 2 - 5 until max number of generations is reached

        double[] fitness = getFitness(initGeneration, pd, numGeneration);
        ArrayList<Double> tempFitness = new ArrayList<>();
        for(double item : fitness){
            tempFitness.add(item);
        }

        double maxProbability = Collections.max(tempFitness);
        int indexMax = tempFitness.indexOf(maxProbability);

        return initGeneration[indexMax];
    }

    private ArrayList<int[]> mutation(ArrayList<int[]> children) {
        // double pm = 0.001; <-- Ask this
        // Bit flip or swapping two products?
        Random rand = new Random();
        ArrayList<int[]> tempChildren = new ArrayList<>();

        if(rand.nextFloat() <= 0.001) {
            for(int[] child: children){
                int index1 = (int)(Math.random() * child.length);
                int index2 = (int)(Math.random() * child.length);

                int temp = child[index2];
                child[index2] = child[index1];
                child[index1] = temp;
                tempChildren.add(child);
            }
            return tempChildren;
        }
        return children;
    }

    private ArrayList<int[]> crossOver(ArrayList<int[]> children) {
        // So for the selection we should take into account the probability that one child is getting crossover and mutated
        // double crossoverProb = 0.7; <-- Ask this
        Random rand = new Random();
        ArrayList<int[]> tempChildren = new ArrayList<>();

        if(rand.nextFloat() <= 0.7) {
            for(int i = 0; i < 2; i++){
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

                tempChildren.add(newOffspring.stream().mapToInt(j -> (int) j).toArray());
            }
            return tempChildren;
        }
        return children;
    }

    private int[] selection(ArrayList<Double> fitness, Stack<Integer> indices, int[][] generation) {
        /**int index = 0;

        double r = Math.random();

        while (r > 0) {
            r = r - fitness.get(index);
            index++;
        }
        index--;

        fitness.remove(index);

        return generation[index];**/

        double maxProbability = Collections.max(fitness);
        int indexMax = fitness.indexOf(maxProbability);

        fitness.remove(indexMax);
        indices.push(indexMax);

        return generation[indexMax];

    }

    private double[] getFitness(int[][] generation,TSPData pd, int numGeneration) {
        double[] fitnessOfGeneration = new double[popSize];
        int[] startDistances = pd.getStartDistances();
        int[] endDistances = pd.getEndDistances();

        // For each child of the generation determine the distance, of the order a child
        for(int i = 0; i < popSize; i++) {
            double sum = 0.0;

            int[] child = generation[i];
            int childStartIndex = child[0];
            int childEndIndex = child[child.length - 1];
            int start = startDistances[childStartIndex];
            int end = endDistances[childEndIndex];

            // Assumption made here: the first and last element of the child are the start and finish of the route taken in the maze
            // All products in between are the products that lay between the start and finish
            for(int j = 1; j < child.length; j++) {
               int tempProduct = child[j];
               int previousProduct = child[j - 1];
               // Lookup the distance from the previous product to this product
               // Keep in mind you still have to determine the distance from the first product(from start to pr1 -> pr1 -> pr2) to the next product, same for the finish
               int distanceBetween = pd.getDistances()[previousProduct][tempProduct];

               sum += distanceBetween;
            }

            sum = sum + end + start;
            fitnessOfGeneration[i] = (1 / (Math.pow(sum, 8) + 1)) * Math.pow(10, numGeneration);
        }

        double sumFitness = 0.0;

        for(double item: fitnessOfGeneration){
            sumFitness += item;
        }
        for(int i = 0; i < fitnessOfGeneration.length; i++){
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
        tspData.writeActionFile(solution, "./data/TSP solution.txt");
    }
}
