set title "static content (small file from cache)"
set terminal postscript enhanced color solid lw 2 "monospace" 8
set output "bench.ps"
set xlabel "clients"
set ylabel "requests/s"
#unset autoscale
#plot "bench-nona.plot" using 2:3 title "noname","bench-Resi.plot" using 2:3 title "resin","bench-ngin.plot" using 2:3 title "nginx"
