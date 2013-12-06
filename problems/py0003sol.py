#! /usr/bin/env python3.3

def alpha(n):
    d = '""'
    b = d+"a"+'"'+str(n)+d
    return b


a = alpha(1)
#desired output is ""a"1""
print(a)
