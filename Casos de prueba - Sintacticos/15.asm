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
overflow db "Error en ejecucion: Ha ocurrido Overflow" , 0
divideZero db "Error en ejecucion:Se ha intentado dividir por cero" , 0
resultadoNegativo db "Error en ejecucion: Usinteger negativo" , 0
aux_mem_2bytes dw ?
_parametro	dw ?
_b	dw ?
_funcion1	dw ?
_a	dw ?
;______________________VARIABLES AUXILIARES____________________
var_aux_dx dw ?
@0 dw 0
@cte11	dw 11
@aux0	dw ?
@cte10	dw 10
@cte1	dw 1

;_____________________________CODE_____________________________
.code
start:
main proc 
	 MOV ax ,@cte1          ;-------------  ASIG USINT ---- (_a:=@cte1) 
	 MOV _a, ax
	 CALL _funcion1F 
	 MOV ax ,_funcion1      ;-------------  ASIG USINT ---- (_a:=_funcion1) 
	 MOV _a, ax
	 MOV ax, _a             ;-------------  COMP USINT ---- (_a comp @cte11)
	 CMP ax, @cte11
	 JNE @Label_0 
;==================[THEN]==================
	 print chr$("'se asigno bien'", 13,10) 
	 JMP @Label_1 
;==================[ELSE/FIN]==================
@Label_0:
;==================[FIN IF]==================
@Label_1:
main endp 
JMP @LABEL_END 
 
_funcion1F proc 
	 MOV ax ,@cte10         ;-------------  ASIG USINT ---- (_parametro:=@cte10) 
	 MOV _parametro, ax
	 print chr$("'cadena'", 13,10) 
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
@LABEL_RESULTADO:
invoke MessageBox, NULL, addr resultadoNegativo, addr resultadoNegativo, MB_OK
@LABEL_END:
invoke ExitProcess, 0
end start
