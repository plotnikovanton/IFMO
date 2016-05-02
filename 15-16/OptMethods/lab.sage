x, y = var('x y')
f = 2*x^2 + 3*x^4 + 4*x*y^2 + 12*x^2*y^3 + y^4 + 6*y^6
g1 = 3 - 6 * x^2 - 10 * x^6 - 28*x^3*y - 7*y^2 + 24*x*y^2 - 9*y^4
g2 = 2 - 5*x^4 - 7*x^6 + 28*x^3*y^2 - 20*x^2*y^3 - 16*y^4 - 10*y^6
g3 = 2 - 7*x^2 - 3*x^4 - 12*x^2*y^2 - 12*x^2*y^3- 4*y^4 - 3*y^6
dfdx = diff(f, x)
dfdy = diff(f, y)
d2fdx2 = diff(f, x, 2)
d2fdy2 = diff(f, y, 2)
d2fdxdy = diff(dfdx, y)

sol = solve([dfdx == 0, dfdy == 0, g1 >= 0, g2 >= 0, g3 >= 0], x,
        y, solution_dict=False)

eq = dfdx == 0 and dfdy == 0 and g1 >= 0 and g2 >= 0 and g3 >= 0

