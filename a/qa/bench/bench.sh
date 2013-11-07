#!/bin/bash
hosts="192.168.2.101:8082 192.168.2.101"
paths="/"
nclients="1 100 200 300 400 500 600 700 800 900 1000 1500 2000 2500 3000 3500 4000 4500 5000 5500 6000 6500 7000 7500 8000 8500 9000 9500 10000"
log="bench.log"
echo>$log
t=10
for h in $hosts;do
	for p in $paths;do 
		for c in $nclients;do
			echo ab -c$c -t$t http://$h$p  
			ab -c$c -t$t http://$h$p>>$log
		done
	done
done
