import perceptron as p
import numpy as np

from Assignment3.Neural_Networks.Perceptron.cross_validation import create_validation, get_validation, create_training, create_test, \
    get_length


def main():
    # Read in the given files
    features_file = np.genfromtxt('../features.txt', delimiter=',')
    targets_file = np.genfromtxt('../targets.txt', delimiter='')

    # Only used in tandem with perceptron.evaluate_set(unknown_set)
    # unknown_set = np.genfromtxt('../unknown.txt', delimiter=',')

    # Used when setting up earlier on in the assignment (75% training, 15% validation, 10% testing)
    features_training, features_validation, features_testing = \
        features_file[:5890], features_file[5890:7068], features_file[7068:]
    targets_training, targets_validation, targets_testing = \
        targets_file[:5890], targets_file[5890:7068], targets_file[7068:]

    # NEEDED FOR K_FOLD CROSS VALIDATION
    # Set aside a portion of the data for validation
    param_features = get_validation(features_file)
    param_targets = get_validation(targets_file)

    # Set the rest of the data here for training and testing
    cv_features = create_validation(features_file)
    cv_targets = create_validation(targets_file)

    # For K-fold cross-validation, with k = 10
    for i in range(10):
        test_set_length = get_length(cv_targets)

        features_testing = create_test(file=cv_features, l=test_set_length, i=i)
        targets_testing = create_test(file=cv_targets, l=test_set_length, i=i)

        features_training = create_training(file=cv_features, l=test_set_length, i=i)
        targets_training = create_training(file=cv_targets, l=test_set_length, i=i)


        # Variable length for easy testing
        # Final updated value after checking SciKit
        neurons = 25

        perceptron = p.Perceptron(features_training=features_training, targets_training=targets_training, neurons=neurons)
        perceptron.train()
        perceptron.test(features_test=features_testing, targets_test=targets_testing)

        # This is used in 2.4 - Evaluation to evaluate the unknown set with our neural network. Is used without
        # using the k-fold cross-validation currently in place.
        # perceptron.evaluate_set(unknown_set)

if __name__ == "__main__":
    main()

