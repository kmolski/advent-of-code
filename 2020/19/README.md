# [Day 19: Monster Messages](https://adventofcode.com/2020/day/19) solution

## Expected result
```
$ make run
java MonsterMessages example.txt
Part 1: 2
Part 2: 2
java MonsterMessages input.txt
Part 1: 226
Part 2: 355
$ make test
java MonsterMessages example.txt | diff - example.out
java MonsterMessages input.txt | diff - input.out
```

## Tested on
```
$ java --version
openjdk 11.0.10 2021-01-19
OpenJDK Runtime Environment (build 11.0.10+9)
OpenJDK 64-Bit Server VM (build 11.0.10+9, mixed mode)
```

## Resources
* [Parser combinator](https://en.wikipedia.org/wiki/Parser_combinator)
* [Functional Parsing - Computerphile](https://www.youtube.com/watch?v=dDtZLm7HIJs)
