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
_funcion1	dq ?
_a	dq ?
_parametro	dq ?
;______________________VARIABLES AUXILIARES____________________
var_aux_dx dw ?
@0 dw 0
@cte3$p2	dq 3.2

;_____________________________CODE_____________________________
.code
start:
main proc 
	 CALL _funcion1F 
	 FLD _funcion1          ;-------------  ASIG DOUBLE ---- (_a:=_funcion1) 
	 FSTP _a
	 FLD @cte3$p2           ;-------------  COMP DOUBLE ---- (_a comp @cte3$p2)
	 FLD _a
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JNE @Label_0 
;==================[THEN]==================
	 print chr$("'es igual'", 13,10) 
	 JMP @Label_1 
;==================[ELSE/FIN]==================
@Label_0:
;==================[FIN IF]==================
@Label_1:
main endp 
JMP @LABEL_END 
 
_funcion1F proc 
	 FLD @cte3$p2           ;-------------  ASIG DOUBLE ---- (_parametro:=@cte3$p2) 
	 FSTP _parametro
	 FLD @cte3$p2           ;-------------  COMP DOUBLE ---- (_parametro comp @cte3$p2)
	 FLD _parametro
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JNE @Label_2 
;==================[THEN]==================
	 print chr$("'FUNCION1 - if'", 13,10) 
	 JMP @Label_3 
;==================[ELSE/FIN]==================
@Label_2:
;==================[FIN IF]==================
@Label_3:
	 FLD _parametro         ;-------------  ASIG DOUBLE ---- (_funcion1:=_parametro) 
	 FSTP _funcion1
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
