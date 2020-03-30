import perceptron as p
import numpy as np


def main():
    features_file = np.genfromtxt( '../features.txt', delimiter = ',' )
    targets_file = np.genfromtxt( '../targets.txt', delimiter = '' )
    features_training, features_validation, features_testing = features_file[:5890], features_file[5890:7068], features_file[7068:]
    targets_training, targets_validation, targets_testing = targets_file[:5890], targets_file[5890:7068], targets_file[7068:]

    neurons = 30

    perceptron = p.Perceptron(features_training, targets_training, neurons)
    perceptron.train()
    perceptron.test(features_testing, targets_testing)


if __name__ == "__main__":
    main()

