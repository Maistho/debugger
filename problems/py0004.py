#! /usr/bin/env python3.3

def alpha(n):
    m = "fg"
    n[3] = "d"
    n = n+m
    return n


a = alpha("abca")
#desired output is abcdefg
print(a)
