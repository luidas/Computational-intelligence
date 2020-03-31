import perceptron as p
import numpy as np


def main():
    features_file = np.genfromtxt('../features.txt', delimiter=',')
    targets_file = np.genfromtxt('../targets.txt', delimiter='')

    # Keep this part for Hyper Parameter Tuning, other part is for Cross Validation, 1/10 of the original file
    hyper_param_features = features_file[0:int(len(features_file) * 0.1)]
    hyper_param_targets = targets_file[0:int(len(targets_file) * 0.1)]

    # Update the data accordingly
    features_cv = features_file[int(len(features_file) * 0.1):]
    targets_cv = targets_file[int(len(targets_file) * 0.1):]

    # First column will be the Test Data, with 10 rows, s.t. Test set is 1/10 of the original file
    # Second column will be the Training Data, with 10 rows, s.t. Training set is 9/10 of the original file
    cv_features_testing = np.empty((int(len(features_cv) * 0.1), 10), float)
    cv_features_training = np.empty(63630, float)

    cv_targets_testing = np.empty(int(len(targets_cv) * 0.1), float)
    cv_targets_training = np.empty(6363, float)

    # Make the cross validation file
    for i in range(10):
        test_len_features = int(len(features_cv) * 0.1)
        test_len_targets = int(len(targets_cv) * 0.1)

        features_testing, features_training = \
            features_cv[(i * test_len_features):((i * test_len_features) + test_len_features)], \
            np.append(features_cv[0:(i * test_len_features)],
                      features_cv[((i * test_len_features) + test_len_features): features_cv.size])

        targets_testing, targets_training = \
            targets_cv[(i * test_len_targets):((i * test_len_targets) + test_len_targets)], \
            np.append(targets_cv[0:(i * test_len_targets)],
                      targets_cv[((i * test_len_targets) + test_len_targets): targets_cv.size])

        np.append(cv_features_testing, features_testing, axis=0)
        np.append(cv_features_training, features_training, axis=0)
    
        np.append(cv_targets_testing, targets_testing, axis=0)
        np.append(cv_targets_training, targets_training, axis=0)


    neurons = 30

    perceptron = p.Perceptron(cv_features_training[0], cv_targets_training[0], neurons)
    perceptron.train()
    perceptron.test(features_testing, targets_testing)


if __name__ == "__main__":
    main()
