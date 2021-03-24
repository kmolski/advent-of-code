# [Day 18: Operation Order](https://adventofcode.com/2020/day/18) solution

## Expected result
```
$ make run
java OperationOrder.java example.txt
Part 1: 26335
Part 2: 693891
java OperationOrder.java input.txt
Part 1: 510009915468
Part 2: 321176691637769
$ make test
java OperationOrder.java example.txt | diff - example.out
java OperationOrder.java input.txt | diff - input.out
```

## Tested on
```
$ java --version
openjdk 11.0.10 2021-01-19
OpenJDK Runtime Environment (build 11.0.10+9)
OpenJDK 64-Bit Server VM (build 11.0.10+9, mixed mode)
```

## Resources
* [Shunting-yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)
