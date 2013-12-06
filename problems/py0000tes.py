f = open('output.txt','w')
import solution
test1=testc.alpha(3)
test2=testc.alpha(5)
test3=testc.alpha(7)
res = 'OK'
if test1!=10:
    res = 'NO'    
if test2!=10:
    res = 'NO'
if test3!=10:
    res = 'NO'
f.write(test1)
f.write(test2)
f.write(test3)
f.write(res)
