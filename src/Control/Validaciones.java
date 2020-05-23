
package Control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class Validaciones {
    
     public void ValidaSoloLetras(JTextField campo){
        campo.addKeyListener(new KeyAdapter() {
            public void KeyTyped(KeyEvent e){
                char c = e.getKeyChar();
                int k = (int)e.getKeyChar();
                if(Character.isDigit(c) || k==64){
                    e.consume();
                    System.out.println("car: "+c);
                }
            }
        });
    }
    
    public void valodarSoloNumeros(JTextField campo){
        campo.addKeyListener(new KeyAdapter() {
        public void KeyTyped(KeyEvent e){
            char c = e.getKeyChar();
            if(!Character.isDigit(c)){
                e.consume();
                System.out.println("car: "+c);
            }
        }
        });
    }
    
  public void LimitarCaracteres(final JTextField campo, final int cantidad){
      campo.addKeyListener(new KeyAdapter() {
          public void KeyTyped(KeyEvent e){
              char c = e.getKeyChar();
              int tamano=campo.getText().length();
              if(tamano>=cantidad){
                  e.consume();
                  System.out.println("car: "+c);
              }
          }
      });
  }
    
}
