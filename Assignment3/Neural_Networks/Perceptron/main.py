import perceptron as p
import numpy as np
import csv


def main():
    features_file = np.genfromtxt( '../features.txt', delimiter = ',' )
    targets_file = np.genfromtxt( '../targets.txt', delimiter = ',' )
    features_training, features_testing = features_file[:5890], features_file[5890:]
    targets_training, targets_testing = targets_file[:5890], targets_file[5890:]

    perceptron = p.Perceptron(features_training, targets_training)

if __name__ == "__main__":
    main()

