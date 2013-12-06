f = open('output.txt','w')
while True:
    try:
        import solution
    except Exception as inst:
        f.write(inst.args)
        f.close()
        break
    else:
        test1=testc.alpha(['bc','cd','de','ef'])
        test2=testc.alpha(['vbc','zcd','xde','fef'])
        res = 'OK'
        if test1!=['b','c','d','e']:
            res = 'NO'    
        if test2!=['v','z','x','f']:
            res = 'NO'
        f.write(str(test1))
        f.write(str(test2))
        f.write(res)
        f.close()
        break
