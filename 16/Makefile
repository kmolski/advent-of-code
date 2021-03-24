.PHONY: run
run: TicketTranslation.java example.txt input.txt
	java TicketTranslation.java example.txt
	java TicketTranslation.java input.txt

.PHONY: test
test: TicketTranslation.java example.txt example.out input.txt input.out
	java TicketTranslation.java example.txt | diff - example.out
	java TicketTranslation.java input.txt | diff - input.out
