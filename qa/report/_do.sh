#!/bin/sh
sh ../sysinfo.sh>sysinfo.out
cd ../bench1&&. _do.sh
cd ../bench2&&. _do.sh
cd ../bench3&&. _do.sh
cd ../bench4&&. _do.sh
cd ../bench5&&. _do.sh
cd ../report
cp -a ../bench1/bench.jpg bench1.jpg
cp -a ../bench2/bench.jpg bench2.jpg
cp -a ../bench3/bench.jpg bench3.jpg
cp -a ../bench4/bench.jpg bench4.jpg
cp -a ../bench5/bench.jpg bench5.jpg
