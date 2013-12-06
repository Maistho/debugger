f = open('output.txt','w')
import solution
test1=testc.alpha(2)
test2=testc.alpha(2)
test3=testc.alpha(2.5)
res = 'OK'
if test1!=256:
    res = 'NO'    
if test2!=46656:
    res = 'NO'
if test3!=3125.0:
    res = 'NO'
f.write(test1)
f.write(test2)
f.write(test3)
f.write(res)
