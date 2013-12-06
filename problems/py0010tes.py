f = open('output.txt','w')
while True:
    try:
        import solution
    except Exception as inst:
        f.write(inst.args)
        f.close()
        break
    else:
        test1=testc.alpha(6)
        test2=testc.alpha(12)
        test3=testc.alpha(18)
        res = 'OK'
        if test1!=6:
            res = 'NO'    
        if test2!=12:
            res = 'NO'
        if test3!=18:
            res = 'NO'
        f.write(test1)
        f.write(test2)
        f.write(test3)
        f.write(res)
        f.close()
        break
