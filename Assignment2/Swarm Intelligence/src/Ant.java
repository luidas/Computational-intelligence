import java.util.*;

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

        //Used to resolve loop problems.
        ArrayList<Coordinate> visited = new ArrayList<>();
        visited.add(start);

        //This loop creates the route
        while(!currentPosition.equals(end)){
            //Chooses the nextDirection with the highest probability. If more than one direction have the same
            //probability, then one direction is randomly chosen.
            Direction nextDirection = getNextMove(visited);

            //If no direction (means that it is a dead end), then go back to your steps.
            if(nextDirection == null){
                Direction before = route.getRoute().get(route.size() - 1);
                route.removeLast();
                visited.add(currentPosition);
                currentPosition = currentPosition.subtract(Direction.dirToCoordinateDelta(before));
            }
            //otherwise, update currentPosition and route.
            else{
                Coordinate nextMove = Direction.dirToCoordinateDelta(nextDirection);
                currentPosition = currentPosition.add(nextMove);
                route.add(nextDirection);
                visited.add(currentPosition);
            }
        }

        return route;
    }

    /**
     * Choose the next move, by choosing the highest probability (equation in the slides).
     * If more than one Direction have the same probability, randomly choose between those.
     * @param visited ArrayList of already visited Coordinates.
     * @return Direction, or null if dead end.
     */
    public Direction getNextMove(ArrayList<Coordinate> visited) {
        SurroundingPheromone surroundingPheromone = maze.getSurroundingPheromone(currentPosition);

        //Probabilities as key, Directions as values.
        HashMap<Double, ArrayList<Direction>> directions = new HashMap<>();

        double north = surroundingPheromone.get(Direction.North);
        double probaNorth = north/(surroundingPheromone.getTotalSurroundingPheromone());
        //If proba is zero, it means it is a wall, or it is out of bounds
        if(north!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.North)))) {
            //If probability is not yet in the HashMap
            if(!directions.containsKey(probaNorth)) {
                ArrayList<Direction> toAdd = new ArrayList<>();
                toAdd.add(Direction.North);
                directions.put(probaNorth, toAdd);
            }
            //If probability already in the HashMap
            else {
                ArrayList<Direction> previous = directions.get(probaNorth);
                previous.add(Direction.North);
                directions.put(probaNorth, previous);
            }
        }

        double south = surroundingPheromone.get(Direction.South);
        double probaSouth = south/(surroundingPheromone.getTotalSurroundingPheromone());
        //If proba is zero, it means it is a wall, or it is out of bounds
        if(south!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.South)))) {
            //If probability is not yet in the HashMap
            if(!directions.containsKey(probaSouth)) {
                ArrayList<Direction> toAdd = new ArrayList<>();
                toAdd.add(Direction.South);
                directions.put(probaSouth, toAdd);
            }
            //If probability already in the HashMap
            else {
                ArrayList<Direction> previous = directions.get(probaSouth);
                previous.add(Direction.South);
                directions.put(probaSouth, previous);
            }
        }

        double east = surroundingPheromone.get(Direction.East);
        double probaEast = east/(surroundingPheromone.getTotalSurroundingPheromone());
        //If proba is zero, it means it is a wall, or it is out of bounds
        if (east!=0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.East)))) {
            //If probability is not yet in the HashMap
            if(!directions.containsKey(probaEast)) {
                ArrayList<Direction> toAdd = new ArrayList<>();
                toAdd.add(Direction.East);
                directions.put(probaEast, toAdd);
            }
            //If probability already in the HashMap
            else {
                ArrayList<Direction> previous = directions.get(probaEast);
                previous.add(Direction.East);
                directions.put(probaEast, previous);
            }
        }

        double west = surroundingPheromone.get(Direction.West);
        double probaWest = west/(surroundingPheromone.getTotalSurroundingPheromone());
        //If proba is zero, it means it is a wall, or it is out of bounds
        if(west!= 0 && !visited.contains(currentPosition.add(Direction.dirToCoordinateDelta(Direction.West)))) {
            //If probability is not yet in the HashMap
            if(!directions.containsKey(probaWest)) {
                ArrayList<Direction> toAdd = new ArrayList<>();
                toAdd.add(Direction.West);
                directions.put(probaWest, toAdd);
            }
            //If probability already in the HashMap
            else {
                ArrayList<Direction> previous = directions.get(probaWest);
                previous.add(Direction.West);
                directions.put(probaWest, previous);
            }
        }
        
        double max = Integer.MIN_VALUE;
        for(double proba : directions.keySet()){
            max = Math.max(max, proba);
        }

        ArrayList<Direction> list = directions.get(max);

        if(list == null) return null;
        else return list.get(rand.nextInt(list.size()));
    }
}

