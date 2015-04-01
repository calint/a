namespace b{
static const char*name="xiinux web server";
#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<sys/socket.h>
#include<sys/epoll.h>
#include<netinet/in.h>
#include<fcntl.h>
#include<errno.h>
#include<signal.h>
#include<unistd.h>
#include<sys/sendfile.h>
#include<pthread.h>
#include<ctype.h>
#include<sys/stat.h>
static int const K=1024;
static size_t const conbufnn=K;//? 4K
static int const nconbacklog=10*K;//? 20K
static int const port=8088;
class stats{
public:
	unsigned long long ms;
	unsigned long long input;
	unsigned long long output;
	unsigned long long accepts;
	unsigned long long reads;
	unsigned long long writes;
	unsigned long long files;
	unsigned long long widgets;
	unsigned long long cache;
	unsigned long long errors;
	unsigned long long bp;
	unsigned long long cbc;
	stats(){
		const int thsz=sizeof(stats);
		bzero(this,thsz);
	}
	void printhdr(FILE*f){
		fprintf(f,"%12s%12s%12s%8s%8s%8s%8s%8s%8s%8s%8s%8s\n","ms","input","output","accepts","reads","writes","files","widgets","cache","errors","bp","cbc");
		fflush(f);
	}
	void print(FILE*f){
		fprintf(f,"\r%12llu%12llu%12llu%8llu%8llu%8llu%8llu%8llu%8llu%8llu%8llu%8llu",ms,input,output,accepts,reads,writes,files,widgets,cache,errors,bp,cbc);
		fflush(f);
	}
};
static stats stats;
template<class T>class lut{
private:
	int size;
	class el{
	public:
		const char*key;
		T data;
		el*nxt;
		el(const char*key,T data):key(key),data(data),nxt(NULL){}
//		virtual ~el(){
//			if(nxt)delete nxt;
//		}
	};
	el**array;
public:
	static unsigned int hash(const char*key,const unsigned int roll){
		unsigned int i=0;
		const char*p=key;
		while(*p)
			i+=*p++;
		i%=roll;
		return i;
	}
	lut(const int size=8):size(size){
		array=(el**)calloc(size,sizeof(el*));
	}
	~lut(){
		clear();
		delete array;
	}
	T operator[](const char*key)const{
		const int h=hash(key,size);
		el*l=array[h];
		if(!l)
			return NULL;
		while(1){
			if(!strcmp(l->key,key)){
				return l->data;
			}
			if(l->nxt){
				l=l->nxt;
				continue;
			}
			return NULL;
		}
		return NULL;//? toavoidwarningincompiler
	}
	void put(const char*key,T data){
		const int h=hash(key,size);
		el*l=array[h];
		if(!l){
			array[h]=new el(key,data);
			return;
		}
		while(1){
			if(!strcmp(l->key,key)){
				l->data=data;
				return;
			}
			if(l->nxt){
				l=l->nxt;
				continue;
			}
			l->nxt=new el(key,data);
			return;
		}
	}
	void clear(){
		for(int i=0;i<size;i++){
			el*e=array[i];
			if(!e)
				continue;
			delete e;
			array[i]=NULL;
		}
	}
};
class xser{
private:
	FILE*in;
	FILE*out;
public:
	xser(FILE*in,FILE*out):in(in),out(out){}
	xser&w(const size_t d){fprintf(out,"%ld ",d);return*this;}
	xser&r(size_t&d){fscanf(in,"%ld ",&d);return*this;}
	xser&w(const char*b,const size_t size=0){fprintf(out,"%ld %s ",size?size:strlen(b),b);return*this;}
	xser&r(char**buf,size_t&size){
		if(*buf)delete *buf;
		fscanf(in,"%ld ",&size);
		*buf=new char[size];
		const size_t s=fread(*buf,size,1,in);
		if(s!=1)
			{perror("rs");exit(101);}
		return*this;
	}
	xser&r(char**buf){size_t size=0;return r(buf,size);}
	xser&flush(){fflush(out);return*this;}
};
class uri;
class session{
	char*id;
	lut<uri*>uris;
public:
	~session(){
		if(id)delete id;
		//? delete uris
	}
//	void xserto(xser&x){x.w(id);}
//	void xserfr(xser&x){x.r(&id);}

	session(/*own*/char*id):id(id){}
	lut<uri*>&geturis(){return uris;}
	const char*getid()const{return id;}
};
static lut<session*>sessions;
class sock{
private:
	int state;
	int fdfile;
	off_t fdfileoffset;
	long long fdfilecount;
	char*pth;
	char*qs;
	lut<char*>hdrs;
	session*ses;
	char*hdrp;
	char*hdrvp;
	char*sesid;
	int setcookie;
	int fd;
	char buf[conbufnn];
	char*bufp;
	size_t bufi;
	size_t bufnn;
public:
//	inline sock(const int fd):state(0),qs(0),ses(0),hdrp(0),sesid(0),setcookie(0),fd(fd),bufp(buf),bufi(0),bufnn(0){}

	inline sock(const int fd):state(0),fdfile(0),fdfileoffset(0),fdfilecount(0),pth(0),qs(0),ses(0),hdrp(0),hdrvp(0),sesid(0),setcookie(0),fd(fd),bufp(buf),bufi(0),bufnn(0){}
	inline ~sock(){if(sesid)delete sesid;close(fd);}
	inline int getfd()const{return fd;}
	inline void setfd(const int fd){this->fd=fd;}
	inline int hascookie()const{return setcookie;}
	inline void setcookiedone(){setcookie=0;}
	session&getsession(){
		if(ses)return*ses;
		sesid=hdrs["cookie"];//? whatifcookiesentatfirstreq
		if(!sesid){
			sesid=new char[32];
			strncpy(sesid,"i=abcd",32);//? mkcookie()
			ses=new session(sesid);
			sessions.put(sesid,ses);
			setcookie=1;
		}else{
			ses=sessions[sesid];
			if(!ses){
				ses=new session(sesid);
				sessions.put(sesid,ses);
			}
		}
		return*ses;
	}
	int run();
};
class xwriter{
	sock&sk;
public:
	inline xwriter(sock&s):sk(s){}
	inline sock&getsock()const{return sk;}
	xwriter&reply_http(const int code,const char*content){
		const size_t nn=strlen(content);
		char cookie[128];
		if(sk.hascookie())
			sprintf(cookie,"Set-Cookie: %s; Path=/; Expires=Wed, 09 Jun 2021 10:18:14 GMT\r\n",sk.getsession().getid());
		else
			*cookie=0;
		char bb[K+nn];
		sprintf(bb,"HTTP/1.1 %d\r\nServer: xiinux\r\nConnection: Keep-Alive\r\nContent-Length: %ld\r\n%s\r\n",code,nn,cookie);
		pk(bb).pk(content,nn);//?. oneippacket
		sk.setcookiedone();
		return*this;
	}
	xwriter&pk(const char*s,const size_t nn){
		const ssize_t n=send(sk.getfd(),s,nn,0);
		if(n!=-1)
			stats.output+=n;
		if(n!=ssize_t(nn)){
			if(errno==32)
				stats.bp++;
			else{
				stats.errors++;
				perror("pk");
				printf("\n\n%s:%d errno=%d   sent %ld of %ld\n\n",__FILE__,__LINE__,errno,n,nn);
			}
		}
		return*this;
	}
	inline xwriter&pk(const char*s){const size_t snn=strlen(s);return pk(s,snn);}
};
class buf{
private:
	char*data;
protected:
	size_t ndata;
public:
	buf(char*data=NULL,const int size=0):data(data),ndata(size){
		if(!data)return;
		this->ndata=size?size:strlen(data);
	}
	virtual ~buf(){
		if(data)delete data;
	}
	void setbuf(/*own*/char*b,const size_t s){
		if(data)delete data;
		data=b;
		ndata=s;
	}
	inline const char*getbuf()const{return data;}
	inline ssize_t getsize()const{return ndata;}
	inline virtual void to(xwriter&x){x.pk(data,ndata);}
	virtual void xserto(xser&x){x.w(ndata).w(data,ndata);}
	virtual void xserfr(xser&x){x.r(&data,ndata);}
};
class httpbuf:public buf{public:
	void tohttp(const char*c,const size_t s,const char*lastmod=NULL){
		char lastmodhdr[128];*lastmodhdr=0;
		if(lastmod)sprintf(lastmodhdr,"Last-Modified: %s\r\n",lastmod);
		char*bb=new char[K+s];
		const int nn=sprintf(bb,"HTTP/1.1 200\r\nServer: xiinux\r\nConnection: Keep-Alive\r\nContent-Length: %ld\r\n%s\r\n",s,lastmodhdr);
		strncpy(bb+nn,c,s);
		setbuf(bb,nn+s);
	}
};
class filehttp:public httpbuf{
protected:
	const char*path;
	struct timespec mtim;
public:
	filehttp(const char*path):path(path){
		sync();
	}
	virtual ~filehttp(){
		puts("homepage del");
	}
	int sync(){
		struct stat stt;
		if(stat(path,&stt))//?. evry1000ms
			return 1;
		if(stt.st_mtim.tv_sec!=mtim.tv_sec||stt.st_mtim.tv_nsec!=mtim.tv_nsec){
			mtim=stt.st_mtim;
			ssize_t s=stt.st_size;
			char*b=new char[ndata];
			int fd=open(path,O_RDONLY);
			if(fd==-1)return 1;
			const ssize_t nn=read(fd,b,s);
			if(nn!=s)perror("could not read file");
			close(fd);
			const struct tm*tm=localtime(&stt.st_mtime);
			char lm[64];
			//"Fri, 31 Dec 1999 23:59:59 GMT"
			strftime(lm,size_t(64),"%a, %ld %b %y %H:%M:%S %Z",tm);
			tohttp(b,s,lm);
			delete b;
		}
		return 0;
	}
	inline virtual void to(xwriter&x){
//		if(sync())return;
		httpbuf::to(x);
	}
};
static buf homepage=filehttp("qa/file.txt");
class uri:public buf{public:
	virtual void ax(xwriter&x,char*a[]=0){if(a)x.pk(a[0]);}
};
static char*strtrm(char*p,char*e){
	while(p!=e&&isspace(*p))
		p++;
	while(p!=e&&isspace(*e))
		*e--=0;
	return p;
}
static void strlwr(char*p){
	while(*p){
		*p=tolower(*p);
		p++;
	}
}
static uri*uriget(const char*qs);// forward declaration of generated function
int sock::run(){
	if(state==6){
		const ssize_t sf=sendfile(fd,fdfile,&fdfileoffset,fdfilecount);
		if(sf<0){stats.errors++;perror("resume sendfile");printf("\n\n%s  %d\n\n",__FILE__,__LINE__);return 0;}
		fdfilecount-=sf;
		stats.output+=sf;
		if(fdfilecount!=0){
			state=6;
			return 2;
		}
		close(fdfile);
//		state=8;//? goto while true

		state=0;
		const char*str=hdrs["connection"];//? http1.1iskeepalivebydefault
		if(str){
			strlwr((char*)str);
			if(!strcmp("keep-alive",str)){
				if(bufi==bufnn)
					return 1;
			}else
				return 0;
		}else return 0;
	}
	if(bufi==bufnn){
		int sbufnn=recv(fd,buf,conbufnn,0);
		if(sbufnn==0){//closed
			stats.cbc++;//?
	//		perror("recv");
			return 0;
		}
		if(sbufnn<0)
			if(errno!=EAGAIN&&errno!=EWOULDBLOCK){//error
				perror("recv");
				printf("\n%s:%d errno=%d client error\n\n",__FILE__,__LINE__,errno);
				stats.errors++;
				return 0;
			}
		bufnn=sbufnn;
		bufi=0;
		bufp=buf;
		stats.input+=bufnn;
	}
	//? requestlineandheadersinonereadorbreaks
	while(true){
		switch(state){
		case 0://method
			while(true){
				if(bufi==bufnn)return 1;
				bufi++;
				const char c=*bufp++;
				if(c==' '){
					pth=bufp;
					qs=0;
					state=1;break;
				}
			}
			if(state!=1)break;
		case 1://uri
			while(true){
				if(bufi==bufnn)return 1;
				bufi++;
				const char c=*bufp++;
				if(c==' '){
					*(bufp-1)=0;
					state=2;break;
				}else if(c=='?'){
					qs=bufp;
					*(bufp-1)=0;
					state=7;break;
				}
			}
			if(state!=7)break;
		case 7://querystr
			while(true){
				if(bufi==bufnn)return 1;
				bufi++;
				const char c=*bufp++;
				if(c==' '){
					*(bufp-1)=0;
					state=2;break;
				}
			}
		case 2://protocol
			while(true){
				if(bufi==bufnn)return 1;
				bufi++;const char c=*bufp++;
				if(c=='\n'){
					hdrs.clear();
					ses=NULL;
					hdrp=bufp;
					state=3;break;
				}
			}
		case 3://header key
			while(true){
				if(bufi==bufnn)return 1;
				bufi++;
				const char c=*bufp++;
				if(c==':'){
					*(bufp-1)=0;
					hdrvp=bufp;
					state=4;break;
				}else if(c=='\n'){
					const char*path=pth+1;//?? assumingminlen1
					xwriter x=xwriter(*this);
					if(!*path&&qs){
						stats.widgets++;
						session&ses=getsession();
						lut<uri*>&uris=ses.geturis();
						uri*o=uris[qs];
						if(!o){
							o=uriget(qs);
							uris.put(qs,o);
						}
						o->to(x);
						state=8;break;
					}
					if(!*path){
						stats.cache++;
						homepage.to(x);
						state=8;break;
					}
					struct stat fdstat;
					if(stat(path,&fdstat)){x.reply_http(404,"not found");state=8;break;}
					stats.files++;
					const struct tm*tm=localtime(&fdstat.st_mtime);
					char lastmod[64];
					//"Fri, 31 Dec 1999 23:59:59 GMT"
					strftime(lastmod,size_t(64),"%a, %ld %b %y %H:%M:%S %Z",tm);
					const char*lastmodstr=hdrs["if-modified-since"];
					if(lastmodstr&&!strcmp(lastmodstr,lastmod)){
						const char*hdr="HTTP/1.1 304\r\nConnection: Keep-Alive\r\n\r\n";
						const ssize_t hdrnn=strlen(hdr);
						const ssize_t hdrsn=send(fd,hdr,hdrnn,0);
						if(hdrsn!=hdrnn){stats.errors++;printf("\n\n%s  %d\n\n",__FILE__,__LINE__);return 0;}
						stats.output+=hdrsn;
						state=8;break;
					}
					fdfile=open(path,O_RDONLY);
					if(fdfile==-1){x.reply_http(404,"cannot open");return 0;}
					fdfileoffset=0;
					fdfilecount=fdstat.st_size;
					const char*range=hdrs["range"];
					char bb[K];
					if(range&&*range){
						unsigned long long rs=0;
						if(EOF==sscanf(range,"bytes=%llu",&rs)){
							stats.errors++;
							printf("\n\n%s  %d\n\n",__FILE__,__LINE__);
							return 0;
						}
						fdfileoffset=rs;
						const unsigned long long s=rs;
						const unsigned long long e=fdfilecount;
						fdfilecount-=rs;
						sprintf(bb,"HTTP/1.1 206\r\nServer: xiinux\r\nConnection: Keep-Alive\r\nAccept-Ranges: bytes\r\nLast-Modified: %s\r\nContent-Length: %lld\r\nContent-Range: %lld-%lld/%lld\r\n\r\n",lastmod,fdfilecount,s,e,e);
					}else{
						sprintf(bb,"HTTP/1.1 200\r\nServer: xiinux\r\nConnection: Keep-Alive\r\nAccept-Ranges: bytes\r\nLast-Modified: %s\r\nContent-Length: %lld\r\n\r\n",lastmod,fdfilecount);
					}
					const ssize_t bbnn=strlen(bb);
					const ssize_t bbsn=send(fd,bb,bbnn,0);
					if(bbsn!=bbnn){stats.errors++;printf("\n%s:%d sending header\n",__FILE__,__LINE__);return 0;}
					stats.output+=bbsn;//? -1
					const ssize_t nn=sendfile(fd,fdfile,&fdfileoffset,fdfilecount);
					if(nn<0){
						if(errno==32){//broken pipe
							stats.bp++;
							return 0;
						}
						stats.errors++;
						perror("sending file");
						printf("\n%s:%d errno=%d\n",__FILE__,__LINE__,errno);
						return 0;
					}
					stats.output+=nn;
					fdfilecount-=nn;
					if(fdfilecount!=0){
						state=6;
						return 2;
					}
					close(fdfile);
					state=8;break;
				}
			}
			if(state!=4)break;
		case 4://header value
			while(true){
				if(bufi==bufnn)return 1;
				bufi++;
				const char c=*bufp++;
				if(c=='\n'){
					*(bufp-1)=0;
					hdrp=strtrm(hdrp,hdrvp-2);
					strlwr(hdrp);
					hdrvp=strtrm(hdrvp,bufp-2);
					hdrs.put(hdrp,hdrvp);//?? strncpy
	//				hdrs.put(hdrp,hdrvp,true);// delete hdrp and hdrvp
	//				const size_t nn=strlen(hdrp);
	//				hdrs.put(strncat(new char[nn],hdrp,nn),hdrvp);
					hdrp=bufp;
					state=3;break;
				}
			}
			break;
		case 8://nextreq
			state=0;
			const char*str=hdrs["connection"];
			if(str){
				strlwr((char*)str);
				if(!strcmp("keep-alive",str)){
					if(bufi==bufnn)
						return 1;
				}else return 0;
			}else return 0;
			break;
		}
	}
}
static sock server(0);
void*thdwatchrun(void*arg){
	if(arg)puts((const char*)arg);
	stats.printhdr(stdout);
	while(1){
		int n=10;
		while(n--){
			const int sleep=100000;
			usleep(sleep);
			stats.ms+=sleep/1000;
			stats.print(stdout);
		}
		fprintf(stdout,"\n");
	}
	return 0;
}
void sigexit(int i){
	puts("exiting");
	const int fd=server.getfd();
	if(fd>0)close(fd);
//	if(homepage)delete homepage;
    signal(SIGINT,SIG_DFL);
    kill(getpid(),SIGINT);
    exit(i);
}
int main(int argc,char**args){
	while(argc--)puts(*args++);
	signal(SIGINT,sigexit);
	puts(name);
	printf("  port %d\n",port);
	struct sockaddr_in srv;
	const ssize_t srvsz=sizeof(srv);
	bzero(&srv,srvsz);
	srv.sin_family=AF_INET;
	srv.sin_addr.s_addr=INADDR_ANY;
	srv.sin_port=htons(port);
	const int sfd=socket(AF_INET,SOCK_STREAM,0);
	if(sfd==-1){perror("socket");exit(1);}
	server.setfd(sfd);
	if(bind(sfd,(struct sockaddr*)&srv,srvsz)){perror("bind");exit(2);}
	if(listen(sfd,nconbacklog)==-1){perror("listen");exit(21);}
	const int epfd=epoll_create(nconbacklog);
	if(!epfd){perror("epollcreate");exit(3);}
	struct epoll_event ev;
	ev.events=EPOLLIN;
	ev.data.ptr=&server;
	if(epoll_ctl(epfd,EPOLL_CTL_ADD,sfd,&ev)<0){perror("epolladd");exit(4);}
	struct epoll_event events[nconbacklog];
	pthread_t thdwatch;
	if(pthread_create(&thdwatch,0,&thdwatchrun,0)){perror("threadcreate");exit(5);}
	while(1){
		const int nn=epoll_wait(epfd,events,nconbacklog,-1);
		if(nn==-1){perror("epollwait");exit(6);}
		for(int i=0;i<nn;i++){
			sock&c=*(sock*)events[i].data.ptr;
			if(c.getfd()==sfd){
				stats.accepts++;
				const int fda=accept(sfd,0,0);
				if(fda==-1){perror("accept");exit(7);}
				int opts=fcntl(fda,F_GETFL);
				if(opts<0){perror("getopts");exit(8);}
				opts|=O_NONBLOCK;
				if(fcntl(fda,F_SETFL,opts)){perror("setopts");exit(9);}
				ev.data.ptr=new sock(fda);
				ev.events=EPOLLIN|EPOLLRDHUP|EPOLLET;
				if(epoll_ctl(epfd,EPOLL_CTL_ADD,fda,&ev)){perror("epolladd");exit(10);}
//				if(c.read()){
//					delete&c;
//					continue;
//				}
				continue;
			}
			if(events[i].events&EPOLLIN)stats.reads++;else stats.writes++;
			switch(c.run()){
			case 0:delete&c;break;
			case 1://read
				events[i].events=EPOLLIN|EPOLLRDHUP|EPOLLET;
				if(epoll_ctl(epfd,EPOLL_CTL_MOD,c.getfd(),&events[i])){perror("epollmodread");delete&c;}
				break;
			case 2://write
				events[i].events=EPOLLOUT|EPOLLRDHUP|EPOLLET;
				if(epoll_ctl(epfd,EPOLL_CTL_MOD,c.getfd(),&events[i])){perror("epollmodwrite");delete&c;}
				break;
			}
		}
	}
}
}

//-- application
namespace a{
using namespace b;
class hello:public uri{
	virtual void to(xwriter&x){
		x.reply_http(200,"hello world");
	}
};
class bye:public uri{
	int c;
public:
	virtual void to(xwriter&x){
		session&sn=x.getsock().getsession();
		c++;
		char bb[K];
		sprintf(bb,"sessionid: %s\ndraw %d\n",sn.getid(),c);
		x.reply_http(200,bb);
	}
};
class notfound:public uri{
	virtual void to(xwriter&x){
		x.reply_http(404,"path not found");
	}
};
}
//--

//-- generated
namespace b{static uri*uriget(const char*qs){
	if(!strcmp("hello",qs))return new a::hello();
	if(!strcmp("bye",qs))return new a::bye();
	return new a::notfound();
}}
//--
int main(int argc,char**args){return b::main(argc,args);}
