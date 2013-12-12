def fib(n):
	'''
		Returns the n:th fibonacci number
		fib(0) should be 0
		fib(1) should be 1
		fib(2) should be 1
		fib(3) should be 2
	'''
	if n <= 1:
		return n
	else:
		return fib(n-1)+fib(n-2)
