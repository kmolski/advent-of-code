.PHONY: run
run: RainRisk.class Ferry.class example.txt input.txt
	java RainRisk example.txt
	java RainRisk input.txt

.PHONY: test
test: RainRisk.class Ferry.class example.txt example.out input.txt input.out
	java RainRisk example.txt | diff - example.out
	java RainRisk input.txt | diff - input.out

Ferry.class RainRisk.class: RainRisk.java
	javac RainRisk.java
