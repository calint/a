echo `date`: a.qa pics

session=aaaa-131119-095209.788-1aedd1a3
uri=/pics

httperf --port=8888 --uri=$uri --add-header="cookie:i=$session\n" --print-reply --print-request&&
for n in 10 100 1000 10000 100000;do
	httperf --port=8888 --uri=$uri --add-header="cookie:i=$session\n" --num-conns=$n --num-calls=1;
done

echo "`date`: done"