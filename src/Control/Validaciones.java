package Control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class Validaciones {

    public void SoloNumeros(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                    System.out.println("car: " + c);
                }
            }
        });
    }

    public void SoloLetras(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c)) {
                    e.consume();
                    System.out.println("car: " + c);
                }
            }
        });
    }

    public void LimitarCaracteres(final JTextField campo, final int cantidad) {
        campo.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                int tam = campo.getText().length();
                if (tam >= cantidad) {
                    e.consume();
                    System.out.println("car: " + c);
                }
            }
        });
    }

}
