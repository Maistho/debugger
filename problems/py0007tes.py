f = open('output.txt','w')
while True:
    try:
        import solution
    except Exception as inst:
        f.write(inst.args)
        f.close()
        break
    else:
        test1=testc.alpha(["shave"])
        test2=testc.alpha(["ymuum"])
        res = 'OK'
        if test1!=[[[],[[],'cave']]]:
            res = 'NO'    
        if test2!=[[[],[[],'cuum']]]:
            res = 'NO'
        f.write(str(test1))
        f.write(str(test2))
        f.write(res)
        f.close()
        break
