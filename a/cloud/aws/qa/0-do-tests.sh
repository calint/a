#!/bin/sh
echo `date`&&
sh sysinfo.sh|tee sysinfo.out&&
sh test-coverage.sh|tee test-coverage.out&&
sh test-perf-ab.sh&&
sh test-perf-httperf.sh|tee test-perf-httperf.out&&
sh test-perf-siege.sh|tee test-perf-siege.out&&
sh test-abuse.sh|tee test-abuse.out&&
echo `date`&&
echo done


