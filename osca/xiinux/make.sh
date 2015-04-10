BIN=xiinux
SRC=xiinux.cpp
#OPTS=-Os
WARNINGS="-Wall -Wextra"
LIBS=-lpthread
COMPILER=clang++

echo &&
$COMPILER  -o $BIN  $SRC $LIBS $OPTS $WARNINGS && 
echo    "             lines   words  chars" &&
echo -n "       wc:" &&
cat $SRC|wc
echo -n "wc zipped:" &&
cat $SRC|gzip|wc &&
echo && ls -o --color $BIN &&
echo
