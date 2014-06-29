#!/bin/sh
p=8888

date&&
httperf --port=$p --version&&
httperf --hog --rate=1000 --port=$p --session-cookies --wsess=2000,100,1  --uri=/qa.t010&&
httperf --hog --rate=1000 --port=$p --session-cookies --wsess=1000,1,0    --uri=/qa/t001.txt&&
httperf --hog --rate=1000 --port=$p --session-cookies --wsess=1000,3,0    --uri=/&&
date&&
exit
