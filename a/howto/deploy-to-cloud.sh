for p in b a c d2 d3 auplo;do
  cp -avr ../$p/* . 
done
zip deploy.zip -r .
ls -lah deploy.zip
bees app:deploy -a bb -t java -P java_version=1.8 -R classpath=bin -R class=b.b -P args="root_dir app" -v -d false deploy.zip stickySession=true httpVersion=1.1 jvmFileEncoding=UTF-8
rm deploy.zip
