WARP program for graph Example3
Scheduler Name: Priority
M: 0.9
E2E: 0.99
nChannels: 16
Time Slot	A	B	C	D	F	G	I	P	R	V	X
0	if has(F0) push(F0: A -> B, #1)	wait(#1)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
1	wait(#2)	if has(F0) push(F0: B -> C, #2) else pull(F0: A -> B, #2)	wait(#2)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
2	wait(#3)	if has(F0) push(F0: B -> C, #3) else pull(F0: A -> B, #3)	wait(#3)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
3	sleep	if has(F0) push(F0: B -> C, #4)	wait(#4)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
4	sleep	wait(#1)	if has(F1) push(F1: C -> B, #1)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
5	wait(#5)	if has(F1) push(F1: B -> A, #5) else pull(F1: C -> B, #5)	wait(#5)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
6	wait(#6)	if has(F1) push(F1: B -> A, #6) else pull(F1: C -> B, #6)	wait(#6)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
7	wait(#7)	if has(F1) push(F1: B -> A, #7)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
8	sleep	wait(#1)	sleep	if has(F2) push(F2: D -> B, #1)	sleep	sleep	sleep	sleep	sleep	sleep	sleep
9	sleep	if has(F2) push(F2: B -> F, #8) else pull(F2: D -> B, #8)	sleep	wait(#8)	wait(#8)	sleep	sleep	sleep	sleep	sleep	sleep
10	sleep	if has(F2) push(F2: B -> F, #9) else pull(F2: D -> B, #9)	sleep	wait(#9)	wait(#9)	sleep	sleep	sleep	sleep	sleep	sleep
11	sleep	if has(F2) push(F2: B -> F, #10)	sleep	sleep	wait(#10)	sleep	sleep	sleep	sleep	sleep	sleep
12	sleep	wait(#1)	sleep	sleep	sleep	sleep	sleep	if has(F3) push(F3: P -> B, #1)	sleep	sleep	sleep
13	sleep	if has(F3) push(F3: B -> R, #11) else pull(F3: P -> B, #11)	sleep	sleep	sleep	sleep	sleep	wait(#11)	wait(#11)	sleep	sleep
14	sleep	if has(F3) push(F3: B -> R, #12) else pull(F3: P -> B, #12)	sleep	sleep	sleep	sleep	sleep	wait(#12)	wait(#12)	sleep	sleep
15	sleep	if has(F3) push(F3: B -> R, #13)	sleep	sleep	sleep	sleep	sleep	sleep	wait(#13)	sleep	sleep
16	sleep	wait(#1)	sleep	sleep	sleep	if has(F4) push(F4: G -> B, #1)	sleep	sleep	sleep	sleep	sleep
17	sleep	if has(F4) push(F4: B -> I, #14) else pull(F4: G -> B, #14)	sleep	sleep	sleep	wait(#14)	wait(#14)	sleep	sleep	sleep	sleep
18	sleep	if has(F4) push(F4: B -> I, #15) else pull(F4: G -> B, #15)	sleep	sleep	sleep	wait(#15)	wait(#15)	sleep	sleep	sleep	sleep
19	sleep	if has(F4) push(F4: B -> I, #0)	sleep	sleep	sleep	sleep	wait(#0)	sleep	sleep	sleep	sleep
20	sleep	wait(#1)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	if has(F5) push(F5: V -> B, #1)	sleep
21	sleep	if has(F5) push(F5: B -> X, #2) else pull(F5: V -> B, #2)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	wait(#2)	wait(#2)
22	sleep	if has(F5) push(F5: B -> X, #3) else pull(F5: V -> B, #3)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	wait(#3)	wait(#3)
23	sleep	if has(F5) push(F5: B -> X, #4)	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	wait(#4)
24	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
25	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
26	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
27	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
28	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
29	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
30	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
31	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
32	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
33	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
34	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
35	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
36	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
37	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
38	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
39	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
40	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
41	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
42	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
43	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
44	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
45	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
46	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
47	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
48	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
49	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
50	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
51	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
52	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
53	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
54	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
55	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
56	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
57	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
58	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
59	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
60	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
61	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
62	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
63	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
64	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
65	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
66	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
67	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
68	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
69	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
70	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
71	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
72	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
73	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
74	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
75	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
76	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
77	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
78	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
79	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
80	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
81	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
82	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
83	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
84	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
85	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
86	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
87	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
88	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
89	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
90	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
91	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
92	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
93	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
94	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
95	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
96	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
97	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
98	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
99	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep	sleep
// All flows meet their deadlines
