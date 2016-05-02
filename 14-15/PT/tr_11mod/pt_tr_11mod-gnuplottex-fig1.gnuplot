set terminal latex
set output 'pt_tr_11mod-gnuplottex-fig1.tex'
set terminal epslatex color size 15cm,10cm

            set grid ytics lc rgb '#555555' lw 1 lt 0
            set grid xtics lc rgb '#555555' lw 1 lt 0

            set style line 3 lc rgb 'black' pt 7 ps 2

            plot (0.404 + 0.162 * x) title 'Регрессия Y на X'\
            , ((x+0.25)/0.167) title 'Регрессия X на Y'\
            , "<echo '-2 -1'" w p ls 3 notitle \
            , "<echo '-2 0'" w p ls 3 notitle \
            , "<echo '-2 2'" w p ls 3 notitle \
            , "<echo '-1 -1'" w p ls 3 notitle \
            , "<echo '-1 0'" w p ls 3 notitle \
            , "<echo '-1 2'" w p ls 3 notitle \
            , "<echo '1 -1'" w p ls 3 notitle \
            , "<echo '1 0'" w p ls 3 notitle \
            , "<echo '1 2'" w p ls 3 notitle \
    
