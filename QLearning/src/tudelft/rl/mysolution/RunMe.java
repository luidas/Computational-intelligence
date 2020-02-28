package tudelft.rl.mysolution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import tudelft.rl.*;

public class RunMe {

	public static void main(String[] args) throws IOException {

		//load the maze
		//TODO replace this with the location to your maze on your file system

		Maze maze = new Maze(new File("QLearning/data/toy_maze.txt"));


		//Set the reward at the bottom right to 10
		maze.setR(maze.getState(9, 9), 10);
		maze.setR(maze.getState(9, 0), 5);

		//create a robot at starting and reset location (0,0) (top left)
		Agent robot = new Agent(0, 0);

		//make a selection object (you need to implement the methods in this class)
		EGreedy selection = new MyEGreedy();

		//make a Qlearning object (you need to implement the methods in this class)
		QLearning learn = new MyQLearning();

		int numberOfRuns = 0;
		double epsilon = 0.1;
		//keep learning until you decide to stop
		while (numberOfRuns < 100) {
			//TODO implement the action selection and learning cycle
			Action selectedAction = selection.getEGreedyAction(robot, maze, learn, 1-epsilon);
			State stateBeforeAction = robot.getState(maze);
			State stateAfterAction = robot.doAction(selectedAction, maze);
			learn.updateQ(stateBeforeAction, selectedAction, maze.getR(stateAfterAction), stateAfterAction,
					maze.getValidActions(robot), 0.7, 0.9);
			//TODO figure out a stopping criterion
			if (maze.getState(9, 9).equals(stateAfterAction) || maze.getState(9, 0).equals(stateAfterAction)) {
				robot.reset();
				numberOfRuns++;
				epsilon = epsilon *1.02221;
			}
		}
	}
}
