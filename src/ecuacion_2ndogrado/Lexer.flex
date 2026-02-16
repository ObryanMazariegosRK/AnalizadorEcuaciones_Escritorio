//Chimaltenango 12 de febrero de 2026
/*
Integrantes: 
1990-23-22934	Freyder José Sequén Urlao
1990-23-4406	Christopher Obryan Mazariegos Crúz
1990-23-17188	Luis Miguel Vaquiax Camey
1990-23-10442	Keyner Alejandro Rivera Axpuac
*/

//Lexer, este archivo define reglas léxicas usando expresiones regulares


package ecuacion_2ndogrado;

import java.util.ArrayList;
import java.util.List;

%%
%class Lexer //Nombre de la clase generada
%type Token //Tipode dato que devolvera
%unicode 

%line
%column

%{
    //Lista para almacenar los errores
    public List<String> erroresLexicos = new ArrayList<>();
%}

/* Expresiones Regulares */
Digito = [0-9]+(\.[0-9]+)?
Variable = [a-zA-Z]
Espacio = [ \t\r\n]+

%%

{Espacio} { /* Ignorar */ }

/* Potencia explícita x^2 */
{Variable}"^2" { return new Token("POT", yytext()); }

/* Detectar potencia inválida */
{Variable}"^"[0-9]+ {
    erroresLexicos.add("Solo se permite exponente 2.");
    return new Token("ERROR", yytext());
}

/* Variable lineal */
{Variable} { return new Token("VAR", yytext()); }

{Digito} { return new Token("NUM", yytext()); }

"+" { return new Token("SUM", yytext()); }
"-" { return new Token("RES", yytext()); }
"=" { return new Token("IGU", yytext()); }

//Cualquier cosa que no se halla definido se tomara como error
. { 
    erroresLexicos.add("Caracter no válido: '" + yytext() + "'");
    return new Token("ERROR", yytext()); 
}
