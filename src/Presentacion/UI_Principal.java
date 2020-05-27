package Presentacion;

import Control.BLLUsuario;
import Control.ConvertirMayusculas;
import Control.Validaciones;
import Datos.Usuarios;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class UI_Principal extends javax.swing.JFrame {
    
    Validaciones v = new Validaciones();
    DefaultTableModel modelo_tabla;
    BLLUsuario bll = new BLLUsuario();
    FileInputStream fis;
    int longitudBytes, apretaFoto = 0, id = 0;
    boolean consultar = false;
    Usuarios u = new Usuarios();
    
    public UI_Principal() {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource("../Imagenes/Icono.png")).getImage());
        setLocationRelativeTo(null);
        metodosdeInicio();
        
        modelo_tabla = new DefaultTableModel() {
            //para que las filas no sean editables
            
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        
        Tbl_Datos.setModel(modelo_tabla);
        modelo_tabla.addColumn("Id");
        modelo_tabla.addColumn("Cedula");
        modelo_tabla.addColumn("Nombre");
        modelo_tabla.addColumn("Apellido");
        modelo_tabla.addColumn("Correo");
        bll.mostrarLista(modelo_tabla, Tbl_Datos);
        //para que las columnas no se muevan
        Tbl_Datos.getTableHeader().setReorderingAllowed(false);
        Tbl_Datos.getColumnModel().getColumn(0).setMaxWidth(0);
        Tbl_Datos.getColumnModel().getColumn(0).setMinWidth(0);
        Tbl_Datos.getColumnModel().getColumn(0).setPreferredWidth(0);
        
    }
    
    public final void metodosdeInicio() {
        
        v.SoloLetras(Jt_Nombre);
        v.SoloLetras(Jt_Apellidos);
        v.SoloLetras(Jt_Usuario);
        v.SoloNumeros(Jt_Cedula);
        v.SoloNumeros(Jt_Telefono);
        v.LimitarCaracteres(Jt_Cedula, 15);
        v.LimitarCaracteres(Jt_Nombre, 100);
        v.LimitarCaracteres(Jt_Apellidos, 100);
        v.LimitarCaracteres(Jt_Correo, 100);
        v.LimitarCaracteres(Jt_Telefono, 10);
        v.LimitarCaracteres(Jt_Usuario, 20);
        v.LimitarCaracteres(Jp_Clave, 50);
        Jt_Nombre.setDocument(new ConvertirMayusculas());
        Jt_Apellidos.setDocument(new ConvertirMayusculas());
        Jt_Buscar.setDocument(new ConvertirMayusculas());
        Jt_Usuario.setDocument(new ConvertirMayusculas());
        
    }
    
    public boolean ValidarCorreo(String correo) {
        Pattern pat;
        Matcher mat;
        pat = null;
        mat = null;
        pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        mat = pat.matcher(correo);
        
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }
    
    public void ValidarIngreso() {
        String cc = Jt_Cedula.getText().trim();
        String nom = Jt_Nombre.getText().trim();
        String ape = Jt_Apellidos.getText().trim();
        String tel = Jt_Telefono.getText().trim();
        String user = Jt_Usuario.getText().trim();
        String cla = Jp_Clave.getText();
        boolean estado = ValidarCorreo(Jt_Correo.getText());
        Date fec = jDate_Fecha.getDate();
        
        if (cc.isEmpty() || nom.isEmpty() || ape.isEmpty() || tel.isEmpty() || user.isEmpty() || cla.isEmpty() || estado == false || fec == null) {
            Btn_Guardar.setEnabled(false);
        } else {
            Btn_Guardar.setEnabled(true);
        }
    }
    
    public void cargarFoto() {
        
        JFileChooser j = new JFileChooser();
        FileNameExtensionFilter filto = new FileNameExtensionFilter("JPG & PNG", "jpg", "png");
        j.setFileFilter(filto);
        
        int estado = j.showOpenDialog(null);
        if (estado == JFileChooser.APPROVE_OPTION) {
            
            try {
                fis = new FileInputStream(j.getSelectedFile());
                this.longitudBytes = (int) j.getSelectedFile().length();
                
                try {
                    Jl_Foto.setIcon(null);
                    Image icono = ImageIO.read(j.getSelectedFile()).getScaledInstance(
                            Jl_Foto.getWidth(), Jl_Foto.getHeight(), Image.SCALE_DEFAULT);
                    Jl_Foto.setIcon(new ImageIcon(icono));
                    Jl_Foto.updateUI();
                    apretaFoto = 1;
                    System.out.println("Longutud de bytes: " + longitudBytes);
                    
                } catch (IOException e) {
                    System.out.println("Error a cargar la foto IO: " + e);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error a cargar file: " + e);
            }
            
        } else {
        }
        
    } 
    
    public void botonGuardar() {
        
        String cedula = Jt_Cedula.getText().trim();
        String nombre = Jt_Nombre.getText().trim();
        String apellidos = Jt_Apellidos.getText().trim();
        String correo = Jt_Correo.getText().trim();
        String telefono = Jt_Telefono.getText().trim();
        String usuario = Jt_Usuario.getText().trim();
        String clave = Jp_Clave.getText();
        Date fecha = jDate_Fecha.getDate();
        u.setIdusuario(id);
        u.setCedula(cedula);
        u.setNombre(nombre);
        u.setApellido(apellidos);
        u.setCorreo(correo);
        u.setTelefono(telefono);
        u.setUsuario(usuario);
        u.setClave(clave);
        u.setFecha(fecha);
        u.setFis(fis);
        u.setLongitudBytes(longitudBytes);
        
        if (consultar == false) {
            //Insertar datos
            bll.insertarDatos(u);
            limpiarTodo();
        } else if (consultar == true) {
            //modificar datos
            if (apretaFoto == 0) {
                bll.modificarDatosSinFoto(u);
            } else if (apretaFoto == 1) {
                bll.modificarDatosConFoto(u);
            }
            limpiarTodo();
        }
        
    }
    
    public void actualizarTabla() {

        //limpiar tabla
        while (modelo_tabla.getRowCount() > 0) {
            modelo_tabla.removeRow(0);
        }
        //Cargar tabla
        bll.mostrarLista(modelo_tabla, Tbl_Datos);
        
    }
    
    public void limpiarTodo() {
        
        Jt_Cedula.setText(null);
        Jt_Nombre.setText(null);
        Jt_Apellidos.setText(null);
        Jt_Correo.setText(null);
        Jt_Telefono.setText(null);
        Jt_Usuario.setText(null);
        Jp_Clave.setText(null);
        jDate_Fecha.setDate(null);
        Jl_Foto.setIcon(null);
        Btn_Guardar.setEnabled(false);
        Btn_Eliminar.setEnabled(false);
        apretaFoto = 0;
        consultar = false;
        actualizarTabla();
        
    }
    
    public void buscar() {
        String dato = Jt_Buscar.getText();
        
        if (dato.isEmpty()) {
            actualizarTabla();
        } else if (!dato.isEmpty()) {
            while (modelo_tabla.getRowCount() > 0) {
                modelo_tabla.removeRow(0);
            }
            bll.buscarLista(modelo_tabla, Tbl_Datos, dato);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tbl_Datos = new javax.swing.JTable();
        Jl_Buscar = new javax.swing.JLabel();
        Jt_Buscar = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        Jl_Foto = new javax.swing.JLabel();
        Btn_SubirFoto = new javax.swing.JButton();
        Jl_Cedula = new javax.swing.JLabel();
        Jt_Cedula = new javax.swing.JTextField();
        Jl_Nombre = new javax.swing.JLabel();
        Jt_Nombre = new javax.swing.JTextField();
        Jl_Apellidos = new javax.swing.JLabel();
        Jt_Apellidos = new javax.swing.JTextField();
        Jt_Correo = new javax.swing.JTextField();
        Jl_Correo = new javax.swing.JLabel();
        Jl_Telefono = new javax.swing.JLabel();
        Jt_Telefono = new javax.swing.JTextField();
        Jl_Usuario = new javax.swing.JLabel();
        Jt_Usuario = new javax.swing.JTextField();
        Jl_FechaNacimiento = new javax.swing.JLabel();
        Jp_Clave = new javax.swing.JPasswordField();
        Jl_Clave1 = new javax.swing.JLabel();
        jDate_Fecha = new com.toedter.calendar.JDateChooser();
        Btn_Nuevo = new javax.swing.JButton();
        Btn_Guardar = new javax.swing.JButton();
        Btn_Eliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión de usuarios");
        setResizable(false);

        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setToolTipText("");
        panel.setFocusable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        Tbl_Datos.setForeground(new java.awt.Color(51, 51, 51));
        Tbl_Datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        Tbl_Datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tbl_DatosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tbl_Datos);

        Jl_Buscar.setForeground(new java.awt.Color(51, 51, 51));
        Jl_Buscar.setText("Buscar");

        Jt_Buscar.setForeground(new java.awt.Color(51, 51, 51));
        Jt_Buscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        Jt_Buscar.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                Jt_BuscarCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 666, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(Jl_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Jt_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Jt_Buscar)
                    .addComponent(Jl_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        panel.addTab("Lista de usuarios", jPanel2);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Jl_Foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Jl_Foto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel1.add(Jl_Foto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 17, 150, 200));

        Btn_SubirFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/User.png"))); // NOI18N
        Btn_SubirFoto.setText("Subir foto");
        Btn_SubirFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_SubirFotoActionPerformed(evt);
            }
        });
        jPanel1.add(Btn_SubirFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 150, 30));

        Jl_Cedula.setText("Cedula");
        jPanel1.add(Jl_Cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 17, 99, 30));

        Jt_Cedula.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                Jt_CedulaCaretUpdate(evt);
            }
        });
        jPanel1.add(Jt_Cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 17, 375, 30));

        Jl_Nombre.setText("Nombre");
        jPanel1.add(Jl_Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 53, 99, 30));

        Jt_Nombre.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                Jt_NombreCaretUpdate(evt);
            }
        });
        jPanel1.add(Jt_Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 53, 375, 30));

        Jl_Apellidos.setText("Apellidos");
        jPanel1.add(Jl_Apellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 89, 99, 30));

        Jt_Apellidos.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                Jt_ApellidosCaretUpdate(evt);
            }
        });
        jPanel1.add(Jt_Apellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 89, 375, 30));

        Jt_Correo.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                Jt_CorreoCaretUpdate(evt);
            }
        });
        jPanel1.add(Jt_Correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 125, 375, 30));

        Jl_Correo.setText("Correo");
        jPanel1.add(Jl_Correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 125, 99, 30));

        Jl_Telefono.setText("Telefono");
        jPanel1.add(Jl_Telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 161, 99, 30));

        Jt_Telefono.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                Jt_TelefonoCaretUpdate(evt);
            }
        });
        jPanel1.add(Jt_Telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 161, 375, 30));

        Jl_Usuario.setText("Usuario");
        jPanel1.add(Jl_Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 197, 99, 30));

        Jt_Usuario.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                Jt_UsuarioCaretUpdate(evt);
            }
        });
        jPanel1.add(Jt_Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 197, 375, 30));

        Jl_FechaNacimiento.setText("Fecha de nacimiento");
        jPanel1.add(Jl_FechaNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 271, 280, 30));

        Jp_Clave.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                Jp_ClaveCaretUpdate(evt);
            }
        });
        jPanel1.add(Jp_Clave, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 233, 375, 30));

        Jl_Clave1.setText("Contraseña");
        jPanel1.add(Jl_Clave1, new org.netbeans.lib.awtextra.AbsoluteConstraints(187, 240, 99, -1));

        jDate_Fecha.setBackground(new java.awt.Color(255, 255, 255));
        jDate_Fecha.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDate_FechaPropertyChange(evt);
            }
        });
        jPanel1.add(jDate_Fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(479, 271, 200, 30));

        Btn_Nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Add.png"))); // NOI18N
        Btn_Nuevo.setText("Nuevo");
        Btn_Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_NuevoActionPerformed(evt);
            }
        });
        jPanel1.add(Btn_Nuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 325, 120, 30));

        Btn_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Save.png"))); // NOI18N
        Btn_Guardar.setText("Guardar");
        Btn_Guardar.setEnabled(false);
        Btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_GuardarActionPerformed(evt);
            }
        });
        jPanel1.add(Btn_Guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 325, 120, 30));

        Btn_Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Delete.png"))); // NOI18N
        Btn_Eliminar.setText("Eliminar");
        Btn_Eliminar.setEnabled(false);
        Btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_EliminarActionPerformed(evt);
            }
        });
        jPanel1.add(Btn_Eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 325, 120, 30));

        panel.addTab("Usuarios", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Btn_SubirFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_SubirFotoActionPerformed
        
        cargarFoto();

    }//GEN-LAST:event_Btn_SubirFotoActionPerformed

    private void Jt_CedulaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_Jt_CedulaCaretUpdate
        ValidarIngreso();
    }//GEN-LAST:event_Jt_CedulaCaretUpdate

    private void Jt_NombreCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_Jt_NombreCaretUpdate
        ValidarIngreso();
    }//GEN-LAST:event_Jt_NombreCaretUpdate

    private void Jt_ApellidosCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_Jt_ApellidosCaretUpdate
        ValidarIngreso();
    }//GEN-LAST:event_Jt_ApellidosCaretUpdate

    private void Jt_CorreoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_Jt_CorreoCaretUpdate
        ValidarIngreso();
    }//GEN-LAST:event_Jt_CorreoCaretUpdate

    private void Jt_TelefonoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_Jt_TelefonoCaretUpdate
        ValidarIngreso();
    }//GEN-LAST:event_Jt_TelefonoCaretUpdate

    private void Jt_UsuarioCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_Jt_UsuarioCaretUpdate
        ValidarIngreso();
    }//GEN-LAST:event_Jt_UsuarioCaretUpdate

    private void Jp_ClaveCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_Jp_ClaveCaretUpdate
        ValidarIngreso();
    }//GEN-LAST:event_Jp_ClaveCaretUpdate

    private void jDate_FechaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDate_FechaPropertyChange
        ValidarIngreso();
    }//GEN-LAST:event_jDate_FechaPropertyChange

    private void Btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_GuardarActionPerformed
        
        botonGuardar();

    }//GEN-LAST:event_Btn_GuardarActionPerformed

    private void Jt_BuscarCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_Jt_BuscarCaretUpdate
        
        buscar();

    }//GEN-LAST:event_Jt_BuscarCaretUpdate

    private void Tbl_DatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tbl_DatosMouseClicked
        
        if (evt.getClickCount() == 2) {
            
            consultar = true;
            int fila = Tbl_Datos.getSelectedRow();
            id = (int) Tbl_Datos.getValueAt(fila, 0);
            Object[] datos = bll.consultarPorId(id, Jl_Foto);
            
            Jt_Cedula.setText(datos[0].toString());
            Jt_Nombre.setText(datos[1].toString());
            Jt_Apellidos.setText(datos[2].toString());
            Jt_Correo.setText(datos[3].toString());
            Jt_Telefono.setText(datos[4].toString());
            Jt_Usuario.setText(datos[5].toString());
            Jp_Clave.setText(datos[6].toString());
            jDate_Fecha.setDate((Date) datos[7]);
            try {
                Jl_Foto.setIcon((Icon) datos[8]);
            } catch (Exception e) {
            }
            panel.setSelectedIndex(1);
            Btn_Eliminar.setEnabled(true);
        } else {
        }

    }//GEN-LAST:event_Tbl_DatosMouseClicked

    private void Btn_NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_NuevoActionPerformed

        limpiarTodo();
        
    }//GEN-LAST:event_Btn_NuevoActionPerformed

    private void Btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_EliminarActionPerformed

        int respues = JOptionPane.showConfirmDialog(null, "¿Eliminar usuario?");
        if (respues==0) {
            u.setIdusuario(id);
            bll.eliminarDatos(u);
            limpiarTodo();
        } 
        
    }//GEN-LAST:event_Btn_EliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UI_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UI_Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_Eliminar;
    private javax.swing.JButton Btn_Guardar;
    private javax.swing.JButton Btn_Nuevo;
    private javax.swing.JButton Btn_SubirFoto;
    private javax.swing.JLabel Jl_Apellidos;
    private javax.swing.JLabel Jl_Buscar;
    private javax.swing.JLabel Jl_Cedula;
    private javax.swing.JLabel Jl_Clave1;
    private javax.swing.JLabel Jl_Correo;
    private javax.swing.JLabel Jl_FechaNacimiento;
    private javax.swing.JLabel Jl_Foto;
    private javax.swing.JLabel Jl_Nombre;
    private javax.swing.JLabel Jl_Telefono;
    private javax.swing.JLabel Jl_Usuario;
    private javax.swing.JPasswordField Jp_Clave;
    private javax.swing.JTextField Jt_Apellidos;
    private javax.swing.JTextField Jt_Buscar;
    private javax.swing.JTextField Jt_Cedula;
    private javax.swing.JTextField Jt_Correo;
    private javax.swing.JTextField Jt_Nombre;
    private javax.swing.JTextField Jt_Telefono;
    private javax.swing.JTextField Jt_Usuario;
    private javax.swing.JTable Tbl_Datos;
    private com.toedter.calendar.JDateChooser jDate_Fecha;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane panel;
    // End of variables declaration//GEN-END:variables
}
