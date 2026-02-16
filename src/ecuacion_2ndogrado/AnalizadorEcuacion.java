//Chimaltenango 12 de febrero de 2026
/*
Integrantes: 
1990-23-22934	Freyder José Sequén Urlao
1990-23-4406	Christopher Obryan Mazariegos Crúz
1990-23-17188	Luis Miguel Vaquiax Camey
1990-23-10442	Keyner Alejandro Rivera Axpuac
*/

//AnalizadorEcuación, se verifica las reglas gramaticales, se validan las 
//reglas semanticas y por ultimo se calculan las raices

package ecuacion_2ndogrado;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorEcuacion {

    public String procesar(String entrada) {
        
        //Si la entrada es nula o esta vacia
        if (entrada == null || entrada.trim().isEmpty())
            return "ERR:La entrada está vacía.";
        
        //Eliinamos los espacios en blanco
        String texto = entrada.replace(" ", "");
        //Creamos una instancia mandole la ecuacion al lexel para que lo divida 
        //en tokens
        Lexer lexer = new Lexer(new StringReader(texto));
        
        //Variables y listas que nos serviran para almacenar la información
        List<String> errores = new ArrayList<>();
        String variableDetectada = null;

        double a = 0, b = 0, c = 0;
        int signo = 1;
        //Nos permite igualar cualquier ecuación a cero
        boolean despuesDelIgual = false;
        int ultimoGrado = 3; // Para controlar el orden descendente

        Token t;

        try {
            //Mientras el resultado del lexel no sea nulo
            while ((t = lexer.yylex()) != null) {

                switch (t.tipo) {

                    case "SUM":
                        signo = 1;
                        break;

                    case "RES":
                        signo = -1;
                        break;

                    case "IGU":
                        if (despuesDelIgual)
                            return "ERR:Solo se permite un '='.";
                        despuesDelIgual = true;
                        signo = -1; //Todo lo que viene se multiplica por -1
                        break;

                    case "NUM":
                        double valor = Double.parseDouble(t.valor) * signo;
                        Token siguiente = lexer.yylex();
                        int gradoActual = 0;

                        if (siguiente != null) {
                            
                            /*
                            Si el siguiente token es una potencia significa que 
                            el número era coeficiente de A
                            */
                            if (siguiente.tipo.equals("POT")) {
                                String var = siguiente.valor.substring(0, 1);
                                if (variableDetectada == null)
                                    variableDetectada = var;
                                else if (!variableDetectada.equalsIgnoreCase(var))
                                    errores.add("No se permiten variables distintas.");

                                gradoActual = 2;
                                a += valor;

                            }//Si el siguiente es una variable X
                            /*
                                Significa que el número era coeficiente de B
                            */
                            else if (siguiente.tipo.equals("VAR")) {
                                String var = siguiente.valor;
                                if (variableDetectada == null)
                                    variableDetectada = var;
                                else if (!variableDetectada.equalsIgnoreCase(var))
                                    errores.add("No se permiten variables distintas.");

                                gradoActual = 1;
                                b += valor;

                            } 
                            /*
                               Sino, significara que el número esta solo
                            */
                            
                            else {
                                c += valor;
                                gradoActual = 0;
                                //Regresamos el token extra tomado
                                lexer.yypushback(siguiente.valor.length());
                            }

                        } else {//Si el siguiente token es nulo
                            c += valor;
                            gradoActual = 0;
                        }

                        // Validación de orden descendente
                        if (gradoActual > ultimoGrado)
                            errores.add("Los términos deben ir en orden descendente de grado.");
                        ultimoGrado = gradoActual;

                        signo = despuesDelIgual ? -1 : 1;
                        break;

                    case "VAR"://Cuando la variable viene sin numero 
                        if (variableDetectada == null)
                            variableDetectada = t.valor;
                        else if (!variableDetectada.equalsIgnoreCase(t.valor))
                            errores.add("No se permiten variables distintas.");

                        // Grado lineal
                        if (1 > ultimoGrado)
                            errores.add("Los términos deben ir en orden descendente de grado.");
                        ultimoGrado = 1;

                        b += 1 * signo;
                        signo = despuesDelIgual ? -1 : 1;
                        break;

                    case "POT":
                        String var = t.valor.substring(0, 1);
                        if (variableDetectada == null)
                            variableDetectada = var;
                        else if (!variableDetectada.equalsIgnoreCase(var))
                            errores.add("No se permiten variables distintas.");

                        // Grado cuadrático
                        if (2 > ultimoGrado)
                            errores.add("Los términos deben ir en orden descendente de grado.");
                        ultimoGrado = 2;

                        a += 1 * signo;
                        signo = despuesDelIgual ? -1 : 1;
                        break;

                    case "ERROR":
                        errores.add("Error léxico en: " + t.valor);
                        break;
                }
            }

            //Validaciones, si hubo problemas con el lexel
            if (!lexer.erroresLexicos.isEmpty())
                return "ERR:" + String.join(", ", lexer.erroresLexicos);

            if (!errores.isEmpty())
                return "ERR:" + String.join(", ", errores);
            
            //Validaciones matematicas 
            if (a == 0)
                return "ERR:No es ecuación de segundo grado.";

            //Si es negativo, se tendrian que sacar raices de números regativos,
            //en los realices eso no existe
            double discriminante = (b * b) - (4 * a * c);

            if (discriminante < 0)
                return "ERR:La ecuación tiene soluciones complejas.";


            //Resolución de la ecuación
            if (discriminante == 0) {
                double x = -b / (2 * a);
                return "Raíz doble: " + String.format("%.4f", x);//Solo 4 decimales
            }
            
            //Las dos posibles soluciones 
            double x1 = (-b + Math.sqrt(discriminante)) / (2 * a);
            double x2 = (-b - Math.sqrt(discriminante)) / (2 * a);

            return 
                    String.format("%.4f", x1)
                    + " | "//Separador
                    + String.format("%.4f", x2);

        } catch (Exception e) {
            return "ERR:Formato inválido.";
        }
    }
}
