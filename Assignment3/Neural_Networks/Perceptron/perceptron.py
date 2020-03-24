import numpy as np

class Perceptron:

    # Constructor to create new Perceptron object
    # Epochs is the number of times the learning algorithm will run before stopping
    def __init__(self, features_training, targets_training, learning_rate=0.01, epochs=100):
        # We might want to change the initialisation of the weights here, something to consider
        self.input = features_training.T
        self.targets = targets_training
        self.inputWeight = np.random.rand(8, 10)
        self.hiddenWeight = np.random.rand(7, 8)
        self.learning_rate = learning_rate
        self.epochs = epochs
        self.output = np.zeros(7, 5890)

    # Train the perceptron for at least epoch times, update the weights accordingly
    def train(self, csvFile):
        for _ in range(self.epochs):
            tot_error = 0
            # Zip training data and labels together, so you can loop through it in one time
            for inputs, label in zip(self.features_training, self.targets):
                self.feedForward()
                tot_error += (label - self.output)

                if(...):
                    self.backprop()
                # Update the weights and bias accordingly
                #self.weights[1:] += self.learning_rate * (label - y_hat) * inputs
                #self.weights[0] += self.learning_rate * (label - y_hat)
            csvFile.append(str(tot_error))

    # TO BE CHANGED (copy pasted)
    def feedForward(self):
        #Don't know if you should add a threshold
        self.layer1 = np.sigmoid(np.dot(self.input, self.inputWeight))
        self.output = np.sigmoid(np.dot(self.layer1, self.hiddenWeight))

    #TO BE CHANGED (copy pasted)
    def backprop(self):
        # application of the chain rule to find derivative of the loss function with respect to weights2 and weights1
        #d_weights2 = np.dot(self.layer1.T, (2 * (self.y - self.output) * np.sigmoid_derivative(self.output)))
        #d_weights1 = np.dot(self.input.T, (np.dot(2 * (self.y - self.output) * np.sigmoid_derivative(self.output),
         #                                         self.weights2.T) * np.sigmoid_derivative(self.layer1)))

        delta_output = np.dot(np.dot(self.output, (1 - self.output)), something)
        weightCorr_output = self.learning_rate * self.output * delta_output

        delta_hidden = np.dot(np.dot(self.layer1, (1 - self.layer1)), delta_output * weightCorr_output)
        weightCorr_hidden = self.learning_rate * self.hiddenWeight * delta_hidden

        # update the weights with the derivative (slope) of the loss function
        self.inputWeight += weightCorr_hidden
        self.hiddenWeight += weightCorr_output