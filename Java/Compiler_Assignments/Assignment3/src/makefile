JAVAC := java
JAVAC := javac
JAVACC:= ../bin/javacc
JFLAGS:=

default	:	compiler

compiler :      scan.jar compile

compile : 		compile.sh
	chmod u+x compile.sh

scan.jar	:  parser/*.class main/Scan.class
	-@/bin/rm mc.mf
	echo "Main-Class: main.Scan" > scan.mf
	jar cvmf scan.mf $@ error/*.class parser/*.class main/*.class
	-@/bin/rm scan.mf

error/ErrorMsg.class	:  error/ErrorMsg.java
	$(JAVAC) $<

main/Scan.class : main/Scan.java
	javac main/Scan.java

parser/*.class : parser/*.java
	javac parser/*.java

clean	:
	-/bin/rm *~ */*~
	-/bin/rm */*.class