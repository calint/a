echo -n start time:\ ;date +%Y-%m-%d---%H:%M:%S-
echo url: $3
echo duration: $2
echo clients: $1
. run.sh "ab2 -c $1 -t $2 $3"
#ls *.log|while read ln;do echo •• $ln;cat $ln;done
cat *.log|grep Failed
cat *.log|grep Complete
cat *.log|grep "Total transferred"
cat *.log|grep "Transfer rate"
cat *.log|grep "Time taken for tests"
cat *.log|grep "Requests per second"
echo -n end time:\ ;date +%Y-%m-%d---%H:%M:%S-
