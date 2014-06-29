#!/bin/sh
ab=ab
host=localhost:8888
result_summary=test-perf-ab.out.summary
result_file=test-perf-ab.out
test_time_s=10

echo `date`>$result_file
echo>>$result_file
echo :: test page transfer
$ab -v4 -c1     -n1        http://$host/qa.t010     >> $result_file &&
$ab -v1 -c100   -n100      http://$host/qa.t010     >> $result_file &&
$ab -v1 -c1000  -n1000     http://$host/qa.t010     >> $result_file &&
$ab -v1 -c10000 -n10000    http://$host/qa.t010     >> $result_file &&
$ab -v1 -c20000 -n20000    http://$host/qa.t010     >> $result_file &&
echo>>$result_file
echo :: test page transfer with keepalive
$ab -v4 -c1     -n1     -k http://$host/qa.t010     >> $result_file &&
#$ab -v1 -c100   -n100   -k http://$host/qa.t010     >> $result_file &&
#$ab -v1 -c1000  -n1000  -k http://$host/qa.t010     >> $result_file &&
#$ab -v1 -c10000 -n10000 -k http://$host/qa.t010     >> $result_file &&
#$ab -v1 -c20000 -n20000 -k http://$host/qa.t010     >> $result_file &&
echo>>$result_file
echo test transfer from cache
$ab -v4 -c1     -n1        http://$host/              >> $result_file &&
$ab -v1 -c10    -n20000    http://$host/              >> $result_file &&
$ab -v1 -c100   -n20000    http://$host/              >> $result_file &&
$ab -v1 -c1000  -n20000    http://$host/              >> $result_file &&
$ab -v1 -c10000 -n20000    http://$host/              >> $result_file &&
$ab -v1 -c20000 -n20000    http://$host/              >> $result_file &&
echo>>$result_file
echo test transfer from cache with keepalive
$ab -v4 -c1     -n1     -k http://$host/              >> $result_file &&
$ab -v1 -c10    -n20000 -k http://$host/              >> $result_file &&
$ab -v1 -c100   -n20000 -k http://$host/              >> $result_file &&
$ab -v1 -c1000  -n20000 -k http://$host/              >> $result_file &&
$ab -v1 -c10000 -n20000 -k http://$host/              >> $result_file &&
#$ab -v1 -c20000 -n20000 -k http://$host/              >> $result_file &&
echo>>$result_file
echo :: test bigger file transfer
$ab -v4 -c1     -n1        http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c10    -n1000     http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c100   -n1000     http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c1000  -n1000     http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c1000  -n10000    http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c20000 -n20000    http://$host/qa/t001.txt >> $result_file &&
echo>>$result_file
echo :: test bigger file transfer with keepalive
$ab -v4 -c1     -n1     -k http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c10    -n1000  -k http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c100   -n1000  -k http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c1000  -n1000  -k http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c10000 -n10000 -k http://$host/qa/t001.txt >> $result_file &&
$ab -v1 -c20000 -n20000 -k http://$host/qa/t001.txt >> $result_file &&
echo>>$result_file

echo `date`>>$result_file&&

cat $result_file|grep Failed>$result_summary&&
cat $result_summary&&
rm $result_summary
echo done
