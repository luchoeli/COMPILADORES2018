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
_d	dd ?
_c	dd ?
_b	dw ?
_a	dw ?
@aux0	dw ?

.code
start:
MOV ax, 1
MOV _a, ax
MOV ax, 2
MOV _b, ax
MOV ax, 1
SUB ax, 2
MOV @aux0, ax
JS @LABEL_PERDIDAINFO:
MOV ax, @aux0
MOV _a, ax
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
