CC="gcc -std=c11"
BIN=sticky
SRC=sticky.c
DBG="-g -O0"
#OPTS=-Os
WARNINGS="-Wall -Wextra -Wpedantic -Wno-unused-parameter -Wfatal-errors"
LIBS=$(pkg-config --cflags --libs gtk+-3.0 gtksourceview-3.0)

echo &&
$CC  -o $BIN $SRC $DBG $LIBS $OPTS $WARNINGS && 
echo    "             lines   words   chars" &&
echo -n "   source:" &&
cat $SRC|wc &&
echo -n "   zipped:" &&
cat $SRC|gzip|wc &&
echo && ls -o --color $BIN &&
echo &&
valgrind --leak-check=yes ./$BIN
