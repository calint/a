cd /&&

echo `date` download jre
curl --silent http://calintenitchi.googlecode.com/svn/amazon-ec2/jre.tgz>jre.tgz&&

echo `date` download jars
curl --silent http://calintenitchi.googlecode.com/svn/amazon-ec2/a-lib.tgz>a-lib.tgz&&

echo `date` download ramvark
curl --silent http://calintenitchi.googlecode.com/svn/amazon-ec2/a.tgz>a.tgz&&

echo `date` unpack
tar -xzf a.tgz&&
tar -xzf a-lib.tgz&&
tar -xzf jre.tgz&&

chmod ugo+x /etc/rc.local&&
#apt-get update&&

echo `date` install gcc clang
apt-get --quiet=2 -y install gcc clang&&
dpkg -i deb/* 1>/dev/null

echo `date` open port 80
/etc/rc.local &

echo `date` done
