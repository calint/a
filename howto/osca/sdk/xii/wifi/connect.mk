#!/bin/sh
BIN=xii.wifi.connect
gcc -o $BIN *.c&&
chmod u+s $BIN&&
ls -l $BIN