WARP program for graph Example1A
Scheduler Name: PosetRM
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C
0	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0)	sleep
1	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)
2	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)
3	sleep	if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)
4	sleep	if !has(F1: C -> B) pull(F1: C -> B, #0)	wait(#0)
5	wait(#0)	if !has(F1: C -> B) pull(F1: C -> B, #0) else if has(F1: B -> A) push(F1: B -> A, #0)	wait(#0)
6	wait(#0)	if !has(F1: C -> B) pull(F1: C -> B, #0) else if has(F1: B -> A) push(F1: B -> A, #0)	wait(#0)
7	wait(#0)	if has(F1: B -> A) push(F1: B -> A, #0)	sleep
8	sleep	sleep	sleep
9	sleep	sleep	sleep
10	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0)	sleep
11	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)
12	wait(#0)	if !has(F0: A -> B) pull(F0: A -> B, #0) else if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)
13	sleep	if has(F0: B -> C) push(F0: B -> C, #0)	wait(#0)
14	sleep	sleep	sleep
15	sleep	sleep	sleep
16	sleep	sleep	sleep
17	sleep	sleep	sleep
18	sleep	sleep	sleep
19	sleep	sleep	sleep
// All flows meet their deadlines
