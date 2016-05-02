#!/usr/bin/python3

n = int(input())

for _ in range(n):
    addr = [int(i) for i in input().split('.')]
    mask = [int(i) for i in input().split('.')]

    ip = [a & b for (a, b) in zip(addr, mask)]

    print("IP:\t\t", ip)
    print("MASK:\t\t", mask)
    print("NUM OF ADDR:\t", (~ (-256 | mask[-1])) - 1)
    print("START ADDR:\t", ip[:-1] + [ip[-1] | 1])
    print("END ADDR:\t", ip[:-1] + [ip[-1] | (((~(-256 | mask[-1])) - 1 ) )])
    print("\n", "-"*50, "\n")
