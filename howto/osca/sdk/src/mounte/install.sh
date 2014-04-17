#!/bin/sh
DST=/sdk/bin/mounte
cp -p mounte $DST&&
chown root $DST&&
chmod u+s $DST&&
ls -l $DST
