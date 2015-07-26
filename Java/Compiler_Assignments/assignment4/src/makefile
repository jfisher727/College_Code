JAVAC := java
JAVAC := javac
JAVACC:= ../bin/javacc
JFLAGS:=

default	:	compiler

compiler :      parse.jar compile

compile : 		compile.sh
	chmod u+x compile.sh

parse.jar	:  parser/*.class main/Parse.class
	-@/bin/rm parse.mf
	echo "Main-Class: main.Parse" > parse.mf
	jar cvmf parse.mf $@ error/*.class parser/*.class main/*.class
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