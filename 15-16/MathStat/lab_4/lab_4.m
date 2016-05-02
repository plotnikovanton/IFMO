clc
clear

function solve(n, m)
    a = 5;
    s = 2;
    u = 4;
    d = 2 * s;

    xNorm = a + s * randn(n, m);
    xUnif = a - d / 2 + d * rand(n, m);
    xExp1 = -log(rand(n, m)) * u;
    xExp2 = -log(rand(n, m)) * u;
    xLaplace = xExp1 - xExp2 + a;

    stdXnNorm = std(mean(xNorm));
    stdXnUnif = std(mean(xUnif));
    stdXnLaplace = std(mean(xLaplace));
    stdMedNorm = std(median(xNorm));
    stdMedUnif = std(median(xUnif));
    stdMedLaplace = std(median(xLaplace));
    stdMinmaxNorm = std((min(xNorm) + max(xNorm)) / 2);
    stdMinmaxUnif = std((min(xUnif) + max(xUnif)) / 2);
    stdMinmaxLaplace = std((min(xLaplace) + max(xLaplace)) / 2);

    XnNorm = mean(mean(xNorm));
    thXnNorm = mean(std(xNorm)) / sqrt(n);
    XnUnif = mean(mean(xUnif));
    thXnUnif = d / sqrt(12*n);
    XnLaplace = mean(mean(xLaplace));
    thXnLaplace = u * sqrt(2) / sqrt(n);

    medNorm = mean(median(xNorm));
    thMedNorm = mean(std(xNorm)) * sqrt(pi) / sqrt(2*n);
    medUnif = mean(median(xUnif));
    thMedUnif = d / sqrt(4*n);
    medLaplace = mean(median(xLaplace));
    thMedLaplace = u / sqrt(n);

    minmaxNorm = mean((min(xNorm) + max(xNorm)) / 2);
    thMinmaxNorm = mean(std(xNorm)) * sqrt(0.4) / sqrt(log(n));
    minmaxUnif = mean((min(xUnif) + max(xUnif)) / 2);
    thMinmaxUnif = d / n / sqrt(2);
    minmaxLaplace = mean((min(xLaplace) + max(xLaplace)) / 2);
    thMinmaxLaplace = u / sqrt(0.9);

    printf("\n-- Estimated parameter a --\
            \n\tNorm\t\tLaplace\t\tUnif\
            \nmean\t%f\t%f\t%f\nmedian\t%f\t%f\t%f\nminmax\t%f\t%f\t%f\t\n"
          , XnNorm, XnLaplace, XnUnif
          , medNorm, medLaplace, medUnif
          , minmaxNorm, minmaxUnif, minmaxLaplace)

    printf("\n-- Squared risks(estimated) --\
            \n\tNorm\t\tLaplace\t\tUnif\
            \nmean\t%f\t%f\t%f\nmedian\t%f\t%f\t%f\nminmax\t%f\t%f\t%f\t\n"
          , stdXnNorm, stdXnLaplace, stdXnUnif
          , stdMedNorm, stdMedLaplace, stdMedUnif
          , stdMinmaxNorm, stdMinmaxUnif, stdMinmaxLaplace)

    printf("\n-- Squared risks(theoretical) --\
            \n\tNorm\t\tLaplace\t\tUnif\
            \nmean\t%f\t%f\t%f\nmedian\t%f\t%f\t%f\nminmax\t%f\t%f\t%f\t\n"
          , thXnNorm, thXnLaplace, thXnUnif
          , thMedNorm, thMedLaplace, thMedUnif
          , thMinmaxNorm, thMinmaxUnif, thMinmaxLaplace)

endfunction

printf("using 100 size\n")
solve(10^2, 10^2)
printf("\n\n\nusing 10000 size\n")
solve(10^4, 10^2)
