import perceptron as p
import numpy as np


def main():
    p1 = p.Perceptron(num_inputs=4, bias=-1.5, )
    input_data = np.array([(0, 0), (0, 1), (1, 0), (1, 1)])

    # print(p1.activation_func(input_data))


if __name__ == "__main__":
    main()
