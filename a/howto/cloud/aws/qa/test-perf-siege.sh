#!/bin/sh
url=localhost:8888

date&&
echo $0&&

siege -V&&
siege -C&&
siege -bg $url&&
siege -bv -c1     -r1    $url&&
siege -b  -c10    -r1000 $url&&
siege -b  -c100   -r100  $url&&
siege -b  -c1000  -r10   $url&&
siege -b  -c10000 -r1    $url&&

date
