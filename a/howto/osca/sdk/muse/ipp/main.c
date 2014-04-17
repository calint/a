#include<stdio.h>
#include<string.h>
#define K 1024
struct pk{
	unsigned long long int from;
	unsigned long long int to;
	unsigned char bits;
	unsigned int len;
	void*data;
};
const int pk_bit_frst=1;
const int pk_bit_last=2;
const int pk_bit_clos=4;
void pkprint(FILE*f,struct pk*p){
	fprintf(f,"from: %llu\n",p->from);
	fprintf(f,"  to: %llu\n",p->to);
	fprintf(f,"bits: %x\n",p->bits);
	fprintf(f," len: %u\n",p->len);
	fprintf(f,"%s\n",p->data);
}
const int pk_max_pk_size=4*K*K;
void ethput(struct pk*p,void pf(const long long int,const long long int)){
	printf("### ethsend %u bytes from %llu to %llu with bits %x\n",p->len,p->from,p->to,p->bits);
	puts((const char*)p->data);
	puts("### ethsend done");
	pf(p->len,p->len);
}
void send(const long long int from,const long long int to,const long long int len,const void*data,void pf(const long long int,const long long int)){
	long long int i=len;
	int b=0;
	char*d=(char*)data;
	struct pk p;
	while(i>pk_max_pk_size){
		p.from=from;
		p.to=to;
		p.len=pk_max_pk_size;
		p.data=d;
		d+=pk_max_pk_size;
		i-=pk_max_pk_size;
		if(!b)
			b=p.bits=pk_bit_frst;
		if(!i)
			p.bits|=pk_bit_last;
		ethput(&p,pf);
	}
	if(!i)
		return;
	p.from=from;
	p.to=to;
	p.len=i;
	p.data=d;
	if(!b)
		p.bits=pk_bit_frst;
	p.bits|=pk_bit_last;
	ethput(&p,pf);
}
static void pf(const long long int i,const long long int n){
	printf("progress %lld/%lld\r",i,n);
}
static const char msg[]="GET / HTTP/1.1\r\nHost: localhost\r\nConnection: Keep-Alive\r\n\r\n";
static const char msg2[]="GET /?hello#1 HTTP/1.1\r\nHost: localhost\r\nConnection: Close\r\nCookie: i=1\r\n\r\n";
static long long int ipfr=0x0000000000000001;
static long long int ipto=0x0000000000000002;
void rec(struct pk*p){
//	puts("### received");
//	pkprint(stdout,p);
	printf("### ethrec %u bytes from %llu to %llu with bits %x\n%s\n",p->len,p->from,p->to,p->bits,p->data);
}
void(*ethrec)(struct pk*)=rec;
void _doreply(){
	struct pk p;
	p.from=ipto;
	p.to=ipfr;
	p.data="HTTP/1.1 200\r\nConnection: Keep-Alive\r\nCookie: i=1\r\nContent-Length: 11\r\n\r\nhello world";
	p.len=strlen((const char*)p.data);
	p.bits=pk_bit_frst|pk_bit_last;
	ethrec(&p);
}
void _doreply2(){
	struct pk p;
	p.from=ipto;
	p.to=ipfr;
	p.data="HTTP/1.1 200\r\nConnection: Close\r\nContent-Length: 5\r\n\r\nhello";
	p.len=strlen((const char*)p.data);
	p.bits=pk_bit_frst|pk_bit_last;
	ethrec(&p);
}
int main(){
	printf("sizeof(struct pk)=%dB\n",sizeof(struct pk));
	send(ipfr,ipto,strlen(msg),msg,pf);
	printf("\n");
	_doreply();
	send(ipfr,ipto,strlen(msg2),msg2,pf);
	printf("\n");
	_doreply2();
}
