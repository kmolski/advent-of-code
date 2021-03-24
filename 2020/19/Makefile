.PHONY: run
run: MonsterMessages.class Rule.class CharacterRule.class CompositeRule.class example.txt input.txt
	java MonsterMessages example.txt
	java MonsterMessages input.txt

.PHONY: test
test: MonsterMessages.class Rule.class CharacterRule.class CompositeRule.class example.txt example.out input.txt input.out
	java MonsterMessages example.txt | diff - example.out
	java MonsterMessages input.txt | diff - input.out

Rule.class CharacterRule.class CompositeRule.class MonsterMessages.class: MonsterMessages.java
	javac MonsterMessages.java
