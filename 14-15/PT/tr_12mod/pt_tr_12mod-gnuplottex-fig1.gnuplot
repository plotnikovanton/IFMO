set terminal latex
set output 'pt_tr_12mod-gnuplottex-fig1.tex'
set terminal epslatex color size 15cm,10cm

            set grid ytics lc rgb '#555555' lw 1 lt 0
            set grid xtics lc rgb '#555555' lw 1 lt 0

            set xrange [-2:2]

            plot (x+1) title 'Регрессия Y на X'\
            , ((x+5)/4) title 'Регрессия X на Y'\
            , "<echo '-1 1\n 1 2\n 1 1\n -1 1'" u 1:2 w linesp lc rgb "black" notitle # AC
    
