#!/bin/sh
DIR=$(dirname "$0")
cd $DIR &&

cmd="java -cp bin`ls $DIR/lib|while read f;do echo -n :;echo -n $DIR/lib/$f;done` b.b $*" &&
echo \> $cmd;$cmd


#java -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining -cp bin:../b/bin b.b
