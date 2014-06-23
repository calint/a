#!/bin/bash
hosts="`cat conf_hosts`"
nclients="`cat conf_clients`"
paths=/qa/t001.txt
log=ab.log

echo $hosts
echo $nclients
t=5
rm $log *.plot
for h in $hosts;do
	for p in $paths;do
		for c in $nclients;do
		    cmd="ab -c$c -t$t http://$h$p"
			echo $cmd
			$cmd|tee -a $log|java bench|tee -a all.plot
		done
	done
done

cat all.plot|awk '{print $1}'|sort -u>servers.ls
for s in `cat servers.ls`;do
	fd=bench-${s:0:4}.plot
	echo $fd
	cat all.plot|grep $s>$fd
done

cp plots.gp __plots.gp
echo -n "plot ">>__plots.gp;
for f in `ls bench-*`;do
    echo -n \"$f\" using 2:3 title \"${f:6:4}\",>>__plots.gp
done

#mv -f bench.ps bench.ps.bak
gnuplot __plots.gp
rm __plots.gp

rm bench.pdf
ps2pdf bench.ps
convert bench.pdf bench.jpg

ls -la bench.ps
ls -la bench.pdf
ls -la bench.jpg

#gv bench.ps
