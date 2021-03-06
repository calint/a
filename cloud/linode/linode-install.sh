#!/bin/sh
IP=$1
IMG=~/a.zip

echo " * init ubuntu 16 lts linode at $IP" &&
echo " * copy ssh public key to root" &&
ssh-copy-id root@$IP &&

echo " * copy systemd service file ba.service to /etc/systemd/system/" &&
scp ba.service root@$IP:/etc/systemd/system/ &&
ssh root@$IP systemctl enable ba &&

echo " * update package list" &&
ssh root@$IP apt-get update &&

echo " * update packages" &&
ssh root@$IP apt-get -y upgrade &&

echo " * install java runtime" &&
ssh root@$IP apt-get -y install openjdk-8-jre-headless unzip &&

echo " * copy install image  $IMG to $IP:/" &&
#scp $IMG root@$IP:/

echo " * unzip image" &&
ssh root@$IP unzip /a.zip -d / && 

echo " * adjust installed image" &&
ssh root@$IP chmod +x /a/ba.sh && 

echo " * reboot" &&
ssh root@$IP reboot && 

echo " * done"
