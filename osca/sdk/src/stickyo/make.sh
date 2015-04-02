#!/bin/sh
app=stickyo
gcc -o $app $app.c -Wfatal-errors -Wall -Wextra `pkg-config --cflags --libs gtk+-2.0`&&
ls -l $app
