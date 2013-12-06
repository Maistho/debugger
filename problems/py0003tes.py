f = open('output.txt','w')
import solution
test1=testc.alpha(1)
test2=testc.alpha('a')
test3=testc.alpha(2)
res = 'OK'
if test1!='""a"1""':
    res = 'NO'    
if test2!='""a"a""':
    res = 'NO'
if test3!='""a"2""':
    res = 'NO'
f.write(test1)
f.write(test2)
f.write(test3)
f.write(res)
