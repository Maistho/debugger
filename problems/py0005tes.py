f = open('output.txt','w')
while True:
    try:
        import solution
    except Exception as inst:
        f.write(inst.args)
        f.close()
        break
    else:
        test1=testc.alpha(3)
        res = 'OK'
        if test1!=15:
            res = 'NO'    
        f.write(test1)
        f.write(test2)
        f.write(test3)
        f.write(res)
        f.close()
        break
