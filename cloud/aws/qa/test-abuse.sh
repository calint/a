#!/bin/bash

echo $0
echo `date`

echo txxx: ddos, request, method, size&&
echo 'DELETE / HTTP/1.1\n'|nc localhost 8888 >file&&diff -q file emptyfile&&rm file&&

echo txxx: ddos, request, uri, size&&
echo 'GET //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// HTTP/1.1\nHost: localhost\n'|nc localhost 8888>file&&diff file emptyfile&&rm file&&

echo txxx: ddos, request, protocol, size&&
echo 'GET / HTTTP/1.11\r\n\r'|nc localhost 8888>file&&diff file emptyfile&&rm file&&

echo txxx: ddos, request, header, name, size&&
echo 'GET / HTTP/1.1\r\nHost-Host-Host-Host-Host-Host-Host: localhost\r\n\r'|nc localhost 8888>file&&diff file emptyfile&&rm file&&

echo txxx: ddos, request, header, value, size&&
echo 'GET / HTTP/1.1\r\nHost: localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-localhost-\r\n\r'|nc localhost 8888>file&&diff file emptyfile&&rm file&&

echo txxx: ddos, request, header, count&&
echo 'GET / HTTP/1.1\nHost: localhost\na1:\na2:\na3:\na4:\na5:\na6:\na7:\na8:\na9:\na10:\na11:\na12:\na13:\na14:\na15:\na16:\n\r'|nc localhost 8888>file&&diff file emptyfile&&rm file&&

echo txxx: ddos, request, content, size&&
echo 'get / 1\nHost:localhost\nCookie:i=user2\nContent-Length: 17179869185\n\na'|nc localhost 8888>file&&diff file emptyfile&&rm file&&

echo txxx: hack, request path, access outside root&&
echo 'GET ../etc/passwd HTTP/1.0\n'|nc localhost 8888>file&&diff file emptyfile&&rm file&&
echo 'GET ../../etc/passwd HTTP/1.0\n'|nc localhost 8888>file&&diff file emptyfile&&rm file&&
echo 'GET ../../../etc/passwd HTTP/1.0\n'|nc localhost 8888>file&&diff file emptyfile&&rm file&&
echo 'GET ../../../../etc/passwd HTTP/1.0\n'|nc localhost 8888>file&&diff file emptyfile&&rm file&&
echo 'GET ../../../../../etc/passwd HTTP/1.0\n'|nc localhost 8888>file&&diff file emptyfile&&rm file&&

echo txxx: hack, upload path, access outside root&&
java -cp ../bin/ a.qa.uploader localhost 8888 calin ".." t001.txt 2>file 1>/dev/null&&rm file
head -n 1 file>file.1&&
echo "java.io.IOException: Broken pipe">file.2&&
diff file.1 file.2&&
rm file file.1 file.2&&

echo TODO Txxx: ddos, open connections with incomplete requests&&
echo TODO Txxx: ddos, thread pool uptake, client stalling server thread write&&
#echo TODO t303: hack, request path, access non web class&&
#echo TODO t304: hack, post to non public fields&&

echo TODO Txxx: upload long file 4G+, strains 32 bit based code&&
echo TODO Txxx: download long file 4G+, strains 32 bit based code&&














#echo TODO t243: ddos, request, content, upload, slow&&
echo TODO t305: hack, session hijack, different sources accessing same session&&

echo TODO t202: ddos, client, uptake, sessions&&
echo TODO t202: ddos, client, uptake, threads&&
echo TODO t203: ddos, client, uptake, storage&&
#echo TODO t204: ddos, client, uptake, file count&&
echo TODO t205: ddos, client, uptake, cpu&&
echo TODO t206: ddos, client, uptake, ram&&
echo TODO t207: ddos, client, uptake, sockets&&
echo TODO t208: ddos, client, uptake, requests, frequency&&
echo TODO t210: ddos, client, uptake, traffic, download&&
echo TODO t211: ddos, client, uptake, traffic, upload&&
echo TODO t211: ddos, client, websocks&&
echo TODO t211: ddos, client, websock, count&&
echo TODO t211: ddos, client, websock, packet, size&&
echo TODO t211: ddos, client, websock, traffic, download&&
echo TODO t211: ddos, client, websock, traffic, upload&&

echo TODO t251: ddos, sockets&&
echo TODO t251: ddos, socket, count&&
echo TODO t253: ddos, socket, packet, size&&
echo TODO t254: ddos, socket, packet, slow&&
echo TODO t256: ddos, socket, traffic, download&&
echo TODO t257: ddos, socket, traffic, upload&&

echo TODO t271: ddos, packets&&
echo TODO t272: ddos, packet, too many fragments&&
echo TODO t273: ddos, packet, too small fragments&&
echo TODO t274: ddos, packet, too large fragments&&
echo TODO t275: ddos, packet, too much traffic from same source&&
echo TODO t276: ddos, packet, too much traffic from same submask&&
echo `date`&&
echo done