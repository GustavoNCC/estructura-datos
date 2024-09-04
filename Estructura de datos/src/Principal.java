import java.util.Scanner;
import java.util.Stack;

public class Principal {

	public static void main(String[] args) {
		
		Scanner read = new Scanner(System.in);
		System.out.print("Ingrese la expresión matemática: ");
        String expresion = read.nextLine();

        if (validacion(expresion)) {
            System.out.println("La expresión es válida.");
            System.out.println("Resultado: " + expresion(expresion));
        } else {
            System.out.println("La expresión es inválida.");
        }
    }

	public static boolean validacion(String expresion) {
		Stack<Character> pila = new Stack<>();
		boolean validacion1 = true;

		for (char caracter : expresion.toCharArray()) {
			if (caracter == '(') {
				pila.push(caracter);
			} else if (caracter == ')') {
				if (pila.isEmpty()) {
					validacion1 = false;
					break;
				}
				pila.pop();
			}
		}

		if (!pila.isEmpty()) {
			validacion1 = false;

		}
		return validacion1;
	}
	
	public static int expresion(String expresion) {
        Stack<Integer> numeros = new Stack<>(); // Integer funciona para almacenar numero enteros a la pila
        Stack<Character> operadores = new Stack<>(); // Character funciona para almacenar caracteres a la pila

        for (char caracter : expresion.toCharArray()) {
            if (Character.isDigit(caracter)) { //El is.digit sirve para saber si chracter es un digito
                numeros.push(caracter - '0');
            } else if (caracter == '+' || caracter == '-' || caracter == '*' || caracter == '/') {
                while (!operadores.isEmpty() && chequeoDeSigno(operadores.peek()) >= chequeoDeSigno(caracter)) {
                    int b = (int) numeros.pop(); //Pop sisve para sacar de la pila un caracter o número
                    int a = (int) numeros.pop();
                    char operador = (char) operadores.pop();
                    numeros.push(validarOperador(a, b, operador));
                }
                operadores.push(caracter);
            }
        }

        while (!operadores.isEmpty()) { //Empty te ayuda a saber si esta vacia la pila 
            int b = (int) numeros.pop();
            int a = (int) numeros.pop();
            char operador = (char) operadores.pop();
            numeros.push(validarOperador(a, b, operador)); //Push te ayuda aañadir datos a tu pila
        }

        return (int) numeros.pop();
    }

    public static int chequeoDeSigno(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    public static int validarOperador(int a, int b, char operador) {
        switch (operador) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            default:
                throw new IllegalArgumentException("Operador invalido: " + operador);
        }
    }
	
}