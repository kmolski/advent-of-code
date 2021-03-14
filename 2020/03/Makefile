.PHONY: run
run: TobogganTrajectory.class GridSquare.class example.txt input.txt
	java TobogganTrajectory example.txt
	java TobogganTrajectory input.txt

.PHONY: test
test: TobogganTrajectory.class GridSquare.class example.txt example.out input.txt input.out
	java TobogganTrajectory example.txt | diff - example.out
	java TobogganTrajectory input.txt | diff - input.out

GridSquare.class TobogganTrajectory.class: TobogganTrajectory.java
	javac TobogganTrajectory.java
