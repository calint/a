. ./make-env.sh &&
FN=$(date +%Y-%m-%d--%H-%M-%S--%N).png
echo "*** $0 $FN" && 
./$BIN $FN &&
#ls --color -l &&
echo -n