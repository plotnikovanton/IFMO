clc
global gamma
gamma = 0.98;

global k c a;
k = 5;
c = 0.94;
a = 3;

function evaluate(n)
    global a k gamma c;
    X=rand(n,k);
    F=sum(exp(-a * X)');
    
    hist(F)
    v = mean(F<c)

    T = norminv((1+gamma)/2)
    d = T*std(F<c)/sqrt(n)
    interval = [v-d, v+d]
end
