package Controlador;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class ValidacionesLogeoRegistro {
    
    public boolean validarCampo(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Por favor, completar los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validarEmail(email)) {
            mostrarMensaje("Por favor, ingresar un email valido", "Error", JOptionPane.ERROR_MESSAGE);
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

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipo);
    }

    
}
