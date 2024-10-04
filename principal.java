package banco;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class principal {

    public static void main(String[] args) {
        Banco banco = new Banco();
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("Menú:");
            System.out.println("1. Agregar cliente con cuenta");
            System.out.println("2. Agregar cliente sin cuenta");
            System.out.println("3. Atender cliente");
            System.out.println("4. Ingresar dinero a caja");
            System.out.println("5. Imprimir estado");
            System.out.println("6. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    banco.agregarCliente(new Cliente(true));
                    break;
                case 2:
                    banco.agregarCliente(new Cliente(false));
                    break;
                case 3:
                    banco.mostrarClientes();
                    System.out.print("Ingresa el número del cliente a atender: ");
                    int numCliente = scanner.nextInt();
                    System.out.print("Elige la caja (1-4): ");
                    int numCaja = scanner.nextInt();
                    banco.atenderCliente(numCliente, numCaja);
                    break;
                case 4:
                    System.out.print("Elige la caja (1-4): ");
                    int cajaNum = scanner.nextInt();
                    System.out.print("Ingresa el tipo de billete/moneda: ");
                    double tipo = scanner.nextDouble();
                    System.out.print("Ingresa la cantidad: ");
                    int cantidad = scanner.nextInt();
                    banco.ingresarDineroCaja(cajaNum, tipo, cantidad);
                    break;
                case 5:
                    banco.imprimirEstado();
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 6);
        scanner.close();
    }
}

class Cliente {//Le junte las clases en el main
    boolean tieneCuenta;
    int id;
    static int idCounter = 1;

    Cliente(boolean tieneCuenta) {
        this.tieneCuenta = tieneCuenta;
        this.id = idCounter++;
    }
}

class Caja {//Le junte las clases en el main
    boolean ocupada;
    double[] tipos = {0.50, 1, 2, 5, 10, 20, 100, 200, 500, 1000};
    int[] cantidades = new int[tipos.length];

    public Caja() {
        for (int i = 0; i < tipos.length; i++) {
            cantidades[i] = 5; 
        }
    }

    public void depositar(double tipo, int cantidad) {
        for (int i = 0; i < tipos.length; i++) {
            if (tipos[i] == tipo) {
                cantidades[i] += cantidad;
                break;
            }
        }
    }

    public boolean retirar(double monto) {
        double total = 0;
        for (int i = 0; i < tipos.length; i++) {
            total += tipos[i] * cantidades[i];
        }
        if (monto > total) {
            System.out.println("No hay suficiente dinero en la caja.");
            return false;
        }
        for (int i = tipos.length - 1; i >= 0; i--) {
            while (monto >= tipos[i] && cantidades[i] > 0) {
                monto -= tipos[i];
                cantidades[i]--;
            }
        }
        if (monto > 0) {
            System.out.println("No se pudo retirar el monto exacto.");
            return false;
        }
        return true;
    }
}

class Banco { //Le junte las clases en el main
    Queue<Cliente> clientesConCuenta = new LinkedList<>();
    Queue<Cliente> clientesSinCuenta = new LinkedList<>();
    Caja[] cajas = {new Caja(), new Caja(), new Caja(), new Caja()};

    public void agregarCliente(Cliente cliente) {
        if (cliente.tieneCuenta) {
            clientesConCuenta.add(cliente);
            System.out.println("Cliente con cuenta agregado a la cola. ID: " + cliente.id);
        } else {
            clientesSinCuenta.add(cliente);
            System.out.println("Cliente sin cuenta agregado a la cola. ID: " + cliente.id);
        }
    }

    public void mostrarClientes() {
        System.out.println("Clientes con cuenta:");
        for (Cliente cliente : clientesConCuenta) {
            System.out.println("ID: " + cliente.id);
        }
        System.out.println("Clientes sin cuenta:");
        for (Cliente cliente : clientesSinCuenta) {
            System.out.println("ID: " + cliente.id);
        }
    }

    public void atenderCliente(int idCliente, int numCaja) {
        if (numCaja < 1 || numCaja > 4) {
            System.out.println("Número de caja no válido.");
            return;
        }
        Caja caja = cajas[numCaja - 1];
        if (caja.ocupada) {
            System.out.println("La caja " + numCaja + " está ocupada.");
            return;
        }
        Cliente cliente = null;
        for (Cliente c : clientesConCuenta) {
            if (c.id == idCliente) {
                cliente = c;
                clientesConCuenta.remove(c);
                break;
            }
        }
        if (cliente == null) {
            for (Cliente c : clientesSinCuenta) {
                if (c.id == idCliente) {
                    cliente = c;
                    clientesSinCuenta.remove(c);
                    break;
                }
            }
        }
        if (cliente != null) {
            caja.ocupada = true;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Atendiendo cliente ID: " + cliente.id + " en la caja " + numCaja);
            System.out.println("1. Depósito");
            System.out.println("2. Retiro");
            System.out.print("Elige una opción: ");
            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    System.out.print("Ingresa el tipo de billete/moneda: ");
                    double tipo = scanner.nextDouble();
                    System.out.print("Ingresa la cantidad: ");
                    int cantidad = scanner.nextInt();
                    caja.depositar(tipo, cantidad);
                    break;
                case 2:
                    System.out.print("Ingresa el monto a retirar: ");
                    double monto = scanner.nextDouble();
                    if (caja.retirar(monto)) {
                        System.out.println("Retiro exitoso.");
                    } else {
                        System.out.println("Retiro fallido.");
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
            caja.ocupada = false;
        } else {
            System.out.println("Cliente no encontrado en la cola.");
        }
    }

    public void ingresarDineroCaja(int numCaja, double tipo, int cantidad) {
        if (numCaja < 1 || numCaja > 4) {
            System.out.println("Número de caja no válido.");
            return;
        }
        cajas[numCaja - 1].depositar(tipo, cantidad);
        System.out.println("Dinero ingresado a la caja " + numCaja);
    }

    public void imprimirEstado() {
        System.out.println("Estado del banco:");
        System.out.println("Clientes con cuenta en cola: " + clientesConCuenta.size());
        System.out.println("Clientes sin cuenta en cola: " + clientesSinCuenta.size());
        for (int i = 0; i < cajas.length; i++) {
            System.out.println("Caja " + (i + 1) + " - Ocupada: " + cajas[i].ocupada);
            System.out.println("Tipos de billetes/monedas y cantidades:");
            for (int j = 0; j < cajas[i].tipos.length; j++) {
                System.out.println("Tipo: $" + cajas[i].tipos[j] + " - Cantidad: " + cajas[i].cantidades[j]);
            }
        }
    }
}