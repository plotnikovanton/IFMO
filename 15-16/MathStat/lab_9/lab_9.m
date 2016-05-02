clc
clear

N = 10;
p = 0.6;
q = 1 - p;
m = 200;

d = zeros(1, N);
dp = zeros(1, N - 1);
dq = zeros(1, N - 1);
d(1, 1) = q;
d(1, N) = p;
dp(1 : N - 1) = p;
dq(1 : N - 1) = q;
P = diag(d) + diag(dp, 1) + diag(dq, -1);

p0 = zeros(1, N);
x = fix(N * rand()) + 1;
p0(1, x) = 1;

pm = p0 * P ^ m
px(1) = (1 - (p / q) ) / (1 - (p / q) ^ N);
for i = 2 : N
    px(i) = px(1) * (p / q) ^ (i - 1);
endfor
px
s(1) = x;
for i = 2 : m
    u = rand();
    if (u < q) s(i) = max(s(i - 1) - 1, 0);
    else s(i) = min(s(i - 1) + 1, N);
    endif
endfor
plot(s, 'r- .'), grid
axis([0, m, 0, N + 1])

pause()
