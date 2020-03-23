import numpy as np


class Perceptron:
    alpha = bias = sum = z = 0

    # Constructor to create new Perceptron object
    def __init__(self, num_inputs, bias, alpha):
        self.weights = np.random.random(num_inputs)
        self.bias = bias
        self.alpha = alpha

    # Get the weights
    def get_weights(self):
        return self.weights

    # Calculate weighted sum between input and weights
    def calc_weighted_sum(self, inputs):
        self.sum = np.dot(inputs, self.weights)

    # Calculate the z, used in the step function
    def calc_z(self):
        self.z = self.sum + self.bias

    # Determine the sign of the z-value
    def activation_func(self, inputs):
        self.calc_weighted_sum(inputs)
        self.calc_z()
        return np.sign(self.z)

    # def update_weights(self):
