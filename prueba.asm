; \masm32\bin\ml /c /Zd /coff 
; \masm32\bin\Link /SUBSYSTEM:CONSOLE 
.386
.model flat, stdcall
option casemap :none

;------------includes------------
include \masm32\include\windows.inc
include \masm32\macros\macros.asm
include \masm32\include\masm32.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
include \masm32\include\gdi32.inc

;------------librerias------------
includelib \masm32\lib\masm32.lib
includelib \masm32\lib\gdi32.lib
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib

FUNCPROTO       TYPEDEF PROTO
FUNCPTR         TYPEDEF PTR FUNCPROTO
;__________________________VARIABLES____________________________
.data 
HelloWorld db "Hello freak bitches", 0
overflow db "Ha ocurrido Overflow" , 0
divideZero db "Se ha intentado dividir por cero" , 0
aux_mem_2bytes dw ?
perdidaInfo db "Se ha producido perdida de informacion" , 0
_parametro	dw ?
_b	dw ?
_funcion1	dw ?
_a	dw ?
;______________________VARIABLES AUXILIARES____________________
var_aux_dx dw ?
@0 dw 0
@aux0	dw ?

;_____________________________CODE_____________________________
.code
start:
main proc 
	 MOV ax ,2              ;-------------  ASIG USINT ---- (_a:=2) 
	 MOV _a, ax
	 MOV ax ,10             ;-------------  ASIG USINT ---- (_b:=10) 
	 MOV _b, ax
	 print chr$("'main1'", 13,10) 
	 print chr$("'main2'", 13,10) 
	 CALL _funcion1F 
	 MOV ax ,_funcion1      ;-------------  ASIG USINT ---- (_a:=_funcion1) 
	 MOV _a, ax
	 MOV ax, _a             ;-------------  COMP USINT ---- (_a comp 4)
	 CMP ax, 4
	 JNE @Label_0 
;==================[THEN]==================
	 print chr$("'a == 4'", 13,10) 
	 JMP @Label_1 
;==================[ELSE/FIN]==================
@Label_0:
;==================[FIN IF]==================
@Label_1:
	 print chr$("'fin'", 13,10) 
main endp 
JMP @LABEL_END 
 
_funcion1F proc 
	 MOV ax ,1              ;-------------  ASIG USINT ---- (_parametro:=1) 
	 MOV _parametro, ax
	 print chr$("'fun1'", 13,10) 
	 MOV ax, _parametro     ;-------------  ADD USINT ---- (_parametro+_a) 
	 ADD ax, _a
	 MOV @aux0, ax
	 JO @LABEL_OVERFLOW
	 MOV ax ,@aux0          ;-------------  ASIG USINT ---- (_funcion1:=@aux0) 
	 MOV _funcion1, ax
ret 
_funcion1F endp 
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
