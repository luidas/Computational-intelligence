import java.util.Random;

/**
 * Class that represents the ants functionality.
 */
public class Ant {
	
    private Maze maze;
    private Coordinate start;
    private Coordinate end;
    private Coordinate currentPosition;
    private static Random rand;

    /**
     * Constructor for ant taking a Maze and PathSpecification.
     * @param maze Maze the ant will be running in.
     * @param spec The path specification consisting of a start coordinate and an end coordinate.
     */
    public Ant(Maze maze, PathSpecification spec) {
        this.maze = maze;
        this.start = spec.getStart();
        this.end = spec.getEnd();
        this.currentPosition = start;
        if (rand == null) {
            rand = new Random();
        }
    }

    /**
     * Method that performs a single run through the maze by the ant.
     * @return The route the ant found through the maze.
     */
    public Route findRoute() {
        Route route = new Route(start);

        while(currentPosition != end){
            SurroundingPheromone surroundingPheromone = maze.getSurroundingPheromone(currentPosition);

            double north = surroundingPheromone.get(Direction.North);
            double probaNorth = north/(surroundingPheromone.getTotalSurroundingPheromone());

            double south = surroundingPheromone.get(Direction.South);
            double probaSouth = south/(surroundingPheromone.getTotalSurroundingPheromone());

            double east = surroundingPheromone.get(Direction.East);
            double probaEast = east/(surroundingPheromone.getTotalSurroundingPheromone());

            double west = surroundingPheromone.get(Direction.West);
            double probaWest = west/(surroundingPheromone.getTotalSurroundingPheromone());

            Math.max(probaNorth, Math.max(probaSouth, Math.max(probaEast, probaWest)));
        }

        return route;
    }
}

