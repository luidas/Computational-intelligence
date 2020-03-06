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

        while(currentPosition != end){



            Direction nextDirection = getNextMove(visited);
            Coordinate nextMove = Direction.dirToCoordinateDelta(nextDirection);


            currentPosition = currentPosition.add(nextMove);
            route.add(nextDirection);
            visited.add(currentPosition);

            System.out.println(currentPosition);
        }

        return route;
    }

    public Direction getNextMove(ArrayList<Coordinate> visited) {
        SurroundingPheromone surroundingPheromone = maze.getSurroundingPheromone(currentPosition);

        HashMap<Direction, Double> directions = new HashMap<>();


        double north = surroundingPheromone.get(Direction.North);
        double probaNorth = north/(surroundingPheromone.getTotalSurroundingPheromone());
        if(north!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.North)))) {
            directions.put(Direction.North, probaNorth);
        }

        double south = surroundingPheromone.get(Direction.South);
        double probaSouth = south/(surroundingPheromone.getTotalSurroundingPheromone());
        if(south!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.South)))) {
            directions.put(Direction.South, probaSouth);
        }

        double east = surroundingPheromone.get(Direction.East);
        double probaEast = east/(surroundingPheromone.getTotalSurroundingPheromone());
        if (east!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.East)))) {
            directions.put(Direction.East, probaEast);
        }

        double west = surroundingPheromone.get(Direction.West);
        double probaWest = west/(surroundingPheromone.getTotalSurroundingPheromone());
        if(west!= 0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.West)))) {
            directions.put(Direction.West, probaWest);
        }

        double max = Math.max(probaNorth, Math.max(probaSouth, Math.max(probaEast, probaWest)));

        Direction result = null;


        for(Direction dirr: directions.keySet()){
            if(directions.get(dirr) == max) result = dirr; break;
        }

        return result;
    }
}

