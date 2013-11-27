httperf --port=8888 --uri=/typealine --print-request --print-reply --num-calls=2 --num-conns=1 --add-header "Connection: Keep-Alive\nCookie: i=aaaa-131127-094424.837-4bcf3b8c\n"
httperf --port=8888 --uri=/qa.t010 --num-calls=10000 --num-conns=20000 --add-header "Connection: Keep-Alive\nCookie: i=aaaa-131127-094424.837-4bcf3b8c\n"

ab -k -c1 -n20000 -v4 http://localhost:8888/qa.t014
ab -k -c1 -n20000 -v4 -Ci=aaaa-131127-104112.687-b23ac992 http://localhost:8888/qa.t014
ab -k -c1 -n2 -v4 -Ci=aaaa-131127-104112.687-b23ac992 http://localhost:8888/qa.t014
