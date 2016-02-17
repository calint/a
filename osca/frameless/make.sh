BIN=frameless
SRC=src/frameless.c
#OPTS=-Os
WARNINGS="-Wall -Wextra"
LIBS=-lX11

echo &&
gcc  -o $BIN  $SRC $LIBS $OPTS $WARNINGS && 
echo    "             lines   words  chars" &&
echo -n "   source:" &&
cat $SRC|wc
echo -n "   zipped:" &&
cat $SRC|gzip|wc &&
echo && ls -o --color $BIN &&
echo
