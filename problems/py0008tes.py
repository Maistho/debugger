f = open('output.txt','w')
while True:
    try:
        import solution
    except Exception as inst:
        f.write(inst.args)
        f.close()
        break
    else:
        test1=testc.alpha(1)
        test2=testc.alpha(2)
        test3=testc.alpha(4)
        res = 'OK'
        if test1!=128128:
            res = 'NO'    
        if test2!=128128:
            res = 'NO'
        if test3!=128128:
            res = 'NO'
        f.write(test1)
        f.write(test2)
        f.write(test3)
        f.write(res)
        f.close()
        break
