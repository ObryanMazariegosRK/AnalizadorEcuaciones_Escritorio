//Chimaltenango 12 de febrero de 2026
/*
Integrantes: 
1990-23-22934	Freyder José Sequén Urlao
1990-23-4406	Christopher Obryan Mazariegos Crúz
1990-23-17188	Luis Miguel Vaquiax Camey
1990-23-10442	Keyner Alejandro Rivera Axpuac
*/

//Token, esta clase actua como un objeto de transferencia de datos, es la 
//estructura que utiliza el analizador léxico para enviar la información a 
//la clase analizadorEcuacion

package ecuacion_2ndogrado;

public class Token {
    //atributos de la clase
    public String tipo;
    public String valor;

    //Constructor, al crear un nuevo token se debera de decir el tipo y su valor
    public Token(String tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
}