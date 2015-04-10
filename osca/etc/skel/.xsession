xsetroot -bitmap $OSCA/bmp/sq.bmp
xmodmap -e "clear Lock"
$OSCA/clonky/clonky & exec $OSCA/frameless/frameless
