clc
clear

N = 10;
p = 0.45;
q = 1 - p;
m = 100;

d = zeros(1, N);
dp = zeros(1, N - 1);
dq = zeros(1, N - 1);
d(1, 1) = 1;
d(1, N) = 1;
dp(2 : N - 1) = p;
dq(1 : N - 2) = q;
P = diag(d) + diag(dp, 1) + diag(dq, -1);

p0 = zeros(1, N);
x = fix(N * rand()) + 1
p0(x) = 1;

pm = p0 * P ^ m
px0 = ((q / p) ^ (x-1) - (q / p) ^ (N - 1)) / (1 - (q / p) ^ (N - 1))
pxN = (1 - (q / p) ^ (x - 1)) / (1 - (q / p) ^ (N - 1))

s(1) = x;
for i = 2 : m
    u = rand();
    if (s(i - 1) == N || s(i - 1) == 0) s(i) = s(i - 1);
        elseif (u < q) s(i) = s(i - 1) - 1;
        else s(i) = s(i - 1) + 1;
    endif
endfor
plot(s, 'r--*'), grid
axis([0, m, 0, N + 1])

pause()
