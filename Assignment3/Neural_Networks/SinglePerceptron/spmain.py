import Assignment3.Neural_Networks.SinglePerceptron.singleperceptron as p
import numpy as np


def main():
    # Create a perceptron
    perceptron = p.SinglePerceptron(num_inputs=2)

    # Inputs + labels for AND
    training_inputs_AND = np.array([[1, 1], [1, 0], [0, 1], [0, 0]])
    labels_AND = np.array([1, 0, 0, 0])

    # Inputs + labels for OR
    training_inputs_OR = np.array([[1, 1], [1, 0], [0, 1], [0, 0]])
    labels_OR = np.array([1, 1, 1, 0])

    # Inputs + labels for XOR
    training_inputs_XOR = np.array([[1, 1], [1, 0], [0, 1], [0, 0]])
    labels_XOR = np.array([0, 1, 1, 0])

    # Collect outputs to file to be graphed
    csvFile = ["outputs"]

    # Train the single perceptron
    perceptron.train(training_inputs_AND, labels_AND, csvFile)

    with open('data.csv', 'w') as file:
        for line in csvFile:
            file.write(line)
            file.write("\n")

    # Test AND gate
    inputs = np.array([0, 0])
    print(perceptron.prediction(inputs))


if __name__ == "__main__":
    main()
