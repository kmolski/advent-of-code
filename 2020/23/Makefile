.PHONY: run
run: CrabCups.java
	java CrabCups.java

.PHONY: test
test: CrabCups.java input.out
	java CrabCups.java | diff - input.out
