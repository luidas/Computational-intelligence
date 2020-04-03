import numpy as np


# Create new class SinglePerceptron
class SinglePerceptron:

    # Constructor to create new Single Perceptron object
    # Epochs is the number of times the learning algorithm will run before stopping
    def __init__(self, num_inputs, learning_rate=0.01, epochs=100):
        self.weights = np.zeros(num_inputs + 1)
        self.learning_rate = learning_rate
        self.epochs = epochs

    # Calculate the new prediction, with current weights and bias
    def prediction(self, inputs):
        z = np.dot(inputs, self.weights[1:]) - self.weights[0]
        return 1 if z > 0 else 0

    # Train the perceptron for at least epoch times, update the weights accordingly
    def train(self, training_inputs, labels, csvFile):
        for _ in range(self.epochs):
            tot_error = 0
            # Zip training data and labels together, so you can loop through it in one time
            for inputs, label in zip(training_inputs, labels):
                y_hat = self.prediction(inputs)
                tot_error += (label - y_hat)
                # Update the weights and bias accordingly
                self.weights[1:] += self.learning_rate * (label - y_hat) * inputs
                self.weights[0] += self.learning_rate * (label - y_hat)
            csvFile.append(str(tot_error))
