p=8888

date&&
httperf --port=$p --version&&
httperf --port=$p --uri=/ --session-cookies --wsess=10000,3,0&&
httperf --port=$p --uri=/qa.t010 --session-cookies --wsess=100000,3,0&&
httperf --port=$p --uri=/qa/t001.txt --session-cookies --wsess=1000,1,0&&
date&&
exit
