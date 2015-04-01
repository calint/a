set title "static content with keep-alive (small file from cache)"
set terminal postscript enhanced color solid lw 2 "monospace" 8
set output "bench.ps"
set xlabel "clients"
set ylabel "requests/s"
#plot "bench-nona.plot" using 2:3 title "noname","bench-Resi.plot" using 2:3 title "resin","bench-ngin.plot" using 2:3 title "nginx"
