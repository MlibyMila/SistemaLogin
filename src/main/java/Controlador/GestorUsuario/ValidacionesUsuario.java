package Controlador.GestorUsuario;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class ValidacionesUsuario {

    public boolean validarCampos(String nombres, String apellidos, String email, String telefono) {
        if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
            mostrarMensaje("Por favor, completar los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validarEmail(email)) {
            mostrarMensaje("Por favor, ingresar un email valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validarTelefono(telefono)) {
            mostrarMensaje("Ingresar un numero telefonico valido", "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (!validarNombreApellido(nombres)) {
            mostrarMensaje("Por favor, ingresar un nombre valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validarNombreApellido(apellidos)) {
            mostrarMensaje("Por favor, ingresar un apellido valido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String formatoEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern patron = Pattern.compile(formatoEmail);
        Matcher confirmar = patron.matcher(email.trim());
        return confirmar.matches();
    }

    public boolean validarNombreApellido(String campo) {
        if (campo == null || campo.trim().isEmpty()) {
            return false;
        }
        String formatName = "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,}$";
        Pattern patron = Pattern.compile(formatName);
        Matcher confirmar = patron.matcher(campo.trim());
        return confirmar.matches();
    }

    public boolean validarTelefono(String telefono) {
        if (telefono.matches("^\\d{7,15}$")) {
            return true;
        }
        return false;
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipo);
    }

}
