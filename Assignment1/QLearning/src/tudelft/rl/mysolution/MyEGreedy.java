package tudelft.rl.mysolution;

import tudelft.rl.Action;
import tudelft.rl.Agent;
import tudelft.rl.EGreedy;
import tudelft.rl.Maze;
import tudelft.rl.QLearning;
import tudelft.rl.State;

import java.util.ArrayList;
import java.util.Random;

public class MyEGreedy extends EGreedy {

	@Override
	public Action getRandomAction(Agent r, Maze m) {
		//TODO to select an action at random in State s
		ArrayList<Action> validActions = m.getValidActions(r);
		Random random = new Random();
		int randomVal = random.nextInt(validActions.size());
		return validActions.get(randomVal);
	}

	@Override
	public Action getBestAction(Agent r, Maze m, QLearning q) {
		//TODO to select the best possible action currently known in State s.

		ArrayList<Action> validActions = m.getValidActions(r);
		double maxQ = Integer.MIN_VALUE;
		Action bestAction = null;
		for (Action a : validActions) {
			double currentQ = q.getQ(r.getState(m), a);
			if (currentQ >= maxQ && currentQ != 0) {
				maxQ = currentQ;
				bestAction = a;
			}
		}
		if(bestAction == null) {
			return getRandomAction(r, m);
		}
		return bestAction;
	}

	@Override
	public Action getEGreedyAction(Agent r, Maze m, QLearning q, double epsilon) {
		//TODO to select between random or best action selection based on epsilon.
		double random = Math.random();
		if (random <= epsilon) {
			return getRandomAction(r, m);
		}
		return getBestAction(r, m, q);
	}

}
