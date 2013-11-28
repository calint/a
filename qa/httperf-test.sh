httperf --version
httperf --port=8888 --uri=/qa.t010 --num-calls=10000 --add-header "Connection: Keep-Alive\nCookie: i=aaaa-131127-094424.37-4bcf3b8c\n"
httperf --port=8888 --uri=/qa.t010 --num-calls=10 --num-conns=10000 --add-header "Connection: Keep-Alive\nCookie: i=aaaa-131127-094424.37-4bcf3b8c\n"
httperf --port=8888 --uri=/qa.t010 --num-calls=10 --num-conns=10000 --add-header "Connection: Keep-Alive\n" --session-cookie --wsess=1000,100,0

httperf --port=8888 --uri=/qa.t010 --session-cookies --wsess=1000,100,0 --add-header='Connection: Keep-Alive\n'


#lsb_release -a
#sudo ngrep -d lo -qW byline port 8888
#sudo tcpdump -i lo
#sudo tcpdump port 80 -i lo -A
#netstat -l
#netstat -vlepcao


calin@vaio:~/workspace/a$ nc localhost 8888
get / .

HTTP/1.1 200
Content-Length: 11
Last-Modified: Sun, 17 Nov 2013 07:39:12 GMT
Connection: Keep-Alive

hello worldcalin@vaio:~/workspace/a$ 



calin@vaio:~/workspace/a$ nc -l 8888
GET / HTTP/1.1
Host: localhost:8888
User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:25.0) Gecko/20100101 Firefox/25.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate
Cookie: i=aaaa-130310-043332.121-db267dd5
Connection: keep-alive
If-Modified-Since: Sun, 17 Nov 2013 07:39:12 GMT
Cache-Control: max-age=0


calin@vaio:~/workspace/a$ echo "hello" |nc localhost 8888
calin@vaio:~/workspace/a$










