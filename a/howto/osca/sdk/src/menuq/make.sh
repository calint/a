#!/bin/sh
bin=menuq
rm -f $bin
gcc -o $bin *.c -lX11 -Wall -Wextra -Wfatal-errors&&
ls -l $bin
