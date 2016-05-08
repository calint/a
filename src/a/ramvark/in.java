package a.ramvark;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
public @Retention(RetentionPolicy.RUNTIME)@interface in{
	public final static int TYPE_AGGR_N=3;
	public final static int TYPE_AGGR_1=8;
	public final static int TYPE_REF_N=7;
	public final static int TYPE_REF_1=9;
	public final static int TYPE_STR=0;
	int type()default TYPE_STR;
	//type 0:text field
	//     1:textarea
	//     3:aggregate many
	//     4:editor
	//     5:number
	//     6:input list orderable
	//     7:refn
	//     8:aggr1
	Class<? extends lst>lst()default lst.class;
	Class<? extends itm>cls()default itm.class;
	boolean must()default false;
}
