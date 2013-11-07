set title "static content (small file)"
set terminal postscript enhanced color solid lw 2 "monospace" 8
set output "bench.ps"
set xlabel "connections"
set ylabel "requests/s"
plot "bench-nona.plot" using 2:3 title "noname","bench-Resi.plot" using 2:3 title "resin4","bench-Glas.plot" using 2:3 title "glassfish3","bench-ligh.plot" using 2:3 title "lighttp","bench-Orio.plot" using 2:3 title "orion2","bench-Apac.plot" using 2:3 title "apache2"
