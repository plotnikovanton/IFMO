clc
clear

gamma = 0.95;
n = 10 ^ 4;
m = 100;
result = 1 - gamma
# Uniform distribution

a = 2;
b = 8;
t = 4;

Fvalue_1 = unifcdf(t, a, b)

X_1 = rand(n, m) * (b - a) + a;
Fn_1 = mean(X_1 < t);


T_1 = norminv((1 + gamma) / 2);
dn_1 = T_1 * std(X_1 < t) / sqrt(n);


leftBound_1 = Fn_1 - dn_1;
rightBound_1 = Fn_1 + dn_1;


x = 1 : 100;
figure(1)


plot(x, leftBound_1, '*-', x, rightBound_1, '*-', x, Fvalue_1, 'r'), grid

sum = 0;
for i = 1 : 100

	X_1 = rand(n, m) * (b - a) + a;
	Fn_1 = mean(X_1 < t);


	T_1 = norminv((1 + gamma) / 2);
	dn_1 = T_1 * std(X_1 < t) / sqrt(n);

	leftBound_1 = Fn_1 - dn_1;
	rightBound_1 = Fn_1 + dn_1;

	counter(i) = 0;
	for k = 1 : 100
		if(Fvalue_1 < leftBound_1(k) || Fvalue_1 > rightBound_1(k))
			counter(i)++;
		endif
	endfor
endfor

result_1 = mean(counter) / 100

# Normal distribution

a = 5;
s = 3;
t = 4;

Fvalue_2 = normcdf(t, a, s)

X_2 = randn(n, m) * s + a;
Fn_2 = mean(X_2 < t);

T_2 = norminv((1 + gamma) / 2);
dn_2 = T_2 * std(X_2 < t) / sqrt(n);


leftBound_2 = Fn_2 - dn_2;
rightBound_2 = Fn_2 + dn_2;

z = 1 : 100;
figure(2)
plot(x, leftBound_2, '*-', x, rightBound_2, '*-', x, Fvalue_2, 'r'), grid

sum = 0;
for i = 1 : 100

	X_2 = randn(n, m) * s + a;
	Fn_2 = mean(X_2 < t);


	T_2 = norminv((1 + gamma) / 2);
	dn_2 = T_2 * std(X_2 < t) / sqrt(n);

	leftBound_2 = Fn_2 - dn_2;
	rightBound_2 = Fn_2 + dn_2;

	counter(i) = 0;
	for k = 1 : 100
		if(Fvalue_2 < leftBound_2(k) || Fvalue_2 > rightBound_2(k))
			counter(i)++;
		endif
	endfor
endfor

result_2 = mean(counter) / 100

pause()
