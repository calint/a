echo `date`: qa pics

session=aaaa-131119-095209.788-1aedd1a3
uri=/pics

hdr=--add-header="cookie:i=$session\n"
sfx=--num-calls=1
pfx="httperf --port=8888 --uri=$uri $hdr" 

$pfx --print-reply --print-request&&

for n in 1 10 100 1000 10000 100000;do
	$pfx --num-conns=$n $sfx;
done

echo "`date`: done"