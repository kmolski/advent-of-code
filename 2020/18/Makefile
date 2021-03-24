.PHONY: run
run: OperationOrder.java example.txt input.txt
	java OperationOrder.java example.txt
	java OperationOrder.java input.txt

.PHONY: test
test: OperationOrder.java example.txt example.out input.txt input.out
	java OperationOrder.java example.txt | diff - example.out
	java OperationOrder.java input.txt | diff - input.out
