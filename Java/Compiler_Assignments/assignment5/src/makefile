JAVAC := java
JAVAC := javac
JAVACC:= ../bin/javacc
SUPPORT:= support.jar
JFLAGS:=

default	:	compiler

compiler :      check.jar compile

compile : 		compile.sh
	chmod u+x compile.sh

check.jar	:  parser/*.class main/Parse.class
	-@/bin/rm check.mf
	echo "Main-Class: main.Parse" > parse.mf
	jar cvmf parse.mf $@ error/*.class parser/*.class main/*.class
	-@/bin/rm check.mf

error/ErrorMsg.class	:  error/ErrorMsg.java
	$(JAVAC) $<

main/Parse.class : main/Parse.java
	javac main/Parse.java

parser/*.class : parser/*.java
	javac parser/*.java

clean	:
	-/bin/rm *~ */*~
	-/bin/rm */*.class