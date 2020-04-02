import numpy as np


class Perceptron:

    # Constructor to create new Perceptron object
    # Epochs is the number of times the learning algorithm will run before stopping
    def __init__(self, features_training, targets_training, neurons, learning_rate=0.01, epochs=30):
        self.input = features_training
        self.targets = targets_training
        self.neurons = neurons
        self.inputWeight = np.random.uniform(low=-2.4 / 47, high=2.4 / 47, size=(10, self.neurons))
        self.hiddenWeight = np.random.uniform(low=-2.4 / 47, high=2.4 / 47, size=(self.neurons, 7))
        self.biasInput = np.random.uniform(low=-2.4 / 47, high=2.4 / 47, size=(1, self.neurons))
        self.biasHidden = np.random.uniform(low=-2.4 / 47, high=2.4 / 47, size=(1, 7))
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.output = np.zeros((7, 1))

    def test(self, features_test, targets_test):
        correct = 0
        length = len(features_test)

        for i, row in enumerate(features_test):
            self.feedForward(row)

            max_output = np.max(self.output)
            rounded = np.where(self.output == max_output, 1, 0)
            if (rounded == self.toVector(targets_test[i] - 1)).all():
                correct = correct + 1

            self.output = np.zeros((7, 1))

        accuracy = correct / length

        with open('accuracy.csv', 'a') as csvFile:
            csvFile.write(str(accuracy))
            csvFile.write(", ")
            csvFile.write(str(self.neurons))
            csvFile.write('\n')

        print("Accuracy: ", correct / length)

    # Train the perceptron for at least epoch times, update the weights accordingly
    def train(self):
        for j in range(self.epochs):
            for i, row in enumerate(self.input):
                self.feedForward(row)
                self.backprop(row, self.toVector(self.targets[i] - 1))
                self.output = np.zeros((7, 1))

            # csvFile.append(str(tot_error))

    def feedForward(self, row):
        # Process for the input to hidden layer
        self.inputToHidden = self.sigmoid(row.dot(self.inputWeight) - self.biasInput)
        # Process for the hidden to output layer
        self.output = self.sigmoid(self.inputToHidden.dot(self.hiddenWeight) - self.biasHidden)

    def backprop(self, row, target):
        # Array of errors (difference between expected output, and predicted one)
        error_output = (target - self.output) * self.sigmoid_derivative(self.output)

        # Array of errors being back propagated from the output layer
        error_hidden = error_output.dot(self.hiddenWeight.T) * self.sigmoid_derivative(self.inputToHidden)

        # update the weights with the derivative (slope) of the loss function
        self.hiddenWeight += self.learning_rate * (self.inputToHidden.T.dot(error_output))
        row = np.reshape(row, (10, 1))
        self.inputWeight += self.learning_rate * (row.dot(error_hidden))

    def sigmoid(self, z):
        return 1 / (1 + np.exp(-1 * z))

    def sigmoid_derivative(self, z):
        f = self.sigmoid(z)
        return f * (1 - f)

    # Sets the target to a vector of size 7, in order to subtract the predicted output from it
    def toVector(self, target):
        zeroes = np.zeros(7)
        zeroes[int(target)] = 1
        return zeroes
