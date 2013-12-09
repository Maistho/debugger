#! /usr/bin/env python3.3

def alpha(n):
    b = n[1:]
    b[-1:] = []
    c = b+b
    return c

a = alpha(['b','a','m','b','i'])
#desired output is ['a','m','b','a','m','b']
print(a)
