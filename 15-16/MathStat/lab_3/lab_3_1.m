clc
clear
n = 10 ^ 4;
a = 3;
s = 2;
gamma = 0.95;
x = sort(a + s * randn(n, 1));
mx = mean(x)
sigma = std(x)
m = ceil(n ^ (1 / 3));
step = (max(x) - min(x)) / m;

X = min(x) : step : max(x) - step;
Y = hist(x, m) / (n * step);

figure(1)
bar(X, Y);

[xb1, yb1] = stairs(X, Y);
c = min(x): 0.1 : max(x);
f = normpdf(c, a, s);

figure(2)
plot(xb1, yb1, c, f), grid

X = hist(x, m) / n;
Y = min(x) : step : max(x);
for i = 1 : m
	p(i) = normcdf(Y(i + 1), mx, sigma) - normcdf(Y(i), mx, sigma);

endfor

hi = sum(((X - p).^2) * n ./ p)
T = chi2inv(gamma, m - 1)

if(hi < T)
	result = ['H0']
else
	result = ['H1']
endif

pause()
