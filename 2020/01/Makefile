.PHONY: run
run: ReportRepair.java example.txt input.txt
	java ReportRepair.java example.txt
	java ReportRepair.java input.txt

.PHONY: test
test: ReportRepair.java example.txt example.out input.txt input.out
	java ReportRepair.java example.txt | diff - example.out
	java ReportRepair.java input.txt | diff - input.out
