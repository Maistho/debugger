f = open('output.txt','w')
while True:
    try:
        import solution
        break
    except Exception as inst:
        f.write(inst.args)
        f.close()
    else:
        test1=testc.alpha([0,1,2])
        test2=testc.alpha([1,1,1])
        res = 'OK'
        if test1!=[0,1,2,3,4,5,6,7,8]:
            res = 'NO'    
        if test2!=[0,1,2,3,4,5,6,7,8]:
            res = 'NO'
        f.write(str(test1))
        f.write(str(test2))
        f.write(res)
        f.close()
