#!/bin/bash
java bench>all.plot
cat all.plot|awk '{print $1}'|sort -u>servers.ls
echo -n "plot ">plotscr.gp
for s in `cat servers.ls`;do
	fd=bench-${s:0:4}.plot
	echo $fd
	echo -n \"$fd\" using 2:3,>>plotscr.gp
	cat all.plot|grep $s>$fd
	sleep 1
done
