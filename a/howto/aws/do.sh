INSTANCE_AMI="ami-b0659fd8"

# create
echo ' • launching instance'
aws ec2 run-instances --count 1 --image-id $INSTANCE_AMI --instance-type t1.micro --key-name ramvark-keypair --security-groups allopen > reservation.txt
INST=(`cat reservation.txt |grep INSTANCES|tr "\\t" "\n"`)
INSTANCE_ID=${INST[7]}
echo "  · $INSTANCE_ID"

echo ' • waiting for public address';
for((;;));do
	aws ec2 describe-instances --instance-ids $INSTANCE_ID > reservations.txt;
	#cat reservations.txt;
	export INST=(`cat reservations.txt|grep ASSOCIATION|tr "\\t" "\n"`);
	INSTANCE_DNS=${INST[2]};
	echo "  · $INSTANCE_DNS"
	if ! [ -z $INSTANCE_DNS ];then break;fi;
	sleep 1
done;

echo
echo " • updating $INSTANCE_DNS"
for((;;));do
#	curl -v localhost:8888
	rsync --timeout=15 --verbose --delete --exclude .svn --exclude u/ --exclude cache/ --progress -az -e "ssh -v -o StrictHostKeyChecking=no -i /Users/calin/wallet/ramvark-keypair.pem" "/Users/calin/Documents/workspace/a/" root@$INSTANCE_DNS:/a/
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo
	echo "  · trying to update $INSTANCE_DNS"
done

echo
echo " • restart $INSTANCE_DNS"
ssh -o StrictHostKeyChecking=no -i $HOME/wallet/ramvark-keypair.pem root@$INSTANCE_DNS "killall -9 java ; /studio/suse-studio-custom"

echo
echo " • trying http://$INSTANCE_DNS/"
curl --verbose $INSTANCE_DNS
echo

# todo qa coverage scripts

echo
echo " • stressing http://$INSTANCE_DNS"
ab -c1  -t5 http://$INSTANCE_DNS/
ab -c10 -t5 http://$INSTANCE_DNS/
ab -c100  -t5 http://$INSTANCE_DNS/
ab -c1  -t5 http://$INSTANCE_DNS/typealine
ab -c10 -t5 http://$INSTANCE_DNS/typealine
ab -c100  -t5 http://$INSTANCE_DNS/typealine

echo
echo " • terminating $INSTANCE_ID"
for((;;));do
	aws ec2 terminate-instances --instance-ids $INSTANCE_ID
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo
	echo "  · trying to terminate instance $INSTANCE_ID"
done

echo
echo " •• done"
 