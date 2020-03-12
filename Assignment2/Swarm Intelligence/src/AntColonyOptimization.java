import sun.awt.image.VolatileSurfaceManager;

import java.io.FileNotFoundException;
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

        //For loop for every Generations
        for(int gen = 1; gen <= generations; gen++){
            //All ants stored in ArrayList so this way they don't learn from each other in the same run.
            ArrayList<Route> routes = new ArrayList<>();

            //For loop for every ants per generation.
            for(int ant = 1; ant <= antsPerGen; ant++){
                Ant newAnt = new Ant(maze, spec);
                Route temp = newAnt.findRoute();
                routes.add(temp);

                //This if statement is used to always keep the smallest route. However we don't know if
                //this is alright for the ACO optimization --> the slides ask for the very last ant.
                if(temp.size() < route.size() || route.size() == 0){
                    route = temp;
                }
            }
            maze.evaporate(evaporation);
            maze.addPheromoneRoutes(routes, Q);
        }

        return route;
    }

    /**
     * Driver function for Assignment 1
     */
    public static void main(String[] args) throws FileNotFoundException {
    	//parameters
    	int gen = 100;
        int noGen = 100;
        double Q = 1600;
        double evap = 0.1;
        
        //construct the optimization objects
        Maze maze = Maze.createMaze("./data/insane maze.txt");
        PathSpecification spec = PathSpecification.readCoordinates("./data/insane coordinates.txt");
        AntColonyOptimization aco = new AntColonyOptimization(maze, gen, noGen, Q, evap);
        
        //save starting time
        long startTime = System.currentTimeMillis();

        //run optimization --> Everything starts with this method.
        Route shortestRoute = aco.findShortestRoute(spec);

        //print time taken
        System.out.println("Time taken: " + ((System.currentTimeMillis() - startTime) / 1000.0));
        
        //save solution
        shortestRoute.writeToFile("./data/insane.txt");

        //print route size
        System.out.println("Route size: " + shortestRoute.size());
    }
}
