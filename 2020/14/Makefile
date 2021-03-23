.PHONY: run
run: DockingData.class DockingProgram.class input.txt
	java DockingData input.txt

.PHONY: test
test: DockingData.class DockingProgram.class input.txt input.out
	java DockingData input.txt | diff - input.out

DockingProgram.class DockingData.class: DockingData.java
	javac DockingData.java
