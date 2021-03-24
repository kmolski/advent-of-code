.PHONY: run
run: ShuttleSearch.java example.txt input.txt
	java ShuttleSearch.java example.txt
	java ShuttleSearch.java input.txt

.PHONY: test
test: ShuttleSearch.java example.txt example.out input.txt input.out
	java ShuttleSearch.java example.txt | diff - example.out
	java ShuttleSearch.java input.txt | diff - input.out
