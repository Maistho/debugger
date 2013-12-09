#! /usr/bin/env python3.3

def alpha(n):
    c = "palla"
    a = []
    for s in n:
        a.append(s[0])
    return a
    
a = alpha(['bc','cd','de','ef'])
#desired output is ['b','c','d','e']
print(a)
