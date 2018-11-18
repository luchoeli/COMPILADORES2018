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
_d	dw ?
_c	dw ?
_b	dq ?
_a	dq ?
var_aux_dx dw ?
@0 dw 0
@aux0	dd ?

.code
start:
;-------------  ASIG DOUBLE ---- (_a:=1.)
FLD 1.
FSTP _a
;-------------  COMP DOUBLE ---- (_a comp 2.)
FLD 2.
FLD _a
FCOM
FSTSW aux_mem_2bytes
MOV AX , aux_mem_2bytes
SAHF
JG @Label0 ELSE if 1
;--------------------------------------------------------------------------[THEN]----------
;-------------  ASIG DOUBLE ---- (_b:=3.)
FLD 3.
FSTP _b
;-------------  COMP USINT ---- (_c comp 4)
MOV ax, _c
CMP ax, 4
JBE @Label1 else if 2
;--------------------------------------------------------------------------[THEN]----------
;-------------  ASIG USINT ---- (_d:=5)
MOV ax, 5
MOV _d, ax
JMP @Label2 
@Label1 
;--------------------------------------------------------------------------[ELSE/FIN]----------
@Label2 
JMP @Label3 
@Label0 
;--------------------------------------------------------------------------[ELSE/FIN]----------
;-------------  MULT DOUBLE ---- (2.3*3.5)
FLD 2.3
FMUL 3.5
FSTP @aux0
;-------------  ASIG DOUBLE ---- (_a:=@aux0)
FLD @aux0
FSTP _a
@Label3 
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
