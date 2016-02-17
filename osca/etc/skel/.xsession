xsetroot -bitmap $OSCA/bmp/sq.bmp
setxkbmap -option caps:none
synclient VertTwoFingerScroll=1
synclient VertScrollDelta=-111
synclient HorizTwoFingerScroll=1
synclient HorizScrollDelta=-111
$OSCA/clonky/clonky & exec $OSCA/frameless/frameless
#$OSCA/clonky/clonky & exec ~/w/frameless/Debug/frameless
#exec xterm
