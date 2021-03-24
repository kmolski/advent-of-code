.PHONY: run
run: EncodingError.java input.txt
	java EncodingError.java input.txt

.PHONY: test
test: EncodingError.java input.txt input.out
	java EncodingError.java input.txt | diff - input.out
