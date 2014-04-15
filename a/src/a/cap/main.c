#include<stdio.h>
#define d(fmt, ...)printf("\n%s:%d: " fmt,__FILE__,__LINE__,__VA_ARGS__);
#define nl()printf("\n");
#define p(format,args...)fprintf(stdout,format,##args)
#define pl(format,args...)p(format,##args);nl()
#define pi(M, ...) fprintf(stderr, "[INFO] (%s:%d) " M "\n", __FILE__, __LINE__, ##__VA_ARGS__)
#define out stdout
typedef size_t size;
typedef FILE* stream;
#define null NULL
/// main.cap
/// 1 : foo{
/// 2 : 	tk=1;
/// 3 : 	float i;
/// 4 : 	to(stream){
/// 5 : 		pl("%p{%d %f}",o,o->tk,o->i);
/// 6 : 	}
/// 7 : }
/// 8 :
/// 9 :
/// 10 : cap{run(){
/// 11 : 	pl("hello world");
/// 12 : 	char ram[]="hellos. bootsector.  32 bytes..";
/// 13 :     pi("cap compiled");
/// 14 :     pl("sizeof(%s)=%lu B","int",sizeof(int));
/// 15 :     pl("sizeof(%s)=%lu B","float",sizeof(float));
/// 16 :     pl("sizeof(%s)=%lu B","long",sizeof(long));
/// 17 :     pl("sizeof(%s)=%lu B","ram",sizeof(ram));
/// 18 :     pl("%p",foo_mk);
/// 19 :     pl("%p",foo_tk);
/// 20 :     ram[0]=1;
/// 21 :     foo f=foo_default;
/// 22 :     const int tk=foo_tk(&f);
/// 23 :     pl("%d",tk);
/// 24 :     pl("%d",foo_tk(&f));
/// 25 :
/// 26 :     foo_tk_(&f,3);
/// 27 :     foo_to(&f,out);
/// 28 :     const foo f2=foo_mk();
/// 29 :     pl("%d",foo_tk(&f2));
/// 30 :
/// 31 :     foo foos[8];
/// 32 :     for(int i=0;i<8;i++){
/// 33 :         foos[i]=foo_mk();
/// 34 :         foos[i].i=i;
/// 35 :         foo_tk_(&foos[i],i);
/// 36 :     }
/// 37 :     for(int i=0;i<8;i++)
/// 38 :         foo_to(&foos[i],out);
/// 39 : }}
///
typedef struct foo foo;
static struct foo{
	int tk;
	float i;
}foo_default={.tk=1,.i=0,};
typedef struct foo foo;
static inline foo foo_mk(){return foo_default;}///keep stack pointer
static inline int foo_tk(const foo*o){return o->tk;}
static inline void foo_tk_(foo*o,int v){o->tk=v;}
static inline float foo_i(const foo*o){return o->i;}
static inline void foo_i_(foo*o,float v){o->i=v;}
static inline void foo_to(foo*o,stream s){
		pl("%p{%d %f}",o,o->tk,o->i);
	}
typedef struct cap cap;
static struct cap{
}cap_default={};
typedef struct cap cap;
static inline cap cap_mk(){return cap_default;}///keep stack pointer
static inline void cap_run(cap*o){
	pl("hello world");
	char ram[]="hellos. bootsector.  32 bytes..";
    pi("cap compiled");
    pl("sizeof(%s)=%lu B","int",sizeof(int));
    pl("sizeof(%s)=%lu B","float",sizeof(float));
    pl("sizeof(%s)=%lu B","long",sizeof(long));
    pl("sizeof(%s)=%lu B","ram",sizeof(ram));
    pl("%p",foo_mk);
    pl("%p",foo_tk);
    ram[0]=1;
    foo f=foo_default;
    const int tk=foo_tk(&f);
    pl("%d",tk);
    pl("%d",foo_tk(&f));

    foo_tk_(&f,3);
    foo_to(&f,out);
    const foo f2=foo_mk();
    pl("%d",foo_tk(&f2));

    foo foos[8];
    for(int i=0;i<8;i++){
        foos[i]=foo_mk();
        foos[i].i=i;
        foo_tk_(&foos[i],i);
    }
    for(int i=0;i<8;i++)
        foo_to(&foos[i],out);
}
/// main.cap done
int main(int argc,char*argv[]){
    cap_run(null);
    return 0;
}
