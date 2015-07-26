JAVAD := /software/solaris/compilers/java/j2sdk1.4.0/j2se/bin
JAVAC := $(JAVAD)/javac
JAVA  := $(JAVAD)/java
JAVACC:= ../../javacc/javacc-3.2/bin/javacc
JFLAGS:=

default	:	compiler

compiler :      compiler.jar assemble

assemble : 		assemble.sh
	chmod u+x assemble.sh

compiler.jar	:  parser/*.class main/Parse.class
	-@/bin/rm parse.mf
	echo "Main-Class: main.Parse" > parse.mf
	jar cvmf compiler.mf $@ error/*.class parser/*.class main/*.class
	-@/bin/rm parse.mf

error/ErrorMsg.class	:  error/ErrorMsg.java
	$(JAVAC) $<

main/Parse.class : main/Parse.java
	javac main/Parse.java

parser/*.class : parser/*.java
	javac parser/*.java

clean	:
	-/bin/rm *~ */*~
	-/bin/rm */*.class