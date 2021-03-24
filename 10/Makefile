.PHONY: run
run: AdapterArray.java example.txt input.txt
	java AdapterArray.java example.txt
	java AdapterArray.java input.txt

.PHONY: test
test: AdapterArray.java example.txt example.out input.txt input.out
	java AdapterArray.java example.txt | diff - example.out
	java AdapterArray.java input.txt | diff - input.out
