# Set one part aside for parameter tuning
def get_validation(file):
    return file[0:int(len(file) * 0.1)]


# Create the validation set
def create_validation(file):
    return file[int(len(file) * 0.1):]


# Create the test set
def create_test(file, l, i):
    return file[(i * l):((i * l) + l)]


# Create training set
def create_training(file, l, i):
    return file[((i * l) + l): file.size]


# Get length test set
def get_length(file):
    return int(len(file) * 0.1)
