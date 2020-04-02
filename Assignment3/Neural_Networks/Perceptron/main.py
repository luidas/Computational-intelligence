import perceptron as p
import numpy as np

def main():
    features_file = np.genfromtxt('../features.txt', delimiter=',')
    targets_file = np.genfromtxt('../targets.txt', delimiter='')


    # however this is what Liudas first initiated, so something like 75 - 15 - 10?
    # but we have to write this in the report - in the code we are using the ..._testing as validation
    # these comments needed to be deleted afterwards
    features_training, features_validation, features_testing = \
        features_file[:5890], features_file[5890:7068], features_file[7068:]
    targets_training, targets_validation, targets_testing = \
        targets_file[:5890], targets_file[5890:7068], targets_file[7068:]

    # NEEDED FOR K_FOLD CROSS VALIDATION
    # param_features = get_validation(features_file)
    # param_targets = get_validation(targets_file)
    #
    # cv_features = create_validation(features_file)
    # cv_targets = create_validation(targets_file)
    #
    # for i in range(1):
    #     test_set_length = get_length(cv_targets)
    #
    #     features_testing = create_test(file=cv_features, l=test_set_length, i=i)
    #     targets_testing = create_test(file=cv_targets, l=test_set_length, i=i)
    #
    #     features_training = create_training(file=cv_features, l=test_set_length, i=i)
    #     targets_training = create_training(file=cv_targets, l=test_set_length, i=i)
    #
    #     neurons = 30
    #
    #     perceptron = p.Perceptron(features_training=features_training, targets_training=targets_training, neurons=neurons)
    #     perceptron.train()
    #     perceptron.test(features_test=features_testing, targets_test=targets_testing)


if __name__ == "__main__":
    main()
