xsetroot -bitmap $OSCA/bmp/sq.bmp
setxkbmap -option caps:none
synclient VertTwoFingerScroll=1 VertScrollDelta=-111 HorizTwoFingerScroll=1 HorizScrollDelta=-111
$OSCA/clonky/clonky & exec $OSCA/frameless/frameless
