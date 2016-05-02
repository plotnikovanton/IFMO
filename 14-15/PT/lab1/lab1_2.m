clc
global gamma;
gamma = 0.95;

% Пункт а
function y = solve_a(n)
    global gamma
    a = 1;
    b = 3;
    l = b-a;
    X = rand(n,1) * (b - a) + a;
    Y = arrayfun(@(x) log(4-x)/(x+2), X);
    hist(Y)
    
    M = mean(Y);
    T = norminv((1+gamma)/2);
    S = std(Y);
    dM = T * S/sqrt(n)
    I = l * M
    dL = S * dM
    mean (dL)
    interval = [(I - dL), (I + dL)]
end

% Пункт б
function y = solve_b(n)
    global gamma;
    c = sqrt(2 * pi);
    a = -1;
    s = 1;
    N = randn(n,1);
    X = a + s * N;
    Y = arrayfun(@(x) sqrt(abs(x)), X);
    M = mean(Y);
    S = std(Y);
    T = norminv((1+gamma)/2)
    dM = T * S/sqrt(n);
    hist(Y)
    I = c * M
    dI = c * dM
    mean(dI)
    interval = [I-dI, I+dI]
end

