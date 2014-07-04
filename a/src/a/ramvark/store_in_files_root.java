package a.ramvark;

import b.path;
import b.req;

public class store_in_files_root extends store_in_files{
	@Override protected path root(final Class<? extends itm>cls){return req.get().session().path(cls.getName());}
}
