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
@aux0	dd ?

.code
start:
FLD 1.
FSTP _a
FLD 2.
FSTP _b
MOV ax, _b
ADD ax, _a
MOV @aux0, ax
JO @LABEL_OVERFLOW
FLD @aux0
FSTP _a
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
