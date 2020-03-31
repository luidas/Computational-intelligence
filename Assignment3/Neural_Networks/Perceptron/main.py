import perceptron as p
import numpy as np
import cross_validation as cv


def main():
    features_file = np.genfromtxt('../features.txt', delimiter=',')
    targets_file = np.genfromtxt('../targets.txt', delimiter='')

    param_features = cv.get_validation(features_file)
    param_targets = cv.get_validation(targets_file)

    cv_features = cv.create_validation(features_file)
    cv_targets = cv.create_validation(targets_file)

    for i in range(10):
        test_set_length = cv.get_length(cv_targets)

        features_testing = cv.create_test(file=cv_features, l=test_set_length, i=i)
        targets_testing = cv.create_test(file=cv_targets, l=test_set_length, i=i)

        features_training = cv.create_training(file=cv_features, l=test_set_length, i=i)
        targets_training = cv.create_training(file=cv_targets, l=test_set_length, i=i)

        neurons = 30

        perceptron = p.Perceptron(features_training=features_training, targets_training=targets_training, neurons=neurons)
        perceptron.train()
        perceptron.test(features_test=features_testing, targets_test=targets_testing)


if __name__ == "__main__":
    main()
