.PHONY: run
run: HandheldHalting.class Console.class example.txt input.txt
	java HandheldHalting example.txt
	java HandheldHalting input.txt

.PHONY: test
test: HandheldHalting.class Console.class example.txt example.out input.txt input.out
	java HandheldHalting example.txt | diff - example.out
	java HandheldHalting input.txt | diff - input.out

Console.class HandheldHalting.class: HandheldHalting.java
	javac HandheldHalting.java
