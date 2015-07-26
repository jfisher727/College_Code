.global start
.align 8
start:
Factorial$main$preludeEnd:
set 10, %o0
call Fac$ComputeFac
nop
mov Fac$ComputeFac, %l1
mov %l1, %o0
call println
nop
ba Factorial$main$epilogBegin
nop
Factorial$main$epilogBegin:
call exit_program
Fac$ComputeFac:
save %sp, -96 & -8, %sp
Fac$ComputeFac$preludeEnd:
cmp %i1, 1
bl if$then001
nop
if$else002:
sub %fp, 4, %fp
mov %fp, %l2
mov %i1, %l3
sub %i1, 1, %i1
mov %i1, %o0
call Fac$ComputeFac
nop
mov Fac$ComputeFac, %l4
smul %l3, %l4, %l3
mov %l2, %l3
ba if$join003
nop
if$join003:
mov %fp, %0
ba Fac$ComputeFac$epilogBegin
nop
if$then001:
sub %fp, 4, %fp
set 1, %l6
mov %fp, %l6
ba if$join003
nop
Fac$ComputeFac$epilogBegin:
ret
restore
