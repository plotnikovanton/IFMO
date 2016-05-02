clc
clear

min = -2.2;
max = 2.5;
n = 69;
a1 = 1.7;
a2 = -2.4;
a3 = -3.6;
c1 = 3.5;
c2 = -4.2;
s = 1.5;

x = (min : (max - min) / (n - 1) : max)';
Px = c1 + c2 * x;
z = s * randn(n, 1);
Yx = Px + z;
an1 = polyfit(x, Yx, 1)
Fx = polyval(an1, x);

# table = [x Yx Fx Yx - Fx]

O2 = cov(x, Yx) / std(x) ^ 2
O1 = mean(Yx) - O2 * mean(x)
P1 = O1 + O2 * x;

figure(1)
plot(x, Px, 'b', x, Yx, '*', x, Fx, 'r'), grid

figure(2)
plot(x, Px, 'b', x, Yx, '*', x, P1, 'g'), grid


Px2 = a1 + a2 * x + a3 * x.^2;
Yx2 = Px2 + z;
an2 = polyfit(x, Yx2, 2)
Fx2 = polyval(an2, x);

figure(3)
plot(x, Px2, 'b', x, Yx2, '*', x, Fx2, 'r'), grid

ort = dot(Fx - Yx, Fx)
std(z)

pause()
