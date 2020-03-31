import perceptron as p
import numpy as np
from Assignment3.Neural_Networks.Perceptron.cross_validation import *


def main():
    features_file = np.genfromtxt('../features.txt', delimiter=',')
    targets_file = np.genfromtxt('../targets.txt', delimiter='')

    param_features = get_validation(features_file)
    param_targets = get_validation(targets_file)

    cv_features = create_validation(features_file)
    cv_targets = create_validation(targets_file)

    for i in range(10):
        test_set_length = get_length(cv_targets)

        features_testing = create_test(file=cv_features, l=test_set_length, i=i)
        targets_testing = create_test(file=cv_targets, l=test_set_length, i=i)

        features_training = create_training(file=cv_features, l=test_set_length, i=i)
        targets_training = create_training(file=cv_targets, l=test_set_length, i=i)

        neurons = 30

        perceptron = p.Perceptron(features_training=features_training, targets_training=targets_training, neurons=neurons)
        perceptron.train()
        perceptron.test(features_test=features_testing, targets_test=targets_testing)


if __name__ == "__main__":
    main()
