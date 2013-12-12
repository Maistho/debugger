#!/usr/bin/env python
'''
    Test template says:
        Print what you do. All prints get sent back to the user.
        Test with some random values.
        The very last print should be either "Success" or "Failure"
        depending on if the tests succeded or not.
'''
F = [0,1,1,2,3,5,8,13,21,34,55,89,144,233,377,610,987,1597,2584,4181,6765,10946,17711,28657,46368,75025,121393,196418,317811,514229,832040,1346269,2178309,3524578,5702887,9227465,14930352,24157817,39088169]

import solution
try:
    solution.fib(0)
except AttributeError:
    print("Could not find fib(). Have you renamed it?")
    print("Failure")
    raise SystemExit(0)


if (solution.fib(5) == F[5]) and (solution.fib(10) == F[10]) and (solution.fib(23) == F[23]) and (solution.fib(30) == F[30]):
    print("Success")
else:
    print("Failure")
