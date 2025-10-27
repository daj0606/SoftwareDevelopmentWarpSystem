WARP program for graph Example1A
Scheduler Name: WarpPosetRM
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C
0	if has(F0: A -> B) push(F0: A -> B, #0)	wait(#0)	sleep
1	if has(F0: A -> B) push(F0: A -> B, #0)	wait(#0)	sleep
2	if has(F0: A -> B) push(F0: A -> B, #0)	wait(#0)	sleep
3	sleep	wait(#0)	if !has(F0: B -> C) pull(F0: B -> C, #0)
4	sleep	wait(#0)	if !has(F0: B -> C) pull(F0: B -> C, #0)
5	sleep	wait(#0)	if !has(F0: B -> C) pull(F0: B -> C, #0)
6	sleep	wait(#0)	if has(F1: C -> B) push(F1: C -> B, #0)
7	sleep	wait(#0)	if has(F1: C -> B) push(F1: C -> B, #0)
8	sleep	wait(#0)	if has(F1: C -> B) push(F1: C -> B, #0)
9	sleep	sleep	sleep
10	if has(F0: A -> B) push(F0: A -> B, #0)	wait(#0)	sleep
11	if has(F0: A -> B) push(F0: A -> B, #0)	wait(#0)	sleep
12	if has(F0: A -> B) push(F0: A -> B, #0)	wait(#0)	sleep
13	sleep	wait(#0)	if !has(F0: B -> C) pull(F0: B -> C, #0)
14	sleep	wait(#0)	if !has(F0: B -> C) pull(F0: B -> C, #0)
15	sleep	wait(#0)	if !has(F0: B -> C) pull(F0: B -> C, #0)
16	if !has(F1: B -> A) pull(F1: B -> A, #0)	wait(#0)	sleep
17	if !has(F1: B -> A) pull(F1: B -> A, #0)	wait(#0)	sleep
18	if !has(F1: B -> A) pull(F1: B -> A, #0)	wait(#0)	sleep
19	sleep	sleep	sleep
// All flows meet their deadlines
