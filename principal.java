package maquinaExp;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class principal {

    public static void main(String[] args) {
        dispensadorDeAlimentos dispensador = new dispensadorDeAlimentos();
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("Menú:");
            System.out.println("1. Agregar producto");
            System.out.println("2. Elegir producto");
            System.out.println("3. Imprimir estado");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Ingresa el nombre del producto: ");
                    String nombre = scanner.next();
                    int cantidad = 0;
                    System.out.print("Ingresa la cantidad del producto (máximo 10): ");
                    cantidad = scanner.nextInt();
                    if (cantidad > 10) {
                        while (cantidad > 10) {
                            System.out.print("Ingresa la cantidad del producto (máximo 10): ");
                            cantidad = scanner.nextInt();
                        }
                    }
                    System.out.print("Ingresa el valor de monedas del producto: ");
                    double valor = scanner.nextDouble();
                    dispensador.agregarProducto(new Producto(nombre, cantidad, valor));
                    break;
                case 2:
                    dispensador.mostrarProductos();
                    System.out.print("Elige el número del producto: ");
                    int numProducto = scanner.nextInt();
                    System.out.print("Ingresa el monto: ");
                    double monto = scanner.nextDouble();
                    dispensador.elegirProducto(numProducto, monto);
                    break;
                case 3:
                    dispensador.imprimirEstado();
                    break;
                case 4:
                    System.out.println("Adios...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 4);
        scanner.close();
    }
}

class Producto { //Le junte las clases en el main
    String nombre;
    int cantidad;
    double valor;

    Producto(String nombre, int cantidad, double valor) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valor = valor;
    }
}

class Moneda { //Le junte las clases en el main
    double denominacion;
    int cantidad;

    Moneda(double denominacion, int cantidad) {
        this.denominacion = denominacion;
        this.cantidad = cantidad;
    }
}

class dispensadorDeAlimentos { //Le junte las clases en el main
    Queue<Producto> productos = new LinkedList<>();
    Moneda[] monedas = new Moneda[5];

    public dispensadorDeAlimentos() {
        // Agregar monedas
        double[] denominaciones = {10, 5, 2, 1, 0.50};
        for (int i = 0; i < denominaciones.length; i++) {
            monedas[i] = new Moneda(denominaciones[i], 13);
        }
    }

    public void agregarProducto(Producto producto) {
        if (productos.size() < 10) {
            productos.add(producto);
            System.out.println("Producto agregado: " + producto.nombre);
        } else {
            System.out.println("La cola de productos está llena.");
        }
    }

    public void mostrarProductos() {
        int i = 1;
        for (Producto producto : productos) {
            System.out.println(i + ". " + producto.nombre + " - Cantidad: " + producto.cantidad + " - Valor: $" + producto.valor);
            i++;
        }
    }

    public void elegirProducto(int numProducto, double monto) {
        if (productos.isEmpty()) {
            System.out.println("No hay productos disponibles.");
            return;
        }
        Producto productoElegido = null;
        int i = 1;
        for (Producto producto : productos) {
            if (i == numProducto) {
                productoElegido = producto;
                break;
            }
            i++;
        }
        if (productoElegido == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        if (monto < productoElegido.valor) {
            System.out.println("Monto insuficiente. El producto cuesta $" + productoElegido.valor);
            return;
        }
        productoElegido.cantidad--;
        if (productoElegido.cantidad == 0) {
            productos.remove(productoElegido);
        }
        System.out.println("Has elegido: " + productoElegido.nombre);
        sacarProducto(productoElegido);
        devolverCambio(monto - productoElegido.valor);
    }

    public void devolverCambio(double monto) {
        System.out.println("Devolviendo cambio: $" + monto);
        for (Moneda moneda : monedas) {
            while (monto >= moneda.denominacion && moneda.cantidad > 0) {
                monto -= moneda.denominacion;
                moneda.cantidad--;
            }
        }
        if (monto > 0) {
            System.out.println("No se pudo devolver todo el cambio. Cambio restante: $" + monto);
        }
    }

    public void sacarProducto(Producto producto) {
        System.out.println("Has sacado: " + producto.nombre);
    }

    public void imprimirEstado() {
        System.out.println("Estado del dispensador:");
        System.out.println("Productos disponibles: " + productos.size());
        for (Producto producto : productos) {
            System.out.println(producto.nombre + " - Cantidad: " + producto.cantidad + " - Valor: $" + producto.valor);
        }
        System.out.println("Monedas disponibles:");
        for (Moneda moneda : monedas) {
            System.out.println("Denominación: $" + moneda.denominacion + " - Cantidad: " + moneda.cantidad);
        }
    }
}