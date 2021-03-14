.PHONY: run
run: BinaryBoarding.java example.txt input.txt
	java BinaryBoarding.java example.txt
	java BinaryBoarding.java input.txt

.PHONY: test
test: BinaryBoarding.java example.txt example.out input.txt input.out
	java BinaryBoarding.java example.txt | diff - example.out
	java BinaryBoarding.java input.txt | diff - input.out
