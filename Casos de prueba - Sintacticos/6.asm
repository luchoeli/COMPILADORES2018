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
@cte2$p	dq 2.
@cte2$p23E2	dq 2.23E2
@cte$n2$p3	dq -2.3
@cte3$p5	dq 3.5
@aux3	dq ?
@aux2	dq ?
@cte2$p3	dq 2.3
@aux1	dq ?
@cte10$p0	dq 10.0
@aux0	dq ?

;_____________________________CODE_____________________________
.code
start:
main proc 
	 print chr$("'primero tenria que entrar al if y desp al else'", 13,10) 
	 FLD @cte2$p3           ;-------------  ASIG DOUBLE ---- (_a:=@cte2$p3) 
	 FSTP _a
	 FLD @cte10$p0          ;-------------  ASIG DOUBLE ---- (_b:=@cte10$p0) 
	 FSTP _b
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
	 print chr$("'entro en if'", 13,10) 
	 JMP @Label_1 
;==================[ELSE/FIN]==================
@Label_0:
	 FLD _a                 ;-------------  ADD DOUBLE ---- (_a+_b) 
	 FADD _b
	 FSTP @aux0
	 FLD @aux0
	 FXAM
	 FSTSW aux_mem_2bytes
	 MOV ax , aux_mem_2bytes
	 FWAIT
	 SAHF
	 JZ @LABEL_SIGUIENTE_INSTRUCCION@aux0
	 JPE @LABEL_C2is1@aux0
	 JMP @LABEL_SIGUIENTE_INSTRUCCION@aux0
	 @LABEL_C2is1@aux0:
	 JC    @LABEL_OVERFLOW
	 @LABEL_SIGUIENTE_INSTRUCCION@aux0:
	 FLD @aux0              ;-------------  ASIG DOUBLE ---- (_b:=@aux0) 
	 FSTP _b
	 print chr$("'entro en else'", 13,10) 
	 FLD @cte2$p3           ;-------------  MULT DOUBLE ---- (@cte2$p3*@cte3$p5)
	 FMUL @cte3$p5
	 FSTP @aux1
	 FLD @aux1              ;-------------  ASIG DOUBLE ---- (_a:=@aux1) 
	 FSTP _a
@Label_1:
;==================[FIN IF]==================
	 FLD @cte$n2$p3         ;-------------  ASIG DOUBLE ---- (_a:=@cte$n2$p3) 
	 FSTP _a
	 FLD @cte10$p0          ;-------------  ASIG DOUBLE ---- (_b:=@cte10$p0) 
	 FSTP _b
	 FLD @cte2$p            ;-------------  COMP DOUBLE ---- (_a comp @cte2$p)
	 FLD _a
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JBE @Label_2 
;==================[THEN]==================
	 FLD @cte2$p23E2        ;-------------  ASIG DOUBLE ---- (_a:=@cte2$p23E2) 
	 FSTP _a
	 print chr$("'entro en if'", 13,10) 
	 JMP @Label_3 
;==================[ELSE/FIN]==================
@Label_2:
	 FLD _a                 ;-------------  ADD DOUBLE ---- (_a+_b) 
	 FADD _b
	 FSTP @aux2
	 FLD @aux2
	 FXAM
	 FSTSW aux_mem_2bytes
	 MOV ax , aux_mem_2bytes
	 FWAIT
	 SAHF
	 JZ @LABEL_SIGUIENTE_INSTRUCCION@aux2
	 JPE @LABEL_C2is1@aux2
	 JMP @LABEL_SIGUIENTE_INSTRUCCION@aux2
	 @LABEL_C2is1@aux2:
	 JC    @LABEL_OVERFLOW
	 @LABEL_SIGUIENTE_INSTRUCCION@aux2:
	 FLD @aux2              ;-------------  ASIG DOUBLE ---- (_b:=@aux2) 
	 FSTP _b
	 print chr$("'entro en else'", 13,10) 
	 FLD @cte2$p3           ;-------------  MULT DOUBLE ---- (@cte2$p3*@cte3$p5)
	 FMUL @cte3$p5
	 FSTP @aux3
	 FLD @aux3              ;-------------  ASIG DOUBLE ---- (_a:=@aux3) 
	 FSTP _a
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
