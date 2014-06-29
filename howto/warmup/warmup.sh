NU=100  # number of concurrent users
TS=30   # number of seconds to run

date
ab -c $NU -t $TS http://www.ramvark.net/
ab -c $NU -t $TS http://www.ramvark.net/qa/t001.txt
ab -c $NU -t $TS http://www.ramvark.net/typealine
ab -c $NU -t $TS http://www.ramvark.net/qa.t013
#ab -c $NU -t $TS http://www.ramvark.net/ramvark
date

