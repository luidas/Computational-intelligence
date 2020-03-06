import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

        ArrayList<Coordinate> visited = new ArrayList<>();
        visited.add(start);

        while(!currentPosition.equals(end)){
            Direction nextDirection = getNextMove(visited);
            Coordinate nextMove = Direction.dirToCoordinateDelta(nextDirection);

            currentPosition = currentPosition.add(nextMove);
            route.add(nextDirection);
            visited.add(currentPosition);
        }

        return route;
    }

    public Direction getNextMove(ArrayList<Coordinate> visited) {
        SurroundingPheromone surroundingPheromone = maze.getSurroundingPheromone(currentPosition);

        HashMap<Double, Direction> directions = new HashMap<>();

        double north = surroundingPheromone.get(Direction.North);
        double probaNorth = north/(surroundingPheromone.getTotalSurroundingPheromone());
        if(north!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.North)))) {
            directions.put(probaNorth, Direction.North);
        }

        double south = surroundingPheromone.get(Direction.South);
        double probaSouth = south/(surroundingPheromone.getTotalSurroundingPheromone());
        if(south!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.South)))) {
            directions.put(probaSouth, Direction.South);
        }

        double east = surroundingPheromone.get(Direction.East);
        double probaEast = east/(surroundingPheromone.getTotalSurroundingPheromone());
        if (east!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.East)))) {
            directions.put(probaEast, Direction.East);
        }

        double west = surroundingPheromone.get(Direction.West);
        double probaWest = west/(surroundingPheromone.getTotalSurroundingPheromone());
        if(west!= 0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.West)))) {
            directions.put(probaWest, Direction.West);
        }

        double max = Integer.MIN_VALUE;
        for(double proba : directions.keySet()){
            max = Math.max(max, proba);
        }

        return directions.get(max);
    }
}

