.PHONY: run
run: PassportProcessing.class Passport.class example.txt input.txt
	java PassportProcessing example.txt
	java PassportProcessing input.txt

.PHONY: test
test: PassportProcessing.class Passport.class example.txt example.out input.txt input.out
	java PassportProcessing example.txt | diff - example.out
	java PassportProcessing input.txt | diff - input.out

Passport.class PassportProcessing.class: PassportProcessing.java
	javac PassportProcessing.java
