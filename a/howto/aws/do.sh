INSTANCE_AMI="ami-b0659fd8"

# create
echo ' • launching instance'
aws ec2 run-instances --count 1 --image-id $INSTANCE_AMI --instance-type t1.micro --key-name ramvark-keypair --security-groups allopen > reservation.txt
INST=(`cat reservation.txt |grep INSTANCES|tr "\\t" "\n"`)
INSTANCE_ID=${INST[7]}
echo " • instance id: $INSTANCE_ID"

echo ' • waiting for ip';
for((;;));do
	aws ec2 describe-instances --instance-ids $INSTANCE_ID > reservations.txt;
	#cat reservations.txt;
	export INST=(`cat reservations.txt|grep ASSOCIATION|tr "\\t" "\n"`);
	INSTANCE_DNS=${INST[2]};
	echo " • public dns name: $INSTANCE_DNS"
	if ! [ -z $INSTANCE_DNS ];then break;fi;
	sleep 1
done;

echo
echo ' • sending files'
for((;;));do
#	curl -v localhost:8888
	rsync --timeout=15 --verbose --delete --exclude .svn --exclude u/ --exclude cache/ --progress -az -e "ssh -v -o StrictHostKeyChecking=no -i /Users/calin/wallet/ramvark-keypair.pem" "/Users/calin/Documents/workspace/a/" root@$INSTANCE_DNS:/a/
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo
	echo \ • trying
done

echo
echo \ • restart $INSTANCE_DNS
ssh -o StrictHostKeyChecking=no -i $HOME/wallet/ramvark-keypair.pem root@$INSTANCE_DNS "killall -9 java ; /studio/suse-studio-custom"

echo
echo " • try $INSTANCE_DNS/"
curl $INSTANCE_DNS
echo

echo
echo " • deleting $INSTANCE_ID"
for((;;));do
	aws ec2 terminate-instances --instance-ids $INSTANCE_ID
	if [ $? -eq 0 ];then break;fi
	sleep 1
	echo
	echo \ • trying
done

echo
echo " • done"
 