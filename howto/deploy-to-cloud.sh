for p in a;do
  echo $p
  cp -av ../$p/* . 
done
# blitz.io key
echo -n 42>mu-1f7c1fa1-64535080-7d8aa870-6cb91bba
zip deploy.zip -r .
ls -lah deploy.zip
bees app:deploy -a bb -t java -P java_version=1.8 -R classpath=bin:lib/mysql-connector-java-5.1.31-bin.jar -R class=b.b -R args="cloud_bees true root_dir app" -v -d false deploy.zip stickySession=true httpVersion=1.1 jvmFileEncoding=UTF-8
#bees db:create -u ramvark -p ramvark ramvark
#bees app:bind -db ramvark -a bb -as ramvark
#rm deploy.zip





#/usr/local/mysql/bin/mysql --host=ec2-23-21-211-172.compute-1.amazonaws.com --port=3306 --user=ramvark --password=ramvark
