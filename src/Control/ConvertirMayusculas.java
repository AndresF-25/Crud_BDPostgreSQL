package Control;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class ConvertirMayusculas extends PlainDocument {

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        super.insertString(offset, str.toUpperCase(), attr);
    }

}
