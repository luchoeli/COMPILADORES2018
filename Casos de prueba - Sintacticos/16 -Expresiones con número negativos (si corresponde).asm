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
_e	dw ?
_d	dw ?
_c	dq ?
_b	dq ?
_a	dq ?
;______________________VARIABLES AUXILIARES____________________
var_aux_dx dw ?
@0 dw 0
@cte$n4$p25	dq -4.25
@cte4$p0	dq 4.0
@cte$n2$p25	dq -2.25
@cte$n2$p0	dq -2.0
@cte10	dw 10
@cte3	dw 3
@aux3	dw ?
@aux2	dq ?
@cte14$p0	dq 14.0
@aux1	dq ?
@cte6$p0	dq 6.0
@aux0	dq ?
@cte2$p0	dq 2.0

;_____________________________CODE_____________________________
.code
start:
main proc 
	 FLD @cte2$p0           ;-------------  ADD DOUBLE ---- (@cte2$p0+@cte$n4$p25) 
	 FADD @cte$n4$p25
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
	 FLD @aux0              ;-------------  ASIG DOUBLE ---- (_a:=@aux0) 
	 FSTP _a
	 FLD @cte$n2$p25        ;-------------  COMP DOUBLE ---- (_a comp @cte$n2$p25)
	 FLD _a
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JNE @Label_0 
;==================[THEN]==================
	 print chr$("'se asigno bien'", 13,10) 
	 JMP @Label_1 
;==================[ELSE/FIN]==================
@Label_0:
;==================[FIN IF]==================
@Label_1:
	 FLD @cte2$p0           ;-------------  MULT DOUBLE ---- (@cte2$p0*@cte4$p0)
	 FMUL @cte4$p0
	 FSTP @aux1
	 FLD @aux1              ;-------------  ADD DOUBLE ---- (@aux1+@cte$n2$p0) 
	 FADD @cte$n2$p0
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
	 FLD @aux2              ;-------------  ASIG DOUBLE ---- (_a:=@aux2) 
	 FSTP _a
	 FLD @cte6$p0           ;-------------  COMP DOUBLE ---- (_a comp @cte6$p0)
	 FLD _a
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JNE @Label_2 
;==================[THEN]==================
	 print chr$("'dio 6'", 13,10) 
	 JMP @Label_3 
;==================[ELSE/FIN]==================
@Label_2:
;==================[FIN IF]==================
@Label_3:
	 MOV ax ,@cte3          ;-------------  ASIG USINT ---- (_e:=@cte3) 
	 MOV _e, ax
	 MOV ax ,@cte10         ;-------------  ASIG USINT ---- (_d:=@cte10) 
	 MOV _d, ax
	 MOV ax, _d             ;-------------  SUB USINT ---- (_d-@cte3)
	 SUB ax, @cte3
	 MOV @aux3, ax
	 MOV ax, _d
	 CMP ax, @cte3
	 JB @LABEL_RESULTADO
	 MOV ax ,@aux3          ;-------------  ASIG USINT ---- (_d:=@aux3) 
	 MOV _d, ax
	 FLD @cte14$p0          ;-------------  COMP DOUBLE ---- (_a comp @cte14$p0)
	 FLD _a
	 FCOM
	 FSTSW aux_mem_2bytes
	 MOV AX , aux_mem_2bytes
	 SAHF
	 JNE @Label_4 
;==================[THEN]==================
	 print chr$("'dio 6'", 13,10) 
	 JMP @Label_5 
;==================[ELSE/FIN]==================
@Label_4:
;==================[FIN IF]==================
@Label_5:
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
