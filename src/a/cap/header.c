#include<stdio.h>
#define d(fmt, ...)printf("\n%s:%d: " fmt,__FILE__,__LINE__,__VA_ARGS__);
#define nl()printf("\n");
#define p(format,args...)fprintf(stdout,format,##args)
#define pl(format,args...)p(format,##args);nl()
#define pi(M, ...) fprintf(stderr, "[INFO] (%s:%d) " M "\n", __FILE__, __LINE__, ##__VA_ARGS__)
#define pls(stream,format,args...)fprintf(stream,format,##args)
#define out stdout
typedef size_t size;
typedef FILE* stream;
#define null NULL