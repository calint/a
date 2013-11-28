date&&
siege -V&&
siege -C&&
siege -bv -c1     -r1 localhost:8888&&
siege -bg -c1     -r1 localhost:8888&&
siege -bv -c10    -r3 localhost:8888&&
siege -bv -c100   -r3 localhost:8888&&
#siege -bv -c1000  -r3 localhost:8888&&
#siege -bv -c10000 -r3 localhost:8888&&
date
