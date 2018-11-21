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
_b	dw ?
_a	dw ?
;______________________VARIABLES AUXILIARES____________________
var_aux_dx dw ?
@0 dw 0
@cte6	dw 6
@cte5	dw 5
@cte3	dw 3
@cte2	dw 2

;_____________________________CODE_____________________________
.code
start:
main proc 
	 MOV ax ,@cte2          ;-------------  ASIG USINT ---- (_a:=@cte2) 
	 MOV _a, ax
	 MOV ax ,@cte5          ;-------------  ASIG USINT ---- (_b:=@cte5) 
	 MOV _b, ax
	 MOV ax, @cte5          ;-------------  COMP USINT ---- (@cte5 comp _a)
	 CMP ax, _a
	 JNE @Label_0
	 MOV ax ,@cte6          ;-------------  ASIG USINT ---- (_a:=@cte6) 
	 MOV _a, ax
	 MOV ax ,@cte6          ;-------------  ASIG USINT ---- (_b:=@cte6) 
	 MOV _b, ax
@Label_0: 
	 MOV ax, @cte2          ;-------------  COMP USINT ---- (@cte2 comp _a)
	 CMP ax, _a
	 JNE @Label_1
	 print chr$("'case2 - ANTES DE IF'", 13,10) 
	 MOV ax, _b             ;-------------  COMP USINT ---- (_b comp _a)
	 CMP ax, _a
	 JBE @Label_2 
;==================[THEN]==================
	 MOV ax ,@cte3          ;-------------  ASIG USINT ---- (_a:=@cte3) 
	 MOV _a, ax
	 print chr$("'case2 THEN'", 13,10) 
	 MOV ax ,@cte3          ;-------------  ASIG USINT ---- (_b:=@cte3) 
	 MOV _b, ax
	 JMP @Label_3 
;==================[ELSE/FIN]==================
@Label_2:
	 print chr$("'case2 ELSE'", 13,10) 
@Label_3:
;==================[FIN IF]==================
	 print chr$("'case2 - DESP DE IF'", 13,10) 
@Label_1: 
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
