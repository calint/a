#!/bin/sh
#port=8888
port=80
#server=localhost
server=www.ramvark.net
host=$server:$port
sessionid=aaaa-110307-064915.110-cb48e04f
cookie="i=$sessionid"
uploadfile="logo.jpg"
uploadfile_verysmall="verysmall.txt"
uploaddir="upload dir"
classpath="/Users/calin/Documents/workspace/a/bin"

echo $0
echo
echo `date`
echo $host
echo $sessionid
echo $classpath
echo

echo t000: file transfer from cache &&
curl -s http://$host/qa/t000.html > file &&
curl -s http://$host/qa/t000.html > file &&
diff -q file t000.html &&
rm file &&

echo t001: file transfer bigger  &&
curl -s http://$host/qa/t001.txt > file &&
diff -q file t001.txt &&
rm file &&

echo t002: file transfer byterange from 1  &&
curl -s -r 1- http://$host/qa/t001.txt > file &&
diff -q file t002.cmp &&
rm file &&

echo t010: page empty &&
curl -s http://$host/qa/t010 > file &&
diff -q file t010.cmp &&
rm file &&

echo t013: page large &&
curl -s http://$host/qa/t013 > file &&
diff -q file t013.cmp &&
rm file &&

echo t014: page input utf8 &&
curl -s -b $cookie http://$host/qa/t014?rst > file &&
curl -s -b $cookie http://$host/qa/t014 > file &&
diff -q file t014.cmp &&
rm file &&

echo t015: page post utf8 &&
curl -s -b $cookie --header "Content-Type: text/plain; charset=utf-8" --data-binary @t015.dat http://$host/qa/t014 > file &&
curl -s -b $cookie http://$host/u/$sessionid/test.txt > file
diff -q file t015.cmp &&
rm file &&
#diff -q ../u/$sessionid/test.txt t015.cmp &&
#rm -f ../u/$sessionid/test.txt &&

echo t016: page post utf8 read &&
curl -s -b $cookie http://$host/qa/t014 > file &&
diff -q file t016.cmp &&
rm file &&

#echo t020: upload file applet &&
#curl -s -b $cookie http://$host/upload > file &&
#diff -q file t020.cmp &&
#rm file &&

echo t021: resource from stream&&
curl -s http://$host/x.css>file&&
diff -q file t021.cmp&&
rm file&&

echo t022: big chunks reply&&
curl -s http://$host/qa.t022>file&&
diff -q file t022.cmp&&
rm file&&

echo t023: cached uri&&
curl -s -b $cookie http://$host/qa.t023>file&&
diff -q file t023.cmp&&
curl -s -b $cookie http://$host/qa.t023>file&&
diff -q file t023.cmp&&
rm file&&

echo t024: uploads&&
echo . . . . . . . file&&
java -cp $classpath a.qa.uploader $server $port $sessionid upload "$uploadfile" q &&
sleep 1 &&
curl -s -b $cookie http://$host/u/$sessionid/upload/$uploadfile > file &&
diff -q $uploadfile file &&
rm file &&
#diff -q ../u/$sessionid/upload/$uploadfile $uploadfile&&
#bck=`pwd`&&
#fl=$bck/file1&&
#cd ../u/$sessionid/upload&&
#ls -l $uploadfile>$fl&&
#cd $bck&&
#ls -l $uploadfile>file2&&
#diff -q file1 file2&&
#rm file1 file2&&

#echo ....: upload dir&&
#mkdir "$uploaddir"&&
#echo . . . . . . . dir&&
#java -cp $classpath a.qa.uploader $server $port $sessionid upload "$uploaddir" q &&
#sleep 1 &&
#basename "`stat -c %n "$uploaddir"`">file1&&
#stat -c %Y "$uploaddir">>file1&&
#basename "`stat -c %n "../u/$sessionid/upload/$uploaddir"`">file2&&
#stat -c %Y "../u/$sessionid/upload/$uploaddir">>file2&&
#diff -q file1 file2&&
#rm file1 file2&&
#rmdir "$uploaddir"&&

#find . -maxdepth 1 -type f -exec ls -l {} \;
#find . | while read file; do ls -l $file;done

echo . . . . . . . very small file&&
java -cp $classpath a.qa.uploader $server $port $sessionid "" "$uploadfile_verysmall" q &&
sleep 1 &&
curl -s -b $cookie http://$host/u/$sessionid/"$uploadfile_verysmall" > file &&
diff -q file "$uploadfile_verysmall" &&
#diff -q ../u/$sessionid/"$uploadfile_verysmall" "$uploadfile_verysmall"&&


#echo t025: websock init&&
#cat t025.req|nc $server $port|cat>file&&
#diff -q file t025.cmp&&
#rm file&&

echo t026: encoders osltgt,osnl&&
curl -s -b $cookie http://$host/qa.t026>file&&
diff -q file t026.cmp&&
rm file&&

echo t027: encoder osjsstr&&
curl -s -b $cookie http://$host/qa.t027>file&&
diff -q file t027.cmp&&
rm file&&

echo t029: http 304&&
cat t029.req|nc $server $port|cat>file&&
diff -q file t029.cmp&&
rm file&&

echo t031: chained request&&
cat t031.req|nc $server $port>file&&
diff -q file t031.cmp&&
rm file&&

echo t032: http 404&&
curl -s --head -b $cookie http://$host/nofile.404err>file&&
diff -q file t032.cmp&&
rm file&&

echo t100: path coverage&&
curl -s http://$host/qa/t100>file&&
diff -q file t100.cmp&&
rm file&&

#echo t101: thdwatch coverage&&
#curl -s http://$host/qa/t101>file&&
#diff -q file t101.cmp&&
#rm file&&

echo t102: session coverage&&
curl -s -b $cookie http://$host/qa/t102>file&&
diff -q file t102.cmp&&
rm file&&


echo TODO t1xx: websock coverage&&
echo TODO t1xx: chunked reply stalled by slow client&&
echo TODO t1xx: throttling&&

#echo cleanup&&
rm -rf ../u/$sessionid&&
echo `date`&&
echo done

