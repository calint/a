#!/bin/sh
ab=ab
host=localhost:8888
result_summary=ab-result-summary
result_file=ab-result
test_time_s=10
echo > $result_file

echo test transfer from cache
$ab -v 4 -c 1   -n 1        http://$host/              >> $result_file &&
$ab -v 4 -c 1   -n 1     -k http://$host/              >> $result_file &&
$ab -v 1 -c 1   -n 10000    http://$host/              >> $result_file &&
$ab -v 1 -c 1   -n 10000 -k http://$host/              >> $result_file &&
$ab -v 1 -c 100 -n 10000    http://$host/              >> $result_file &&
$ab -v 1 -c 100 -n 10000 -k http://$host/              >> $result_file &&
echo :: test file transfer
$ab -v 4 -c 1   -n 1        http://$host/qa/t001.txt >> $result_file &&
$ab -v 4 -c 1   -n 1     -k http://$host/qa/t001.txt >> $result_file &&
$ab -v 1 -c 1   -n 5000     http://$host/qa/t001.txt >> $result_file &&
$ab -v 1 -c 1   -n 5000  -k http://$host/qa/t001.txt >> $result_file &&
$ab -v 1 -c 100 -n 5000     http://$host/qa/t001.txt >> $result_file &&
$ab -v 1 -c 100 -n 5000  -k http://$host/qa/t001.txt >> $result_file &&
echo :: test page transfer
$ab -v 4 -c 1   -n 1        http://$host/qa/t010     >> $result_file &&
$ab -v 4 -c 1   -n 1     -k http://$host/qa/t013     >> $result_file &&
$ab -v 1 -c 100 -n 5000     http://$host/qa/t010     >> $result_file &&
$ab -v 1 -c 100 -n 5000     http://$host/qa/t013     >> $result_file &&
cat $result_file|grep Failed>$result_summary &&
cat $result_summary


