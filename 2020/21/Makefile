.PHONY: run
run: AllergenAssessment.class Food.class example.txt input.txt
	java AllergenAssessment example.txt
	java AllergenAssessment input.txt

.PHONY: test
test: AllergenAssessment.class Food.class example.txt example.out input.txt input.out
	java AllergenAssessment example.txt | diff - example.out
	java AllergenAssessment input.txt | diff - input.out

Food.class AllergenAssessment.class: AllergenAssessment.java
	javac AllergenAssessment.java
