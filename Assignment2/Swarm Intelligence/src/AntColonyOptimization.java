import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class representing the first assignment. Finds shortest path between two points in a maze according to a specific
 * path specification.
 */
public class AntColonyOptimization {
	
	private int antsPerGen;
    private int generations;
    private double Q;
    private double evaporation;
    private Maze maze;

    /**
     * Constructs a new optimization object using ants.
     * @param maze the maze .
     * @param antsPerGen the amount of ants per generation.
     * @param generations the amount of generations.
     * @param Q normalization factor for the amount of dropped pheromone
     * @param evaporation the evaporation factor.
     */
    public AntColonyOptimization(Maze maze, int antsPerGen, int generations, double Q, double evaporation) {
        this.maze = maze;
        this.antsPerGen = antsPerGen;
        this.generations = generations;
        this.Q = Q;
        this.evaporation = evaporation;
    }

    /**
     * Loop that starts the shortest path process
     * @param spec specification of the route we wish to optimize
     * @return ACO optimized route
     */
    public Route findShortestRoute(PathSpecification spec) {
        Route route = new Route(spec.getStart());

        for(int gen = 1; gen <= generations; gen++){
            maze.evaporate(evaporation);

            ArrayList<Route> routes = new ArrayList<>();
            for(int ant = 1; ant <= antsPerGen; ant++){
                Ant newAnt = new Ant(maze, spec);
                Route temp = newAnt.findRoute();
                routes.add(temp);

                if(temp.shorterThan(route) || route.size() == 0) route = temp;
            }
            maze.addPheromoneRoutes(routes, Q);
        }
        return route;
    }

    /**
     * Driver function for Assignment 1
     */

    public static void main(String[] args) throws IOException {
        //parameters
//        FileWriter csvWriter = new FileWriter("medium evaporation.csv");
//        for(double evar = 0; evar <= 1; evar+=0.01){
            int gen = 100;
            int noGen = 100;
            double Q = 1600;
            double evap = 0.2;

            //construct the optimization objects
            Maze maze = Maze.createMaze("./data/medium maze.txt");
            PathSpecification spec = PathSpecification.readCoordinates("./data/medium coordinates.txt");
            AntColonyOptimization aco = new AntColonyOptimization(maze, gen, noGen, Q, evap);

            //save starting time
            long startTime = System.currentTimeMillis();

            //run optimization --> Everything starts with this method.
            Route shortestRoute = aco.findShortestRoute(spec);

            //print time taken
            System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));

            //save solution
            shortestRoute.writeToFile("./data/medium_solution.txt");

            //print route size
            System.out.println("Route size: " + shortestRoute.size());
//            csvWriter.append(shortestRoute.size() + "\n");

            //System.out.println(evar);
        }

//        csvWriter.flush();
//        csvWriter.close();
//    }
}
