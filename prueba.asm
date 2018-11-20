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
_d	dw ?
_c	dw ?
_b	real8 ?
_a	real8 ?
;______________________VARIABLES AUXILIARES____________________
var_aux_dx dw ?
@0 dw 0
@cte4$5	real8 4.5
@cte6	dw 6
@aux1	dw ?
@cte4	dw 4
@aux0	real8 ?
@cte3$5	real8 3.5
@cte8$	real8 8.

;_____________________________CODE_____________________________
.code
start:
main proc 
	 FLD @cte3$5            ;-------------  ASIG DOUBLE ---- (_a+@cte3$5) 
	 FSTP _a
	 FLD @cte4$5            ;-------------  ASIG DOUBLE ---- (_b+@cte4$5) 
	 FSTP _b
	 FLD _a                 ;-------------  SUB DOUBLE ---- (_a-_b)
	 FILD _b
	 FSUB 
	 FSTP @aux0
	 FLD @cte8$             ;-------------  COMP DOUBLE ---- (@aux0 comp @cte8$)
	 FLD @aux0
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JNE @Label_0 
;==================[THEN]==================
	 print chr$("'si es = a 8'", 13,10) 
	 JMP @Label_1 
;==================[ELSE/FIN]==================
@Label_0:
	 print chr$("'distinto a 8'", 13,10) 
@Label_1:
;==================[FIN IF]==================
	 MOV ax ,@cte4          ;-------------  ASIG USINT ---- (_c:=@cte4) 
	 MOV _c, ax
	 MOV ax, _c             ;-------------  SUB USINT ---- (_c-@cte6)
	 SUB ax, @cte6
	 MOV @aux1, ax
	 MOV ax, _c
	 CMP ax, @cte6
	 JB @LABEL_RESULTADO
	 MOV ax ,@aux1          ;-------------  ASIG USINT ---- (_d:=@aux1) 
	 MOV _d, ax
main endp 
JMP @LABEL_END 
 
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
