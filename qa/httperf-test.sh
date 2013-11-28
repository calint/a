httperf --version
httperf --port=8888 --uri=/qa.t010 --num-calls=10000 --add-header "Connection: Keep-Alive\nCookie: i=aaaa-131127-094424.37-4bcf3b8c\n"
httperf --port=8888 --uri=/qa.t010 --num-calls=10 --num-conns=10000 --add-header "Connection: Keep-Alive\nCookie: i=aaaa-131127-094424.37-4bcf3b8c\n"
httperf --port=8888 --uri=/qa.t010 --num-calls=10 --num-conns=10000 --add-header "Connection: Keep-Alive\n" --session-cookie --wsess=1000,100,0

httperf --port=8888 --uri=/qa.t010 --session-cookies --wsess=1000,100,0 --add-header='Connection: Keep-Alive\n'


#sudo ngrep -d lo -qW byline port 8888
#sudo tcpdump -i lo
#sudo tcpdump port 80 -i lo -A