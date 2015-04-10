BIN=sticky
SRC=sticky.c
#OPTS=-Os
WARNINGS="-Wall -Wextra"
LIBS=$(pkg-config --cflags --libs gtk+-3.0 gtksourceview-3.0)

echo &&
gcc  -o $BIN  $SRC $LIBS $OPTS $WARNINGS && 
echo    "             lines   words  chars" &&
echo -n "       wc:" &&
cat $SRC|wc
echo -n "wc zipped:" &&
cat $SRC|gzip|wc &&
echo && ls -o --color $BIN &&
echo
