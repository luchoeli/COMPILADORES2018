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
@aux0	dw ?
@cte2	dw 2
@cte0	dw 0

;_____________________________CODE_____________________________
.code
start:
main proc 
	 MOV ax ,@cte2          ;-------------  ASIG USINT ---- (_a:=@cte2) 
	 MOV _a, ax
	 MOV ax ,@cte0          ;-------------  ASIG USINT ---- (_b:=@cte0) 
	 MOV _b, ax
	 MOV ax, _b             ;-------------  DIV USINT---- (_a/_b)
	 CMP _b, 0
	 JE @LABEL_DIVIDEZERO
	 MOV var_aux_dx, dx
	 MOV ax, _a
	 CWD
	 MOV bx, _b
	 DIV bx
	 MOV dx, var_aux_dx
	 MOV @aux0, ax
	 MOV ax ,@aux0          ;-------------  ASIG USINT ---- (_a:=@aux0) 
	 MOV _a, ax
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
