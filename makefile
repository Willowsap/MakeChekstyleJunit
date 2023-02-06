default: 
	@echo "usage: make target"
	@echo "2 available targets:"
	@echo "____________________ compile - builds codebase"
	@echo "____________________ clean - removes .class files from the bin directory"

compile:
	javac -d bin -sourcepath src src/**/**/*.java

clean:
	rm -f ./bin/**/*.class
