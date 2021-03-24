.PHONY: run
run: ConwayCubes.class Coordinates.class example.txt input.txt
	java ConwayCubes example.txt
	java ConwayCubes input.txt

.PHONY: test
test: ConwayCubes.class Coordinates.class example.txt example.out input.txt input.out
	java ConwayCubes example.txt | diff - example.out
	java ConwayCubes input.txt | diff - input.out

Coordinates.class ConwayCubes.class: ConwayCubes.java
	javac ConwayCubes.java
