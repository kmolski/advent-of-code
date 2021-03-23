.PHONY: run
run: SeatingSystem.class FerrySquare.class example.txt input.txt
	java SeatingSystem example.txt
	java SeatingSystem input.txt

.PHONY: test
test: SeatingSystem.class FerrySquare.class example.txt example.out input.txt input.out
	java SeatingSystem example.txt | diff - example.out
	java SeatingSystem input.txt | diff - input.out

FerrySquare.class SeatingSystem.class: SeatingSystem.java
	javac SeatingSystem.java
