. ./env.sh

VERBOSE=--verbose
#VERBOSE=   # silent
                       # wait for port to free
CMD_RESET='killall java;sleep 1;/etc/rc.local>/var/log/ramvark.log&'
SSH_OPTS='-oBatchMode=yes -oStrictHostKeyChecking=no -oConnectTimeout=60'

DTF=%F---%H:%M:%S-
COLR=

echo "`date +$DTF`  ${COLR}•  update $HOST_:/a/ from $WORKSPACE_/a/"
while :;do
	rsync -e "ssh -i $KEY_" --recursive $VERBVOSE --exclude .svn --exclude u/ $WORKSPACE_/a/ root@$HOST_:/a/
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo "`date +$DTF`  ${COLR}·  waiting for update $HOST_:/a/ from $WORKSPACE_/a/"
done

echo "`date +$DTF`  ${COLR}•  reset $HOST_ with '$CMD_RESET'"
#echo $SSH_OPTS
while :;do
	ssh -i $KEY_ $SSH_OPTS root@$HOST_ $CMD_RESET
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo "`date +$DTF`  ${COLR}·  waiting for $IP:to ssh"
done

echo "`date +$DTF`  ${COLR}•  wait for http://$IP"
while :;do
	curl $HOST_
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo "`date +$DTF`  ${COLR}·  waiting for $IP:to respond on port 80"
done

#while [ $(curl $IP) -neq 0 ]; do echo "`date +$DTF`  ${COLR}·  waiting for http://$IP";sleep 1;done;
#curl $IP

echo "`date +$DTF`  ${COLR}•  pid"
ssh -i $KEY_ $SSH_OPTS root@$HOST_ 'ps aux|grep java|sed /grep/d|tr -s " "|cut -d " " -f2'

echo "`date +$DTF`  ${COLR}•  done"
