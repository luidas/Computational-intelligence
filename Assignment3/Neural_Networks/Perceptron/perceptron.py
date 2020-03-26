import numpy as np


class Perceptron:

    # Constructor to create new Perceptron object
    # Epochs is the number of times the learning algorithm will run before stopping
    def __init__(self, features_training, targets_training, learning_rate=0.01, epochs=10):
        # We might want to change the initialisation of the weights here, something to consider
        self.input = features_training
        self.targets = targets_training
        self.inputWeight = np.random.uniform(low=-2.4 / 25, high=2.4 / 25, size=(10, 8))
        self.hiddenWeight = np.random.uniform(low=-2.4 / 25, high=2.4 / 25, size=(8, 7))
        self.biasInput = np.random.uniform(low=-2.4 / 25, high=2.4 / 25, size=(1, 8))
        self.biasHidden = np.random.uniform(low=-2.4 / 25, high=2.4 / 25, size=(1, 7))
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.output = np.zeros((7, 1))

    # Train the perceptron for at least epoch times, update the weights accordingly
    def train(self):
        for _ in range(self.epochs):
            tot_error = 0

            for i, row in enumerate(self.input):
                self.feedForward(row)
                self.backprop(row, self.toVector(self.targets[i] - 1))
            # csvFile.append(str(tot_error))

    def feedForward(self, row):
        # Process for the input to hidden layer
        self.inputToHidden = self.sigmoid(row.dot(self.inputWeight) - self.biasInput)
        # Process for the hidden to output layer
        self.output = self.sigmoid(self.inputToHidden.dot(self.hiddenWeight) - self.biasHidden)

    # TO BE CHANGED (copy pasted)
    def backprop(self, row, target):
        error_output = (target - self.output) * self.sigmoid_derivative(self.output)
        # error_output = np.where(error_output < 0, 0, error_output)
        error_hidden = error_output.dot(self.hiddenWeight.T) * self.sigmoid_derivative(self.inputToHidden)
        # error_hidden = np.where(error_hidden < 0, 0, error_hidden)

        # update the weights with the derivative (slope) of the loss function
        self.hiddenWeight += self.learning_rate * (self.inputToHidden.T.dot(error_output))
        row = np.reshape(row, (10, 1))
        self.inputWeight += self.learning_rate * (row.dot(error_hidden))

    def sigmoid(self, z):
        return 1 / (1 + np.exp(-1 * z))

    def sigmoid_derivative(self, z):
        f = self.sigmoid(z)
        return f * (1 - f)

    def toVector(self, target):
        zeroes = np.zeros(7)
        zeroes[int(target)] = 1
        return zeroes
