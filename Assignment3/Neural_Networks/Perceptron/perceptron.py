import numpy as np


class Perceptron:

    # Constructor to create new Perceptron object
    # Epochs is the number of times the learning algorithm will run before stopping
    def __init__(self, features_training, targets_training, neurons, learning_rate=0.01, epochs=30):
        self.input = features_training
        self.targets = targets_training
        self.neurons = neurons
        self.inputWeight = np.random.uniform(low=-2.4 / 25, high=2.4 / 25, size=(10, self.neurons))
        self.hiddenWeight = np.random.uniform(low=-2.4 / 25, high=2.4 / 25, size=(self.neurons, 7))
        self.biasInput = np.random.uniform(low=-2.4 / 25, high=2.4 / 25, size=(1, self.neurons))
        self.biasHidden = np.random.uniform(low=-2.4 / 25, high=2.4 / 25, size=(1, 7))
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.output = np.zeros((7, 1))

    # Test the network with the test set
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

        print("Accuracy: ", accuracy)

    # Train the perceptron for at least epoch times, update the weights accordingly
    def train(self):
        for j in range(self.epochs):
            for i, row in enumerate(self.input):
                self.feedForward(row)
                self.backprop(row, self.toVector(self.targets[i] - 1))
                self.output = np.zeros((7, 1))

    # Move the inputs forward through the network, input -> hidden layer -> output layer
    def feedForward(self, row):
        # Process for the input to hidden layer
        self.inputToHidden = self.sigmoid(row.dot(self.inputWeight) - self.biasInput)
        # Process for the hidden to output layer
        self.output = self.sigmoid(self.inputToHidden.dot(self.hiddenWeight) - self.biasHidden)

    # Back propagate the error to update the weights applied to the input appropriately
    def backprop(self, row, target):
        error_output = (target - self.output) * self.sigmoid_derivative(self.output)
        error_hidden = error_output.dot(self.hiddenWeight.T) * self.sigmoid_derivative(self.inputToHidden)

        # update the weights with the derivative (slope) of the loss function
        self.hiddenWeight += self.learning_rate * (self.inputToHidden.T.dot(error_output))
        row = np.reshape(row, (10, 1))
        self.inputWeight += self.learning_rate * (row.dot(error_hidden))

    # Activation function, in our case, the Sigmoid function
    def sigmoid(self, z):
        return 1 / (1 + np.exp(-1 * z))

    # The derivative of the Sigmoid function, used in the loss function
    def sigmoid_derivative(self, z):
        f = self.sigmoid(z)
        return f * (1 - f)

    # Create vector to properly compare self.output and target
    def toVector(self, target):
        zeroes = np.zeros(7)
        zeroes[int(target)] = 1
        return zeroes

    # Function to evaluate the unknown set with our network
    def evaluate_set(self, set):
        with open("Group_50_classes.txt", "a") as file:
            for i, row in enumerate(set):
                self.feedForward(row)
                max_output = np.max(self.output)
                rounded = np.where(self.output == max_output, 1, 0)

                file.write(str(np.where(rounded == 1)[1][0] + 1))
                file.write(",")

                self.output = np.zeros((7, 1))