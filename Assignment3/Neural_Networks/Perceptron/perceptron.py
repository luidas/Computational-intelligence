import numpy as np


class Perceptron:

    # Constructor to create new Perceptron object
    # Epochs is the number of times the learning algorithm will run before stopping
    def __init__(self, features_training, targets_training, learning_rate=0.01, epochs=10):
        # We might want to change the initialisation of the weights here, something to consider
        self.input = features_training
        self.targets = targets_training
        self.inputWeight = np.random.rand(10, 8)
        self.hiddenWeight = np.random.rand(8, 7)
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.output = np.zeros((7, 1))

    # Train the perceptron for at least epoch times, update the weights accordingly
    def train(self):
        for _ in range(self.epochs):
            tot_error = 0

            for i, row in enumerate(self.input):
                self.feedForward(row)
                self.backprop(row, i)
            # csvFile.append(str(tot_error))

    def feedForward(self, row):
        # Don't know if you should add a threshold
        self.layer1 = self.sigmoid(np.dot(row, self.inputWeight))
        self.output = self.sigmoid(np.dot(self.layer1, self.hiddenWeight))

    # TO BE CHANGED (copy pasted)
    def backprop(self, row, i):
        print(np.shape(row), np.shape(self.hiddenWeight), np.shape(self.sigmoid_derivative(self.layer1)))

        d_weights2 = np.dot(self.layer1.T, (2 * np.abs((self.targets[i] - np.amax(self.output))) * self.sigmoid_derivative(np.amax(self.output))))
        d_weights1 = np.dot(row.T, (np.dot(2 * (self.targets[i] - np.amax(self.output)) * self.sigmoid_derivative(np.amax(self.output)),
                                                  self.hiddenWeight.T) * self.sigmoid_derivative(self.layer1)))

        # update the weights with the derivative (slope) of the loss function
        self.weights1 += d_weights1
        self.weights2 += d_weights2

        #delta_output = np.dot(np.dot(self.output, (1 - self.output).T), self.targets - np.amax(self.output, axis = 1))
        #weightCorr_output = self.learning_rate * np.dot(np.amax(self.output, axis = 1), delta_output)

        #print(np.shape(self.layer1))
        #delta_hidden = np.dot(np.dot(self.layer1, (1 - self.layer1).T), np.sum(np.dot(weightCorr_output, delta_output.T)))
        #weightCorr_hidden = self.learning_rate * np.dot(self.hiddenWeight , delta_hidden)

    def sigmoid(self, z):
        return 1 / (1 + np.exp(-1 * z))

    def sigmoid_derivative(self, z):
        f = self.sigmoid(z)
        return f * (1 - f)
