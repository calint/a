#!/bin/sh
host=localhost:8888
sessionid=aaaa-110307-064915.110-cb48e04f
cookie="i=$sessionid"
uploadfile="logo.jpg"

echo `date` &&
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
diff -q ../u/$sessionid/test.txt t015.cmp &&
rm -f ../u/$sessionid/test.txt &&

echo t016: page post ut8 read &&
curl -s -b $cookie http://$host/qa/t014 > file &&
diff -q file t016.cmp &&
rm file &&

echo t020: upload file applet &&
curl -s -b $cookie http://$host/upload?$content > file &&
diff -q file t020.cmp &&
rm file &&

echo t021: upload file &&
java -cp ../../auplo/bin/ applet.uploader localhost 8888 $sessionid upload $uploadfile q&& 
#curl -s -b $cookie --header "Content-Type: $content" --data-binary @$uploadfile http://$host/upload
diff -q ../u/$sessionid/upload/$uploadfile $uploadfile&&
bck=`pwd`&&
fl=$bck/file1&&
cd ../u/$sessionid/upload&&
ls -l $uploadfile>$fl&&
cd $bck&&
ls -l $uploadfile>file2&&
diff -q file1 file2&&


echo t021: resource from jar&&
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


echo t023: create dir todo&&
echo t025: sokio todo&&
echo t026: chained small uploads todo&&


echo cleanup&&
rm file1 file2&&
rm -rf ../u/$sessionid &&

echo ok

