f = open('output.txt','w')
import solution
test1=testc.alpha()
res = 'OK'
if test1!='abcdefg':
    res = 'NO'
f.write(test1)
f.write(res)
