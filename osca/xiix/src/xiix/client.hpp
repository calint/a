#pragma once
#include"ns.hpp"
#include"args.hpp"
#include"sock.hpp"
#include<memory>
#include<vector>
#include<algorithm>
namespace xiix{class client final{
public:
	inline client(const int argc,const char**argv,const char**env){
		try{
			main(argc,argv);
		}catch(const char*msg){
			puts(" *** exception caught");
			puts(msg);
		}catch(...){
			puts(" *** exception caught");
		}
	}
private:
	inline int main(const int argc,const char**argv){
		args ag{argc,argv};
		if(ag.hasoption('h')){
			fprintf(stderr,"usage: %s [host=localhost] [port=8088] [uri=/] [connections=1] -psqv\n",argv[0]);
			return 0;
		}
		const char*hostname=ag.getarg(1,default_host);
		const int port=atoi(ag.getarg(2,default_port));
		const char*uri=ag.getarg(3,default_path);
		const int nsocks=atoi(ag.getarg(4,default_nclients));
		conf::print_traffic=ag.hasoption('p');
		conf::print_stats=ag.hasoption('s');
		if(ag.hasoption('q'))conf::print_content=false;
		const bool repeatmode=ag.hasoption('r');
		if(ag.hasoption('v')){
			printf("\n  x i i x   m a r k\n\n");
			if(port!=80)printf("           url: http://%s:%d%s\n",hostname,port,uri);
			else        printf("           url: http://%s%s\n",hostname,uri);
			printf("       clients: %d\n",nsocks);
			printf("        repeat: %s\n",repeatmode?"yes":"no");
			printf(" print traffic: %s\n",conf::print_traffic?"yes":"no");
			printf("   print stats: %s\n",conf::print_stats?"yes":"no");
			printf("   print reply: %s\n",conf::print_content?"yes":"no");
			puts("");
		}
		const int epollfd=epoll_create(nsocks);
		if(epollfd<0)throw"epoll_create";

		using namespace std;
		using sock_sp=shared_ptr<sock>;
		vector<sock_sp>socks;
		for(int i=0;i<nsocks;i++)socks.push_back(make_shared<sock>(epollfd,hostname,port));
		for_each(socks.begin(),socks.end(),[repeatmode,uri](sock_sp s){s->seturi(uri).setrepeatmode(repeatmode).connect();});


//		lst<sock*>socks;
//		for(int i=0;i<nsocks;i++)socks.add(new sock(epollfd,hostname,port));
//		socks.foreach2([repeatmode,uri](sock*s){s->seturi(uri).setrepeatmode(repeatmode).connect();return true;});
//		socks.foreach2([repeatmode,uri](auto s){s->seturi(uri).setrepeatmode(repeatmode).connect();return true;});

//		using namespace std;
//		vector<unique_ptr<sock>>socks;
//		for(auto&s:socks)s=new sock(epollfd,hostname,port);
//		for(int i=0;i<nsocks;i++)socks.add(unique_ptr<sock>(new sock(epollfd,hostname,port)));
//		for(int i=0;i<nsocks;i++)socks.push_back(make_unique<sock>(epollfd,hostname,port));
//		socks.foreach2([repeatmode,uri](unique_ptr<sock>s){s->seturi(uri).setrepeatmode(repeatmode).connect();return true;});
//		for_each(socks.begin(),socks.end(),[repeatmode,uri](unique_ptr<sock>s){s->seturi(uri).setrepeatmode(repeatmode).connect();});

		struct epoll_event*evs=(epoll_event*)calloc(nsocks,sizeof(struct epoll_event));
		while(1){
			const int n=epoll_wait(epollfd,evs,nsocks,500);
			for(int i=0;i<n;++i){
				meters::events++;
				struct epoll_event&ev=evs[i];
				sock*sk=(sock*)ev.data.ptr;
				try{
					sk->on_epoll_event(ev);
				}catch(const char*msg){
					if(msg==signal_close||msg==signal_connreset){
//						delete sk;
						sk->disconnect();//? need
						meters::socks--;
						if(meters::socks==0){
							free(evs);
							return 0;
						}
					}else{
						puts("*** unknown signal from sock caught  exiting");
						puts(msg);
						free(evs);
						return-1;
					}
				}catch(...){
					puts("*** unknown signal caught  exiting");
					free(evs);
					return-1;
				}
				if(conf::print_stats)printf("   requests: %zu  events: %zu  writes: %zu    reads: %zu \r",meters::requests,meters::events,meters::writes,meters::reads);
			}
		}
	}
};}
