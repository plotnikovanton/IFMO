#!/bin/gnuplot

set terminal postscript eps enhanced color 
set encoding koi8r
set output 'plot.eps'

set style line 1 lt 2 pt 6 
set style line 2 lt 3 pt 6

plot    'data.in' u 1:2 w linesp ls 1 title 'Y | X',\
        'data.in' u 3:4 w linesp ls 2 title 'X | Y'
