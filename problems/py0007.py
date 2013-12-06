#! /usr/bin/env python3.3

def alpha(n):
    n = "c"+n[0][2]
    c = [[],[],[]]
    c[0][1].append(n)
    return c

a = alpha(["shave"])
#desired output is [[[],[[],'cave']]]
print(a)
