.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
HelloWorld db "Hello freak bitches", 0
overflow db "Ha ocurrido Overflow" , 0
divideZero db "Se ha intentado dividir por cero" , 0
aux_mem_2bytes dw ?
perdidaInfo db "Se ha producido perdida de informacion" , 0
_b	dd ?
_a	dd ?
var_aux_dx dw ?
@0 dw 0

.code
start:
;-------------  COMP DOUBLE ---- (_a comp 2.)
FLD 2.
FLD _a
FCOM
FSTSW aux_mem_2bytes
MOV AX , aux_mem_2bytes
SAHF
;-------------  ASIG DOUBLE ---- (_a:=2.23E+2)
FLD 2.23E+2
FSTP _a
;-------------  ASIG DOUBLE ---- (_b:=65.)
FLD 65.
FSTP _b
invoke HelloWorld, NULL, addr HelloWorld,addr HelloWorld,MB_OK
JMP @LABEL_END
@LABEL_OVERFLOW:
invoke MessageBox, NULL, addr overflow, addr overflow, MB_OK
JMP @LABEL_END
@LABEL_DIVIDEZERO:
invoke MessageBox, NULL, addr divideZero, addr divideZero, MB_OK
JMP @LABEL_END
@LABEL_PERDIDAINFO:
invoke MessageBox, NULL, addr perdidaInfo, addr perdidaInfo, MB_OK
JMP @LABEL_END
@LABEL_END:
invoke ExitProcess, 0
end start
