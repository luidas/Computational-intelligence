import perceptron as p
import numpy as np


def main():
    perceptron = p.Perceptron(num_inputs=2)

    # Inputs + labels for AND
    training_inputs_AND = np.array([[1, 1], [1, 0], [0, 1], [0, 0]])
    labels_AND = np.array([1, 0, 0, 0])

    # Inputs + labels for OR
    training_inputs_OR = np.array([[1, 1], [1, 0], [0, 1], [0, 0]])
    labels_OR = np.array([1, 1, 1, 0])

    # Inputs + labels for XOR
    training_inputs_XOR = np.array([[1, 1], [1, 0], [0, 1], [0, 0]])
    labels_XOR = np.array([0, 1, 1, 0])

    csvFile = ["outputs"]

    perceptron.train(training_inputs_XOR, labels_XOR, csvFile)

    with open('data.csv', 'w') as file:
        for line in csvFile:
            file.write(line)
            file.write("\n")

    # Test AND gate
    #inputs = np.array([1, 0])
    #print(perceptron.prediction(inputs))


if __name__ == "__main__":
    main()
