.PHONY: run
run: CustomCustoms.java example.txt input.txt
	java CustomCustoms.java example.txt
	java CustomCustoms.java input.txt

.PHONY: test
test: CustomCustoms.java example.txt example.out input.txt input.out
	java CustomCustoms.java example.txt | diff - example.out
	java CustomCustoms.java input.txt | diff - input.out
