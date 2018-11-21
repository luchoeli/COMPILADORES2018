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
_b	dq ?
_a	dq ?
;______________________VARIABLES AUXILIARES____________________
var_aux_dx dw ?
@0 dw 0
@cte2$p25	dq 2.25
@cte2$p23E2	dq 2.23E2
@cte2$p	dq 2.
@cte$n2$p0	dq -2.0

;_____________________________CODE_____________________________
.code
start:
main proc 
	 FLD @cte2$p25          ;-------------  ASIG DOUBLE ---- (_a:=@cte2$p25) 
	 FSTP _a
	 FLD @cte2$p            ;-------------  COMP DOUBLE ---- (_a comp @cte2$p)
	 FLD _a
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JBE @Label_0 
;==================[THEN]==================
	 FLD @cte2$p23E2        ;-------------  ASIG DOUBLE ---- (_a:=@cte2$p23E2) 
	 FSTP _a
	 print chr$("'entro al if'", 13,10) 
	 FLD @cte$n2$p0         ;-------------  ASIG DOUBLE ---- (_b:=@cte$n2$p0) 
	 FSTP _b
	 JMP @Label_1 
;==================[ELSE/FIN]==================
@Label_0:
;==================[FIN IF]==================
@Label_1:
	 print chr$("'fuera del if'", 13,10) 
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
