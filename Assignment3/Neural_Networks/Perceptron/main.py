import perceptron as p
import numpy as np


def main():
    perceptron = p.Perceptron(num_inputs=2)

    # Inputs + labels for AND
    training_inputs = np.array([[1, 1], [1, 0], [0, 1], [0, 0]])
    labels = np.array([1, 0, 0, 0])

    perceptron.train(training_inputs, labels)

    # Test AND gate
    inputs = np.array([1, 1])
    print(perceptron.prediction(inputs))


if __name__ == "__main__":
    main()
