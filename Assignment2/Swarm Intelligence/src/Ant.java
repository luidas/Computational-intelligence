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
                // This is a very expensive operation, easier would be temporary store the route and get the previous locations
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
    private Direction getNextMove(ArrayList<Coordinate> visited) {
        SurroundingPheromone surroundingPheromone = maze.getSurroundingPheromone(currentPosition);

        //Probabilities as key, Directions as values.
        HashMap<Double, ArrayList<Direction>> directions = new HashMap<>();

        // For every direction, calculate probabilities and place in the hashmap
        for(Direction direction : Direction.values()) {
            getCoordinateProbability(direction.name(), surroundingPheromone, directions, visited);
        }

        if (directions.isEmpty()) return null;

        double highestProbability= Collections.max(directions.keySet());

        ArrayList<Direction> coordinates = directions.get(highestProbability);

        return coordinates.get(rand.nextInt(coordinates.size()));
    }

    /**
     * Helper function: for the current position, it computes the probabilities for all valid actions
     * @param dir, either North, West, East, South
     * @param surroundingPheromone , the amount of pheromone for a specific direction
     * @param directions, probabilities
     * @param visited, places already visited
     */
    private void getCoordinateProbability(String dir, SurroundingPheromone surroundingPheromone, HashMap<Double, ArrayList<Direction>> directions, ArrayList<Coordinate> visited) {
        Direction coordinateDir = Direction.valueOf(dir);
        double direction = surroundingPheromone.get(coordinateDir);
        double probabilityDir = direction / (surroundingPheromone.getTotalSurroundingPheromone());
        // If the probability equals zero, it is a wall, or out of bounds, else is a possible path
        Coordinate coordinate = Direction.dirToCoordinateDelta(coordinateDir);
        if (probabilityDir != 0 && !visited.contains(currentPosition.add(coordinate))) {
            // If the probability is not yet in the HashMap
            if (!directions.containsKey(probabilityDir)){
                directions.put(probabilityDir, new ArrayList<Direction>(Collections.singleton(coordinateDir)));
            }
            // If the probability is already in the HashMap
            else {
                ArrayList<Direction> temp = directions.get(probabilityDir);
                temp.add(coordinateDir);
                directions.put(probabilityDir, temp);
            }
        }
    }
}

