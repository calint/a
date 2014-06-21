for p in a;do
  echo $p
  cp -av ../$p/* . 
done
# blitz.io key
echo -n 42>mu-1f7c1fa1-64535080-7d8aa870-6cb91bba
zip deploy.zip -r .
ls -lah deploy.zip
bees app:deploy -a bb -t java -P java_version=1.8 -R classpath=bin -R class=b.b -P args="root_dir app" -v -d false deploy.zip stickySession=true httpVersion=1.1 jvmFileEncoding=UTF-8
#bees app:bind -db ramvark -a bb -as ramvark
#rm deploy.zip
