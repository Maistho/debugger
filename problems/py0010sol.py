#! /usr/bin/env python3.3

def alpha(n):
    n = n + 1
    r = n*3//3
    a = 0
    for i in range(r):
        if i > 4:
            ab = i
            a = ab
    return a
    
a = alpha(6)
#desired output is 6
print(a)
