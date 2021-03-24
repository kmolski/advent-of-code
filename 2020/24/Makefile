.PHONY: run
run: LobbyLayout.java example.txt input.txt
	java LobbyLayout.java example.txt
	java LobbyLayout.java input.txt

.PHONY: test
test: LobbyLayout.java example.txt example.out input.txt input.out
	java LobbyLayout.java example.txt | diff - example.out
	java LobbyLayout.java input.txt | diff - input.out
