import numpy as np


class IsingModel:
    """ Initialize 3 dimension model with x, y, z size """
    def __init__(self, x, y, z):
        self.grid = np.ones((x, y, z), dtype=bool)


if __name__ == '__main__':
    pass
