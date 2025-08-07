WARP program for graph Example4
Scheduler Name: Priority
numFaults: 1
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D
0	if has(F0) push(F0: A -> B, #9)	wait(#9)	sleep	sleep
1	wait(#14)	if has(F0) push(F0: B -> C, #14) else pull(F0: A -> B, #14)	wait(#14)	sleep
2	sleep	wait(#13)	if has(F0) push(F0: C -> D, #13) else pull(F0: B -> C, #13)	wait(#13)
3	sleep	sleep	if has(F0) push(F0: C -> D, #14)	wait(#14)
4	sleep	wait(#1)	if has(F1) push(F1: C -> B, #1)	sleep
5	wait(#2)	if has(F1) push(F1: B -> A, #2) else pull(F1: C -> B, #2)	wait(#2)	sleep
6	wait(#3)	if has(F1) push(F1: B -> A, #3)	sleep	sleep
7	sleep	sleep	sleep	sleep
8	sleep	sleep	sleep	sleep
9	sleep	sleep	sleep	sleep
10	if has(F0) push(F0: A -> B, #11)	wait(#11)	sleep	sleep
11	wait(#0)	if has(F0) push(F0: B -> C, #0) else pull(F0: A -> B, #0)	wait(#0)	sleep
12	sleep	wait(#15)	if has(F0) push(F0: C -> D, #15) else pull(F0: B -> C, #15)	wait(#15)
13	sleep	sleep	if has(F0) push(F0: C -> D, #0)	wait(#0)
14	sleep	sleep	sleep	sleep
15	sleep	sleep	sleep	sleep
16	sleep	sleep	sleep	sleep
17	sleep	sleep	sleep	sleep
18	sleep	sleep	sleep	sleep
19	sleep	sleep	sleep	sleep
// All flows meet their deadlines
