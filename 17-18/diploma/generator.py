import random
from sympy import *
from sympy.abc import x


class FourF:
    def __init__(self, seed):
        self.__rnd = random.Random(seed)
        def f_gen(a, b, c, d):
            def f(x_val):
                y = a*x**4 + b*x**3 + c*x**2 + d*x
                return y, FourF.get_data(y, x_val)
            return f
        self.__f_gen = f_gen


    @staticmethod
    def get_data(y, x_val):
        prime_one = y.diff(x)
        prime_two = prime_one.diff(x)
        return list(map(lambda e: float(e), [
                x_val,
                y.subs(x, x_val).evalf(),
                prime_one.subs(x, x_val).evalf(),
                prime_two.subs(x, x_val).evalf()
            ]))


    def __iter__(self):
        while True:
            a = self.__rnd.uniform(0, 10)
            b = self.__rnd.uniform(-10, 10)
            c = self.__rnd.uniform(-10, 10)
            d = self.__rnd.uniform(-10, 10)

            x_val = self.__rnd.uniform(-10, 10)
            yield(self.__f_gen(a, b, c, d)(x_val))
