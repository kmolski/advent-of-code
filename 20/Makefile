.PHONY: run
run: JurassicJigsaw.class ImageTile.class StringUtils.class example.txt input.txt
	java JurassicJigsaw example.txt
	java JurassicJigsaw input.txt

.PHONY: test
test: JurassicJigsaw.class ImageTile.class StringUtils.class example.txt example.out input.txt input.out
	java JurassicJigsaw example.txt | diff - example.out
	java JurassicJigsaw input.txt | diff - input.out

ImageTile.class StringUtils.class JurassicJigsaw.class: JurassicJigsaw.java
	javac JurassicJigsaw.java
