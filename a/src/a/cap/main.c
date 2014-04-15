#include<stdio.h>
#define d(fmt, ...)printf("\n%s:%d: " fmt,__FILE__,__LINE__,__VA_ARGS__);
#define nl()printf("\n");
#define p(format,args...)fprintf(stdout,format,##args)
#define pl(format,args...)p(format,##args);nl()
#define out stdout
#define pi(M, ...) fprintf(stderr, "[INFO] (%s:%d) " M "\n", __FILE__, __LINE__, ##__VA_ARGS__)
typedef size_t size;
typedef FILE* stream;

// generated
typedef struct foo foo;
static struct foo{
	int tk;//rw
    int i;//r
}foo_default={1,2};
static inline foo foo_mk(){return foo_default;}// and dont change stack pointer // synthesized
static inline void foo_to(const foo*o,stream s){p("{%d %d}",o->tk,o->i);}// synthesized
static inline int foo_tk(const foo*o){return o->tk;}// synthesized
static inline void foo_tk_(foo*o,const int tk){o->tk=tk;}// synthesized
static inline int foo_i(const foo*o){return o->i;}// synthesized

int main(const int argc,const char**argv){
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
    foo_tk_(&f,3);
    pl("%d %d",foo_tk(&f),foo_i(&f));
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

    return 0;
}
