. ./make-env.sh &&
echo "*** $0" && 
cc -Os -I/usr/include/cairo/ -lX11 -lcairo $SRC -o $BIN && 
echo &&
echo -n "   source:" &&
cat $SRC|wc &&
echo -n "   zipped:" &&
cat $SRC|gzip|wc &&
echo && 
ls -o --color $BIN &&
echo -n
