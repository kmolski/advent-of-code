.PHONY: run
run: HandyHaversacks.class Haversack.class example.txt input.txt
	java HandyHaversacks example.txt
	java HandyHaversacks input.txt

.PHONY: test
test: HandyHaversacks.class Haversack.class example.txt example.out input.txt input.out
	java HandyHaversacks example.txt | diff - example.out
	java HandyHaversacks input.txt | diff - input.out

Haversack.class HandyHaversacks.class: HandyHaversacks.java
	javac HandyHaversacks.java
