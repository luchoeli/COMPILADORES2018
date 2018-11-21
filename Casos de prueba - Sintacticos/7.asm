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
_b	dq ?
_a	dq ?
;______________________VARIABLES AUXILIARES____________________
var_aux_dx dw ?
@0 dw 0
@cte20	dw 20
@cte16	dw 16
@cte8$p0	dq 8.0
@cte6	dw 6
@cte4$p	dq 4.
@cte2	dw 2
@cte2$p5	dq 2.5
@aux3	dw ?
@aux2	dw ?
@aux1	dq ?
@cte10$p0	dq 10.0
@aux0	dq ?
@cte2$p0	dq 2.0

;_____________________________CODE_____________________________
.code
start:
main proc 
	 print chr$("'primero tenria que entrar al if y desp al else'", 13,10) 
	 FLD @cte2$p5           ;-------------  ASIG DOUBLE ---- (_a:=@cte2$p5) 
	 FSTP _a
	 FLD @cte10$p0          ;-------------  ASIG DOUBLE ---- (_b:=@cte10$p0) 
	 FSTP _b
	 FLD _a                 ;-------------  MULT DOUBLE ---- (_a*@cte4$p)
	 FMUL @cte4$p
	 FSTP @aux0
	 FLD @aux0              ;-------------  SUB DOUBLE ---- (@aux0-@cte2$p0)
	 FSUB @cte2$p0
	 FSTP @aux1
	 FLD @cte8$p0           ;-------------  COMP DOUBLE ---- (@aux1 comp @cte8$p0)
	 FLD @aux1
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JNE @Label_0 
;==================[THEN]==================
	 print chr$("'entro en if'", 13,10) 
	 JMP @Label_1 
;==================[ELSE/FIN]==================
@Label_0:
	 print chr$("'entro en else'", 13,10) 
@Label_1:
;==================[FIN IF]==================
	 MOV ax ,@cte2          ;-------------  ASIG USINT ---- (_c:=@cte2) 
	 MOV _c, ax
	 MOV ax ,@cte20         ;-------------  ASIG USINT ---- (_d:=@cte20) 
	 MOV _d, ax
	 MOV ax, @cte2          ;-------------  DIV USINT---- (_d/@cte2)
	 CMP @cte2, 0
	 JE @LABEL_DIVIDEZERO
	 MOV var_aux_dx, dx
	 MOV ax, _d
	 CWD
	 MOV bx, @cte2
	 DIV bx
	 MOV dx, var_aux_dx
	 MOV @aux2, ax
	 MOV ax, @aux2          ;-------------  ADD USINT ---- (@aux2+@cte6) 
	 ADD ax, @cte6
	 MOV @aux3, ax
	 JO @LABEL_OVERFLOW
	 MOV ax, @aux3          ;-------------  COMP USINT ---- (@aux3 comp @cte16)
	 CMP ax, @cte16
	 JE @Label_2 
;==================[THEN]==================
	 print chr$("'entro en if'", 13,10) 
	 JMP @Label_3 
;==================[ELSE/FIN]==================
@Label_2:
	 print chr$("'entro en else'", 13,10) 
@Label_3:
;==================[FIN IF]==================
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
