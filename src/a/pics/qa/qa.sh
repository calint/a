echo "`date`: qa pics"

session=aaaa-131119-095209.788-1aedd1a3
uri=/pics

hdr=--add-header="cookie:i=$session\n"
pfx=--num-calls=1
sfx="httperf --port=8888 --uri=$uri $hdr" 

$sfx --print-reply --print-request&&

$sfx --num-conns=1      $pfx&&
$sfx --num-conns=10     $pfx&&
$sfx --num-conns=100    $pfx&&
$sfx --num-conns=1000   $pfx&&
$sfx --num-conns=10000  $pfx&&
$sfx --num-conns=100000 $pfx&&

echo "`date`: done"