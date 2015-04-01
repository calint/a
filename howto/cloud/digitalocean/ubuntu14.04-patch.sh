IP=$(if [ -z $1 ];then echo 104.131.63.23;else echo $1;fi)
WS=$(realpath $(if [ -z $2 ];then echo ../../../..;else echo $2;fi))
SRC=$WS/a/howto/cloud/digitalocean/ubuntu14.04/
date
echo "  workspace: $WS"
echo "      patch: $SRC"
echo "destination: $IP"
echo
while :;do
	rsync --recursive --verbose --progress --exclude .svn -aze 'ssh -oStrictHostKeyChecking=no -oBatchMode=yes' $SRC root@$IP:/
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo "`date +$DTF`  ${COLR}·  waiting for update $IP:/a/ from $WS/a/"
done
date
while :;do
	ssh root@$IP 'sh /ramvark-install.sh'
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo "`date +$DTF`  ${COLR}·  waiting for $IP"
done
date

