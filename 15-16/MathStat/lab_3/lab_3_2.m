clc
clear
n = 10 ^ 4;
a = 4;
b = 8;
gamma = 0.95;
x = sort(a + (b-a) * rand(n, 1));
m = ceil(n ^ (1 / 3));
ma = min(x)
mb = max(x)
step = (mb - ma) / m;

X = min(x) : step : max(x) - step;
Y = hist(x, m) / (n * step);

figure(1)
bar(X, Y);

[xb1, yb1] = stairs(X, Y);
c = min(x): 0.1 : max(x);
f = unifpdf(c, a, b);

figure(2)
plot(xb1, yb1, c, f), grid

X = hist(x, m) / n;
Y = min(x) : step : max(x);
for i = 1 : m
	p(i) = unifcdf(Y(i + 1), ma, mb) - unifcdf(Y(i), ma, mb);

endfor

hi = sum(((X - p).^2) * n ./ p)
T = chi2inv(gamma, m - 1)

if(hi < T)
	result = ['H0']
else
	result = ['H1']
endif

pause()
