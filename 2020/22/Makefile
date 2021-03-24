.PHONY: run
run: CrabCombat.class Player.class example.txt input.txt
	java CrabCombat example.txt
	java CrabCombat input.txt

.PHONY: test
test: CrabCombat.class Player.class example.txt example.out input.txt input.out
	java CrabCombat example.txt | diff - example.out
	java CrabCombat input.txt | diff - input.out

Player.class CrabCombat.class: CrabCombat.java
	javac CrabCombat.java
