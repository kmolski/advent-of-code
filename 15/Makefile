.PHONY: run
run: RambunctiousRecitation.java
	java RambunctiousRecitation.java

.PHONY: test
test: RambunctiousRecitation.java input.out
	java RambunctiousRecitation.java | diff - input.out
