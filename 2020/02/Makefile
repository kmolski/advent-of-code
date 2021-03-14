.PHONY: run
run: PasswordPhilosophy.java example.txt input.txt
	java PasswordPhilosophy.java example.txt
	java PasswordPhilosophy.java input.txt

.PHONY: test
test: PasswordPhilosophy.java example.txt example.out input.txt input.out
	java PasswordPhilosophy.java example.txt | diff - example.out
	java PasswordPhilosophy.java input.txt | diff - input.out
