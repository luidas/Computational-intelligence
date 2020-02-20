package tudelft.rl.mysolution;

import java.util.ArrayList;

import tudelft.rl.Action;
import tudelft.rl.QLearning;
import tudelft.rl.State;

public class MyQLearning extends QLearning {

	@Override
	public void updateQ(State s, Action a, double r, State s_next, ArrayList<Action> possibleActions, double alfa, double gamma) {
		// TODO Auto-generated method stub
		double[] actionValues = super.getActionValues(s_next, possibleActions);
		double maxValue = Integer.MIN_VALUE;
		for (int i = 0; i < actionValues.length; i++) {
			double currentValue = actionValues[i];
			if (currentValue >= maxValue) {
				maxValue = currentValue;
			}
		}
		super.setQ(s, a, super.getQ(s, a) + alfa * (r + gamma * maxValue - super.getQ(s, a)));
	}

}
