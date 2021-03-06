CC="cc -std=c99"
#CC=cc
BIN=clonky
SRC=src/*.c
#OPTS="-Os -pedantic-errors -Wfatal-errors"
OPTS=
WARNINGS="-Wall -Wextra -Wno-unused-result -Wno-unused-function"
LIBS="-lX11 -lXft"
INCLUDES=-I/usr/include/freetype2/

echo &&
$CC -o $BIN  $SRC $INCLUDES $LIBS $OPTS $WARNINGS && 
echo    "             lines   words  chars" &&
echo -n "   source:" &&
cat $SRC|wc &&
echo -n "   zipped:" &&
cat $SRC|gzip|wc &&
echo && ls -o --color $BIN &&
echo &&
#valgrind --leak-check=yes --leak-check=full --show-leak-kinds=all ./$BIN
#valgrind --leak-check=yes ./$BIN
echo -n
