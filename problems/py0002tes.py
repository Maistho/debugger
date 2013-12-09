f = open('output.txt','w')
import solution
test1=testc.alpha(1)
test2=testc.alpha('bb')
test3=testc.alpha('c1')
res = 'OK'
if test1!='a1a1a1':
    res = 'NO'    
if test2!='abbabbabb':
    res = 'NO'
if test3!='ac1ac1ac1':
    res = 'NO'
f.write(test1)
f.write(test2)
f.write(test3)
f.write(res)
