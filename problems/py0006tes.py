f = open('output.txt','w')
while True:
    try:
        import solution
    except Exception as inst:
        f.write(inst.args)
        f.close()
        break
    else:
        test1=testc.alpha(['b','a','m','b','i'])
        test2=testc.alpha(['b','u','m','b','i'])
        test3=testc.alpha(['b','a','n','b','i'])
        res = 'OK'
        if test1!=['a','m','b','a','m','b']:
            res = 'NO'    
        if test2!=['u','m','b','u','m','b']:
            res = 'NO'
        if test3!=['a','n','b','a','n','b']:
            res = 'NO'
        f.write(str(test1))
        f.write(str(test2))
        f.write(str(test3))
        f.write(res)
        f.close()
        break
