#pragma once
#include"doc.hpp"
#include"sessions.hpp"
#include"widget.hpp"
#include<memory>
namespace xiinux{// shared by server and sock to avoid circular ref
	static int epollfd;
	std::unique_ptr<doc>homepage;  //? delete not triggered at ctrl+c
	static widget*widgetget(const char*qs);
}
