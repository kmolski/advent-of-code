.PHONY: run
run: ComboBreaker.java
	java ComboBreaker.java

.PHONY: test
test: ComboBreaker.java input.out
	java ComboBreaker.java | diff - input.out
