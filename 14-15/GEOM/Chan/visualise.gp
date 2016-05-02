#!/bin/gnuplot
set terminal postscript eps enhanced color font 'Helvetica,10'
set output "out.eps"

set nokey

set style line 1 lc rgb 'green' pt 6
set style line 2 lw 3 lc rgb 'blue' lt 1 pt 7

plot "data.in" using 1:2 with points ls 1,\
"<cat data.out & head -n 1 data.out" using 1:2 with linespoints ls 2
