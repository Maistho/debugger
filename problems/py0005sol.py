#! /usr/bin/env python3.3

def alpha(n):
    om = "abcdeabcdeabcde"*2
    o = om[:15]
    l = 1
    n = om
    return len(o)


a = alpha(1337)
#desired output is 15
print(a)
