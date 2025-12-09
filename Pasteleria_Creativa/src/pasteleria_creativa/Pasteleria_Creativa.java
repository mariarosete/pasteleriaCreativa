package pasteleria_creativa;

/**
 *  * @author María Rosete Suárez
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.plaf.ColorUIResource;
import javax.help.HelpSet;
import javax.help.HelpBroker;
import java.io.File;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Pasteleria_Creativa extends JFrame implements ActionListener {
   
    /********************************Componentes principales****************************************************************************/
    private JTextArea taPostres, taIngredientes;
    private DefaultListModel<String> modeloRecetas;
    
    private JList<String> listaRecetas;
    private ArrayList<JCheckBoxMenuItem> checkIngredientes;
    
    private HashMap<String, String> recetasMap;      // Información del postre
    private HashMap<String, String> ingredientesMap; // Ingredientes del postre
        
    private JMenuBar menuBar;
    private JMenu menuArchivo, menuIngredientes, menuPostres, menuColor, menuAyuda;
    private JMenuItem miNuevo, miGuardar, miCargar, miGenerarArchivo, miAgregarIngrediente, miEscogecolor, miAcercaDe,miVerAyuda;
    private JMenuItem miNombrePostre, miTipoPostre, miCategoriaPostre;

    private JToolBar toolBar; 
    private JButton btnNuevo, btnGuardar, btnCargar, btnGenerarArchivo, btnAgregarIngrediente,btnAyuda;
    private JButton btnNombrePostre, btnTipoPostre, btnCategoriaPostre;

    // Variables para almacenar datos del postre actual
    private String nombrePostre = "";
    private String tipoPostre = "";
    private String categoriaPostre = "";
    private String ingredientesPostre = "";
    
    /***********************************Constructor de la clase*********************************************************************************************/

  public Pasteleria_Creativa() {
        super("Pastelería Creativa");

        // Establecer el ícono de la ventana
        setIconImage(new ImageIcon(getClass().getResource("/images/16x16/reposteria.png")).getImage());

        setSize(1200, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Establecer colores personalizados para el menú
        UIManager.put("MenuItem.selectionBackground", new ColorUIResource(255, 255, 204));  // Color de fondo al pasar el ratón
        UIManager.put("MenuItem.selectionForeground", new ColorUIResource(255, 102, 178));  // Color del texto al pasar el ratón
        UIManager.put("Menu.selectionBackground", new ColorUIResource(255, 102, 178)); // Submenú
        UIManager.put("Menu.selectionForeground", new ColorUIResource(255, 255, 204)); // Submenú

        // Inicialización de datos
        recetasMap = new HashMap<>();
        ingredientesMap = new HashMap<>();
        modeloRecetas = new DefaultListModel<>();

        // Crear áreas de texto para Postres
        taPostres = new JTextArea(10, 30);
        taPostres.setWrapStyleWord(true);
        taPostres.setLineWrap(true);
        taPostres.setEditable(false);
        taPostres.setBackground(new Color(255, 255, 204)); // (amarillo claro)
        taPostres.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Borde gris
        taPostres.setFont(new Font("Georgia", Font.PLAIN, 16)); 
        
        JScrollPane scrollPostres = new JScrollPane(taPostres);
        JPanel panelPostres = new PanelPersonalizado(new Color(255, 255, 204));  // fondo amarillo suave
        panelPostres.setLayout(new BorderLayout());
        panelPostres.add(scrollPostres, BorderLayout.CENTER);

        // Título de "Postres"
        JLabel labelPostres = new JLabel("Postres", SwingConstants.CENTER);
        labelPostres.setIcon(new ImageIcon(getClass().getResource("/resources/postre.png")));
        labelPostres.setFont(new Font("Georgia", Font.BOLD, 22));
        labelPostres.setIconTextGap(10);
        labelPostres.setBackground(new Color(255, 255, 255));
        labelPostres.setOpaque(true);  
        panelPostres.add(labelPostres, BorderLayout.NORTH);

        // Crear área de texto para Ingredientes
        taIngredientes = new JTextArea(10, 30);
        taIngredientes.setWrapStyleWord(true);
        taIngredientes.setLineWrap(true);
        taIngredientes.setBackground(new Color(255, 255, 204)); // (amarillo claro)
        taIngredientes.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Borde gris
        taIngredientes.setFont(new Font("Georgia", Font.PLAIN, 16)); 
        
        JScrollPane scrollIngredientes = new JScrollPane(taIngredientes);
        JPanel panelIngredientes = new PanelPersonalizado(new Color(255, 255, 204));  
        panelIngredientes.setLayout(new BorderLayout());
        panelIngredientes.add(scrollIngredientes, BorderLayout.CENTER);
        configurarMouseListener();

        // Título de "Ingredientes" 
        JLabel labelIngredientes = new JLabel("Ingredientes", SwingConstants.CENTER);
        labelIngredientes.setIcon(new ImageIcon(getClass().getResource("/resources/mezcla.png")));
        labelIngredientes.setFont(new Font("Georgia", Font.BOLD, 22));
        labelIngredientes.setIconTextGap(10); // Espacio entre el icono y el texto
        labelIngredientes.setBackground(new Color(255, 255, 255));
        labelIngredientes.setOpaque(true); 
        panelIngredientes.add(labelIngredientes, BorderLayout.NORTH);

        // Lista de recetas
        listaRecetas = new JList<>(modeloRecetas);
        listaRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listaRecetas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                cargarRecetaSeleccionada();
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaRecetas);

        // Cambiar el color al pasar el ratón por encima en la lista de recetas
        listaRecetas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(new Color(255, 204, 229));  // Color rosa cuando está seleccionado
                    setForeground(Color.BLACK);  // Texto en negro cuando está seleccionado
                } else {
                    setBackground(Color.WHITE);  // Fondo blanco por defecto
                    setForeground(Color.BLACK);  // Texto en negro
                }
                return this;
            }
        });

        // Panel para las recetas "Últimas recetas"
        JPanel panelRecetas = new PanelPersonalizado(new Color(255, 204, 229));  // Color rosa
        panelRecetas.setLayout(new BorderLayout());
        listaRecetas.setFont(new Font("Georgia", Font.PLAIN, 16));

        // Título "Últimas Recetas" 
        JLabel labelRecetas = new JLabel("Últimas recetas", SwingConstants.CENTER);
        labelRecetas.setIcon(new ImageIcon(getClass().getResource("/resources/ingredientes.png")));
        labelRecetas.setFont(new Font("Georgia", Font.BOLD, 22));
        labelRecetas.setIconTextGap(10);
        labelRecetas.setBackground(new Color(255, 204, 229));  // Fondo rosa para el título
        labelRecetas.setOpaque(true);  
        panelRecetas.add(labelRecetas, BorderLayout.NORTH);
        panelRecetas.add(scrollLista, BorderLayout.CENTER);

        // Crear menú y barra de herramientas
        crearMenu();
        crearToolBar();

        // Layout principal con GridLayout
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 3, 10, 10));
        panelPrincipal.add(panelRecetas);
        panelPrincipal.add(panelPostres);
        panelPrincipal.add(panelIngredientes);

        // Agregar el panel principal a la ventana
        add(panelPrincipal);
        setJMenuBar(menuBar);
        add(toolBar, BorderLayout.NORTH);
        setVisible(true);
    }
    /***********************************Crear barra de herramientas***********************************************************************/
    
    private void crearToolBar() {
        // Barra de herramientas superior 
        toolBar = new JToolBar();

        // Crear los botones (parte izquierda)
        btnNuevo = crearBoton("/images/nuevo.png", "Nuevo");
        btnGuardar = crearBoton("/images/guardar.png", "Guardar");
        btnCargar = crearBoton("/images/cargar.png", "Cargar archivo de recetas");
        btnGenerarArchivo = crearBoton("/images/generar.png", "Generar archivo de texto");
        btnAyuda = crearBoton("/images/ayuda.png", "Ver la ayuda");

        // Agregar botones a la barra de herramientas
        toolBar.add(btnNuevo);
        toolBar.add(btnGuardar);

        // Agregar un separador entre los botones "Guardar" y "Cargar"
        toolBar.addSeparator();

        toolBar.add(btnCargar);
        toolBar.add(btnGenerarArchivo);
        toolBar.addSeparator();
        toolBar.add(btnAyuda);

        // Crear un JPanel para los botones a la derecha
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Crear los últimos botones (se colocan a la derecha)
        btnAgregarIngrediente = crearBoton("/images/agregarIngrediente.png", "Agregar Ingrediente");
        btnNombrePostre = crearBoton("/images/postre.png", "Ingresar Nombre Postre");
        btnTipoPostre = crearBoton("/images/tipo.png", "Seleccionar Tipo Postre");
        btnCategoriaPostre = new JButton(new ImageIcon(getClass().getResource("/images/categoria.png")));
        
        btnCategoriaPostre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSelectorCategorias();
            }
        });
        btnCategoriaPostre.setToolTipText("Seleccionar Categoría Postre");

        // Botón personalizado para "Ver Recetas Predeterminadas"
        BotonPersonalizado btnVerReceta = new BotonPersonalizado("Ver Recetas");
        btnVerReceta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRecetaPredeterminada();
            }
        });
        btnVerReceta.setToolTipText("Ver Recetas Predeterminadas");

        // Agregar los botones al panel derecho
        rightPanel.add(btnNombrePostre);
        rightPanel.add(btnTipoPostre);
        rightPanel.add(btnCategoriaPostre);
        rightPanel.add(btnAgregarIngrediente);
        rightPanel.add(btnVerReceta); // Agregar el botón personalizado

        toolBar.add(Box.createHorizontalGlue());  // Empujar los botones a la derecha
        toolBar.add(rightPanel);

        // La barra de herramientas se agrega al contenedor (arriba)
        this.add(toolBar, BorderLayout.NORTH);

        // Barra de herramientas inferior con imagen de fondo
        JToolBar bottomToolBar = new JToolBar() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cargar la imagen de fondo
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/fondo.png"));
                if (imageIcon.getImage() != null) {
                    g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("Imagen no encontrada.");
                }
            }
        };

        // Ajustar el tamaño de la barra inferior
        bottomToolBar.setPreferredSize(new Dimension(0, 150)); // Ajusta la altura de la barra inferior
        bottomToolBar.setFloatable(false); // Evitar que la barra sea movible
        this.add(bottomToolBar, BorderLayout.SOUTH);  // Colocarla en la parte inferior de la ventana

        // Configurar las áreas de texto con la altura ajustada
        taPostres.setPreferredSize(new Dimension(300, 200));  // Ajusta el tamaño de los text areas
        taIngredientes.setPreferredSize(new Dimension(300, 200));  // Ajusta el tamaño de los text areas
    }

    
    private JButton crearBoton(String rutaIcono, String toolTip) {
        JButton boton = new JButton(new ImageIcon(getClass().getResource(rutaIcono)));
        boton.addActionListener(this);
        boton.setToolTipText(toolTip);
        return boton;
    }


/************Crear Menú*************************************************************************************************/
    private void crearMenu() {
        
        Font fontMenu = new Font("Georgia", Font.PLAIN, 16); 
        UIManager.put("Menu.font", fontMenu);
        UIManager.put("MenuItem.font", fontMenu);
        UIManager.put("CheckBoxMenuItem.font", fontMenu);
        UIManager.put("RadioButtonMenuItem.font", fontMenu);
        menuBar = new JMenuBar();

        // Crear íconos
        ImageIcon iconoNuevo = new ImageIcon(getClass().getResource("/images/16x16/nuevo.png"));
        ImageIcon iconoGuardar = new ImageIcon(getClass().getResource("/images/16x16/guardar.png"));
        ImageIcon iconoCargar = new ImageIcon(getClass().getResource("/images/16x16/cargar.png"));
        ImageIcon iconoGenerar = new ImageIcon(getClass().getResource("/images/16x16/txt.png"));
        ImageIcon iconoPostre = new ImageIcon(getClass().getResource("/images/16x16/postre.png"));
        ImageIcon iconoTipo = new ImageIcon(getClass().getResource("/images/16x16/tipo.png"));
        ImageIcon iconoCategoria = new ImageIcon(getClass().getResource("/images/16x16/categoria.png"));
        ImageIcon iconoColor = new ImageIcon(getClass().getResource("/images/16x16/color.png"));
        ImageIcon iconoAyuda = new ImageIcon(getClass().getResource("/images/16x16/ayuda.png"));
        ImageIcon acercaDe = new ImageIcon(getClass().getResource("/images/16x16/acerca-de.png"));

        // Menú Archivo
        menuArchivo = new JMenu("Archivo");
        miNuevo = new JMenuItem("Nuevo", iconoNuevo);
        // Añadir tecla aceleradora a la opción “Nuevo” - Pulsamos CTRL+N 
        miNuevo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        // Añadimos Mnemonics a la opción Menú Archivo - Pulsamos ALT+A para poder ejecutarla (Abrir Menú)
        menuArchivo.setMnemonic(KeyEvent.VK_A);
        miNuevo.addActionListener(this);

        miGuardar = new JMenuItem("Guardar", iconoGuardar);
        // Añadir tecla aceleradora a la opción “Guardar” - Pulsamos CTRL+S 
        miGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        miGuardar.addActionListener(this);

        miCargar = new JMenuItem("Cargar", iconoCargar);
        // Añadir tecla aceleradora a la opción “Cargar” - Pulsamos CTRL+G 
        miCargar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        miCargar.addActionListener(this);

        miGenerarArchivo = new JMenuItem("Generar archivo", iconoGenerar);
        // Añadir tecla aceleradora a la opción “Generar” - Pulsamos CTRL+T 
        miGenerarArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        miGenerarArchivo.addActionListener(this);

        menuArchivo.add(miNuevo);
        menuArchivo.add(miGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(miCargar);
        menuArchivo.add(miGenerarArchivo);

        // Menú Postres
        menuPostres = new JMenu("Postres");
        // Añadimos Mnemonics a la opción Menú Postres - Pulsamos ALT+P para poder ejecutarla (Abrir Menú)
        menuPostres.setMnemonic(KeyEvent.VK_P);

        miNombrePostre = new JMenuItem("Nombre", iconoPostre);
        miNombrePostre.addActionListener(this);

        // Crear submenú para "Tipo de Postre"
        JMenu tipoPostreSubMenu = new JMenu("Tipo");
        tipoPostreSubMenu.setIcon(iconoTipo);

        // Crear los JRadioButtonMenuItem (opciones de tipo de postre)
        JRadioButtonMenuItem rbTarta = new JRadioButtonMenuItem("Tarta");
        JRadioButtonMenuItem rbGalletas = new JRadioButtonMenuItem("Galletas");
        JRadioButtonMenuItem rbCupcake = new JRadioButtonMenuItem("Cupcake");
        JRadioButtonMenuItem rbBrownie = new JRadioButtonMenuItem("Brownie");

        // Crear un ButtonGroup para que solo se pueda seleccionar uno a la vez
        ButtonGroup grupoTipos = new ButtonGroup();
        grupoTipos.add(rbTarta);
        grupoTipos.add(rbGalletas);
        grupoTipos.add(rbCupcake);
        grupoTipos.add(rbBrownie);

        // Añadir los JRadioButtonMenuItem al submenú
        tipoPostreSubMenu.add(rbTarta);
        tipoPostreSubMenu.add(rbGalletas);
        tipoPostreSubMenu.add(rbCupcake);
        tipoPostreSubMenu.add(rbBrownie);

        // Agregar un ActionListener para manejar la selección del tipo de postre
        rbTarta.addActionListener(e -> {
            tipoPostre = "Tarta";  // Actualizar el tipo de postre
            actualizarVistaPostre();  // Actualizar la vista
        });
        rbGalletas.addActionListener(e -> {
            tipoPostre = "Galletas";
            actualizarVistaPostre();
        });
        rbCupcake.addActionListener(e -> {
            tipoPostre = "Cupcake";
            actualizarVistaPostre();
        });
        rbBrownie.addActionListener(e -> {
            tipoPostre = "Brownie";
            actualizarVistaPostre();
        });

        // Añadir el submenú "Tipo de Postre" al menú "Postres"
        menuPostres.add(miNombrePostre);
        menuPostres.add(tipoPostreSubMenu);

        // Crear submenú para "Categoría de Postre"
        JMenu categoriaPostreSubMenu = new JMenu("Categoría");
        categoriaPostreSubMenu.setIcon(iconoCategoria);

        // Crear JMenuItems para categorías 
        JMenuItem miFestividades = new JMenuItem("Festividades");
        JMenuItem miEstacionAno = new JMenuItem("Estación del Año");
        JMenuItem miSaludables = new JMenuItem("Saludables");
        JMenuItem miClasicos = new JMenuItem("Clásicos");
        JMenuItem miInnovadores = new JMenuItem("Innovadores");
        JMenuItem miPostresRapidos = new JMenuItem("Postres Rápidos");

        // Añadir los JMenuItems al submenú de categoría
        categoriaPostreSubMenu.add(miFestividades);
        categoriaPostreSubMenu.add(miEstacionAno);
        categoriaPostreSubMenu.add(miSaludables);
        categoriaPostreSubMenu.add(miClasicos);
        categoriaPostreSubMenu.add(miInnovadores);
        categoriaPostreSubMenu.add(miPostresRapidos);

        // Agregar ActionListener para actualizar la categoría
        miFestividades.addActionListener(e -> {
            categoriaPostre = "Festividades";  // Actualizar la categoría
            actualizarVistaPostre();  // Actualizar la vista
        });
        miEstacionAno.addActionListener(e -> {
            categoriaPostre = "Estación del Año";
            actualizarVistaPostre();
        });
        miSaludables.addActionListener(e -> {
            categoriaPostre = "Saludables";
            actualizarVistaPostre();
        });
        miClasicos.addActionListener(e -> {
            categoriaPostre = "Clásicos";
            actualizarVistaPostre();
        });
        miInnovadores.addActionListener(e -> {
            categoriaPostre = "Innovadores";
            actualizarVistaPostre();
        });
        miPostresRapidos.addActionListener(e -> {
            categoriaPostre = "Postres Rápidos";
            actualizarVistaPostre();
        });

        // Añadir el submenú "Categoría de Postre" al menú "Postres"
        menuPostres.add(categoriaPostreSubMenu);

        // Menú Ingredientes
        menuIngredientes = new JMenu("Ingredientes");
        // Añadimos Mnemonics a la opción Menú Ingredientes - Pulsamos ALT+I para poder ejecutarla (Abrir Menú)
        menuIngredientes.setMnemonic(KeyEvent.VK_I);

        // Submenú para agregar ingredientes
        miAgregarIngrediente = new JMenu("Agregar");
        miAgregarIngrediente.setIcon(new ImageIcon(getClass().getResource("/images/16x16/agregarIngredientes.png")));
        menuIngredientes.add(miAgregarIngrediente);

        // Crear opciones con JCheckBoxMenuItem para cada ingrediente
        String[] listaIngredientes = {"Mascarpone", "Azúcar", "Harina", "Galleta", "Mantequilla", "Huevos", "Chocolate", "Levadura", "Leche"};
        checkIngredientes = new ArrayList<>(); 

        for (String ingrediente : listaIngredientes) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(ingrediente);
            item.addActionListener(this);
            checkIngredientes.add(item);
            miAgregarIngrediente.add(item); // Añadimos el CheckBox al submenú
        }

        // Agregar el menú de ingredientes al menú principal
        menuBar.add(menuIngredientes);

        // Menú Color
        menuColor = new JMenu("Personalización");
        // Añadimos Mnemonics a la opción Menú Color - Pulsamos ALT+C para poder ejecutarla (Abrir Menú)
        menuColor.setMnemonic(KeyEvent.VK_C);
        miEscogecolor = new JMenuItem("Escoge el color del texto", iconoColor);
        miEscogecolor.addActionListener(this);
        menuColor.add(miEscogecolor);
        
       // Menú Ayuda
        menuAyuda = new JMenu("Ayuda");
        // Añadimos Mnemonics a la opción Menú Ayuda - Pulsamos ALT+Y para poder ejecutarla (Abrir Menú)
        menuAyuda.setMnemonic(KeyEvent.VK_Y);

        // Ítem de menú "Acerca de..."
        miAcercaDe = new JMenuItem("Acerca de...", acercaDe);
        miVerAyuda = new JMenuItem("Ver la ayuda", iconoAyuda);
        miVerAyuda.addActionListener(e -> mostrarAyuda());

        // Añadir acción al menú "Acerca de..."
        miAcercaDe.addActionListener(e -> {
            // Crear el ícono para el cuadro de diálogo
            ImageIcon iconoDialogo = new ImageIcon(getClass().getResource("/images/reposteria.png"));

            // Mostrar el cuadro de diálogo con el ícono personalizado
            JOptionPane.showMessageDialog(
                    this, 
                    "Pastelería Creativa\nVersión 1.0\nDesarrollado por María Rosete", 
                    "Acerca de...", 
                    JOptionPane.INFORMATION_MESSAGE, 
                    iconoDialogo
            );
        });

        // Añadir tecla aceleradora a la opción "Acerca de..." - Pulsamos CTRL+H
        miAcercaDe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));

        // Añadir la opción "Acerca de..." al menú Ayuda
        menuAyuda.add(miAcercaDe);
        // Añadir el ítem "Ver la ayuda" al menú Ayuda
        menuAyuda.add(miVerAyuda);

        // Agregar menús a la barra de menús
        menuBar.add(menuArchivo);
        menuBar.add(menuPostres);
        menuBar.add(menuIngredientes);
        menuBar.add(menuColor);
        menuBar.add(menuAyuda);
    }

    private void mostrarAyuda() {
                
        try {
            // Ruta al archivo de ayuda
            File fichero = new File("./help/help_set.hs"); 
            // Construir la URL desde el archivo de ayuda
            URL hsURL = fichero.toURI().toURL();
            // Crear el HelpSet con el archivo de ayuda
            HelpSet helpSet = new HelpSet(getClass().getClassLoader(), hsURL);
            // Crear el HelpBroker
            HelpBroker hb = helpSet.createHelpBroker();

            // Muestra la ayuda al hacer clic en el menú "Ver la ayuda"
            hb.enableHelpOnButton(miVerAyuda, "inicio", helpSet);
            //Muestra la ayuda al pulsar F1 sobre la ventana principal                   
            hb.enableHelpKey(getRootPane(), "inicio", helpSet);
            //Muestra la ayuda al hacer clic sobre el botón ?
            hb.enableHelpOnButton(btnAyuda, "inicio", helpSet);
          
        } catch (Exception e) {
            e.printStackTrace();  
            JOptionPane.showMessageDialog(this, "Error al cargar la ayuda.");
        }
    }
    /**********Método para mostrar el selector de categorías************************************************************************/   
        private void mostrarSelectorCategorias() {
        
        String[] categorias = {"Festividades", "Estación del Año", "Saludables", "Clásicos", "Innovadores", "Postres Rápidos"};
        JComboBox<String> comboCategorias = new JComboBox<>(categorias);
        ImageIcon icono = new ImageIcon(getClass().getResource("/images/categoria.png"));
        Image imagenEscalada = icono.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        icono = new ImageIcon(imagenEscalada);

        int opcion = JOptionPane.showConfirmDialog(
            this,
            comboCategorias,
            "Seleccionar Categoría",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,  
            icono  
        );

        if (opcion == JOptionPane.OK_OPTION) {
            // Obtener la categoría seleccionada
            categoriaPostre = (String) comboCategorias.getSelectedItem();

            // Llamar al método para actualizar el área de texto de los postres
            actualizarVistaPostre();
        }
    }

/******** Método para eliminar el ingrediente seleccionado*************************************************************/   
    private void eliminarIngrediente() {
        // Obtener el texto seleccionado en el JTextArea
            String ingredienteSeleccionado = taIngredientes.getSelectedText();

            // Verificamos si hay texto seleccionado
            if (ingredienteSeleccionado != null && !ingredienteSeleccionado.isEmpty()) {
                // Eliminar el ingrediente del string ingredientesPostre 
                ingredientesPostre = ingredientesPostre.replace(ingredienteSeleccionado + ", ", "")
                                                        .replace(", " + ingredienteSeleccionado, "") 
                                                        .replace(ingredienteSeleccionado, ""); 

                // Actualizar el mapa de ingredientes con el nuevo string
                ingredientesMap.put(nombrePostre, ingredientesPostre);

                // Actualizar el JTextArea de ingredientes
                taIngredientes.setText(ingredientesPostre);

                // Opcional: Mensaje de confirmación
                JOptionPane.showMessageDialog(this, "Ingrediente eliminado: " + ingredienteSeleccionado);
            } else {
                // Si no hay texto seleccionado
                JOptionPane.showMessageDialog(this, "Seleccione un ingrediente para eliminar.");
            }
    }        
  /*********************Método para agregar el MouseListener al JTextArea******************************************/  
    private void configurarMouseListener() {
        taIngredientes.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        mostrarPopupMenu(e); 
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        mostrarPopupMenu(e);  
                    }
                }
            });
    }
        
    /******** Método para limpiar todos los ingredientes************************************************************/   
    private void limpiarTodosIngredientes() {
        // Confirmar con el usuario antes de borrar
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar todos los ingredientes?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                ingredientesPostre = "";  
                ingredientesMap.put(nombrePostre, ingredientesPostre);  // Actualizar el mapa
                taIngredientes.setText(ingredientesPostre);  // Actualizar el JTextArea
                JOptionPane.showMessageDialog(this, "Todos los ingredientes han sido eliminados.");
            }
    } 
                
    /*********Mostrar el PopUp Menú*********************************************************************************/    
    private void mostrarPopupMenu(MouseEvent e) {
        // Crear el popup menu
            JPopupMenu popupMenu = new JPopupMenu();

            // Crear la opción de eliminar
            JMenuItem eliminarItem = new JMenuItem("Eliminar");

            // Acción para eliminar el ingrediente
            eliminarItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eliminarIngrediente();  
                }
            });
            popupMenu.add(eliminarItem);  
            
            // Añadir un separador entre las opciones
            popupMenu.addSeparator();  

            // Crear la opción de limpiar todos los ingredientes
            JMenuItem limpiarTodosItem = new JMenuItem("Limpiar Todos");

            limpiarTodosItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    limpiarTodosIngredientes();  
                }
            });
            popupMenu.add(limpiarTodosItem); 

            
            // Mostrar el menú en la posición del clic
           popupMenu.show(e.getComponent(), e.getX(), e.getY());    
    }
    
/*********Método para ver la receta****************************************************************/
    private void cargarRecetaSeleccionada() {
        String seleccion = listaRecetas.getSelectedValue();
        if (seleccion != null) {
            taPostres.setText(recetasMap.get(seleccion));
            taIngredientes.setText(ingredientesMap.getOrDefault(seleccion, ""));
        }
    }
 /**********Método para Ver Recetas Predeterminadas*************************************************/   
   private void mostrarRecetaPredeterminada() {
    // Definir los datos de las recetas predeterminadas
    String[] nombresRecetas = {"Tarta de Fresa", "Pastel de Chocolate", "Galletas de Mantequilla"};
    String[] tiposRecetas = {"Cupcake", "Pastel", "Galletas"};
    String[] categoriasRecetas = {"Festividades", "Cumpleaños", "Navidad"};
    String[] ingredientesRecetas = {"Fresas, Harina, Azúcar, Huevos, Mantequilla","Chocolate, Harina, Azúcar, Huevos, Leche","Mantequilla, Azúcar, Harina, Huevos"};

    // Limpiar los JTextArea antes de mostrar nuevas recetas
    taPostres.setText("");
    taIngredientes.setText("");

    // Agregar las recetas a la lista de últimas recetas 
    for (int i = 0; i < nombresRecetas.length; i++) {
        // Añadir la receta a la lista
        modeloRecetas.addElement(nombresRecetas[i]);
        
        // Agregar las recetas al mapa para poder acceder a los detalles más tarde
        recetasMap.put(nombresRecetas[i], "Nombre: " + nombresRecetas[i] + "\nTipo: " + tiposRecetas[i] + "\nCategoría: " + categoriasRecetas[i]);
        ingredientesMap.put(nombresRecetas[i], ingredientesRecetas[i]);
    }
}
   /******************Eventos de escucha*********************************************************************************/    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Comprobamos si el evento proviene de un botón de la barra de herramientas
        if (e.getSource() == btnNuevo || e.getSource() == miNuevo) {
            nombrePostre = "";
            tipoPostre = "";
            categoriaPostre = "";
            ingredientesPostre = "";
            taPostres.setText("");
            taIngredientes.setText("");
        
        } else if (e.getSource() == btnGuardar || e.getSource() == miGuardar) {
            if (nombrePostre.isEmpty() || tipoPostre.isEmpty() || categoriaPostre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el nombre, tipo y categoría del postre.");
                return;
            }
            String contenidoPostre = "Nombre: " + nombrePostre + "\nTipo: " + tipoPostre + "\nCategoría: " + categoriaPostre;
            recetasMap.put(nombrePostre, contenidoPostre);
            ingredientesMap.put(nombrePostre, ingredientesPostre);

            if (!modeloRecetas.contains(nombrePostre)) {
                modeloRecetas.addElement(nombrePostre);
            }

            JOptionPane.showMessageDialog(this, "Receta guardada.");
        
        } else if (e.getSource() == btnCargar || e.getSource() == miCargar) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                cargarDesdeArchivo(archivo);
            }
        
        } else if (e.getSource() == btnGenerarArchivo || e.getSource() == miGenerarArchivo) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                guardarArchivo(archivo);
            }
        
        } else if (e.getSource() == btnAgregarIngrediente || e.getSource() == miAgregarIngrediente) {
            mostrarDialogoIngredientes(); 
        
        } else if (e.getSource() == btnNombrePostre || e.getSource() == miNombrePostre) {
            
            ImageIcon icono = new ImageIcon(getClass().getResource("/images/postre.png"));
            Image imagenEscalada = icono.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icono = new ImageIcon(imagenEscalada);

            // Mostrar el cuadro de diálogo con el ícono redimensionado
            nombrePostre = (String) JOptionPane.showInputDialog(
                this,
                "Ingrese el nombre:",
                "Nombre del Postre",
                JOptionPane.QUESTION_MESSAGE,
                icono, 
                null,  // No hay opciones, solo el campo de texto
                ""
            );

            // Actualizar la vista del postre con el nombre ingresado
            actualizarVistaPostre();
        
        } else if (e.getSource() == btnTipoPostre || e.getSource() == miTipoPostre) {
            // Opciones para los tipos de postre
            String[] tipos = {"Tarta", "Galletas", "Cupcake", "Brownie"};
            String[] iconosPath = {
                "/images/16x16/pastel.png", 
                "/images/galletas.png", 
                "/images/cupcake.png", 
                "/images/16x16/brownie.png"
            };

            // Crear un panel para mostrar los radio buttons
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical layout para los radio buttons

            // Crear un ButtonGroup para agrupar los JRadioButton
            ButtonGroup grupoTipos = new ButtonGroup();

            // Crear los JRadioButton para cada tipo de postre
            JRadioButton rbTarta = new JRadioButton("Tarta");
            JRadioButton rbGalletas = new JRadioButton("Galletas");
            JRadioButton rbCupcake = new JRadioButton("Cupcake");
            JRadioButton rbBrownie = new JRadioButton("Brownie");

            // Asignar iconos a los JRadioButton
            rbTarta.setIcon(new ImageIcon(getClass().getResource(iconosPath[0])));
            rbGalletas.setIcon(new ImageIcon(getClass().getResource(iconosPath[1])));
            rbCupcake.setIcon(new ImageIcon(getClass().getResource(iconosPath[2])));
            rbBrownie.setIcon(new ImageIcon(getClass().getResource(iconosPath[3])));

            // Agregar los radio buttons al grupo
            grupoTipos.add(rbTarta);
            grupoTipos.add(rbGalletas);
            grupoTipos.add(rbCupcake);
            grupoTipos.add(rbBrownie);

            // Establecer el primer radio button como seleccionado por defecto
            rbTarta.setSelected(true);

            // Agregar los botones al panel
            panel.add(rbTarta);
            panel.add(rbGalletas);
            panel.add(rbCupcake);
            panel.add(rbBrownie);

            // Mostrar el cuadro de diálogo con los radio buttons
            int option = JOptionPane.showConfirmDialog(
                this, 
                panel, 
                "Seleccione el tipo de postre", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE
            );

            // Si el usuario hizo clic en OK
            if (option == JOptionPane.OK_OPTION) {
                // Determinar cuál radio button está seleccionado
                if (rbTarta.isSelected()) {
                    tipoPostre = "Tarta";
                } else if (rbGalletas.isSelected()) {
                    tipoPostre = "Galletas";
                } else if (rbCupcake.isSelected()) {
                    tipoPostre = "Cupcake";
                } else if (rbBrownie.isSelected()) {
                    tipoPostre = "Brownie";
                }

                // Actualizar la vista del postre seleccionado
                actualizarVistaPostre();  
            }
        
        } else if (e.getSource() == btnCategoriaPostre || e.getSource() == miCategoriaPostre) {
           
            actualizarVistaPostre();
        
        } else if (e.getSource() == miEscogecolor) {

            Color colorSeleccionado = JColorChooser.showDialog(this, "Seleccione color de texto para los postres", taPostres.getForeground());
            Color colorSeleccionado2 = JColorChooser.showDialog(this, "Seleccione color de texto para los ingredientes",taIngredientes.getForeground());

            if (colorSeleccionado != null) {
                taPostres.setForeground(colorSeleccionado);
            }
            if (colorSeleccionado2 != null) {
                taIngredientes.setForeground(colorSeleccionado2);
            }

        } else {
            // Manejo de selección de ingredientes con JCheckBoxMenuItem
            if (checkIngredientes != null) { 
                for (JCheckBoxMenuItem item : checkIngredientes) { 
                    if (e.getSource() == item) {
                        if (item.isSelected()) {
                            // Agregar ingrediente al string de ingredientes
                            ingredientesPostre += item.getText() + ", ";
                        } else {
                            // Quitar ingrediente del string si se deselecciona
                            ingredientesPostre = ingredientesPostre.replace(item.getText() + ", ", "");
                        }

                        // Sincronizar los ingredientes con el mapa del postre actual
                        ingredientesMap.put(nombrePostre, ingredientesPostre);

                        // Actualizar directamente el área de texto de ingredientes
                        taIngredientes.setText(ingredientesPostre);
                        break; 
                    }
                }
            }
        }
    }

/***********Mostrar Cuadro de Diálogo para añadir Ingredientes**********************************************************************/
    private void mostrarDialogoIngredientes() {
        // Ingredientes disponibles con sus respectivas imágenes
        String[] ingredientes = {"Mascarpone", "Azúcar", "Harina", "Galleta", "Mantequilla", "Huevos","Chocolate","Levadura","Leche","Nata"};
        String[] rutasImagenes = {
            "/images/mascarpone.png",
            "/images/azucar.png",
            "/images/harina.png",
            "/images/galleta.png",
            "/images/mantequilla.png",
            "/images/huevos.png",
            "/images/chocolate.png",
            "/images/levadura.png",
            "/images/leche.png",
            "/images/nata.png"
        };

        // Crear un diálogo personalizado
        JDialog dialogo = new JDialog(this, "Agregar Ingrediente", true);
        dialogo.setSize(400, 450);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        // Panel para contener la lista de ingredientes
        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));

        for (int i = 0; i < ingredientes.length; i++) {
            // Panel para cada ingrediente
            JPanel panelIngrediente = new JPanel(new BorderLayout());
            panelIngrediente.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Espaciado

            // Etiqueta con la imagen del ingrediente
            ImageIcon icono = new ImageIcon(getClass().getResource(rutasImagenes[i]));
            if (icono.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.out.println("Error cargando imagen: " + rutasImagenes[i]);
            }
            Image img = icono.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            JLabel labelImagen = new JLabel(new ImageIcon(img));

            // Etiqueta con el nombre del ingrediente
            JLabel labelNombre = new JLabel(ingredientes[i]);
            labelNombre.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Espaciado entre imagen y texto

            // Botón con la imagen de "Agregar"
            JButton botonAgregar = new JButton();
            botonAgregar.setBorderPainted(false); // Sin borde
            botonAgregar.setContentAreaFilled(false); // Sin fondo
            botonAgregar.setFocusPainted(false); // Sin efecto de foco
            botonAgregar.setIcon(new ImageIcon(getClass().getResource("/images/agregar.png")));

            // Listener para agregar el ingrediente
            final int index = i; // Necesitamos una variable final para usarla en el listener
            botonAgregar.addActionListener(ev -> {
                // Agregar el ingrediente seleccionado
                ingredientesPostre += ingredientes[index] + "\n";
                taIngredientes.setText(ingredientesPostre);
            });

            // Agregar componentes al panel del ingrediente
            panelIngrediente.add(labelImagen, BorderLayout.WEST);
            panelIngrediente.add(labelNombre, BorderLayout.CENTER);
            panelIngrediente.add(botonAgregar, BorderLayout.EAST);

            // Agregar el panel del ingrediente al panel principal
            panelLista.add(panelIngrediente);
        }

        // ScrollPane para permitir desplazamiento si hay muchos ingredientes
        JScrollPane scrollPane = new JScrollPane(panelLista);
        dialogo.add(scrollPane, BorderLayout.CENTER);

        // Botón para cerrar el diálogo
        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.addActionListener(e -> dialogo.dispose());
        dialogo.add(botonCerrar, BorderLayout.SOUTH);

        dialogo.setVisible(true);
    }

 /************Método para actualizar el área de texto de los postres*****************************************************/   
    private void actualizarVistaPostre() {
        // Actualizar el área de texto de los postres con nombre, tipo y categoría
        taPostres.setText("Nombre: " + nombrePostre +
                (tipoPostre.isEmpty() ? "" : "\nTipo: " + tipoPostre) +
                (categoriaPostre.isEmpty() ? "" : "\nCategoría: " + categoriaPostre));

        // Actualizar los ingredientes asociados al postre actual
        ingredientesPostre = ingredientesMap.getOrDefault(nombrePostre, ""); // Recuperar del mapa o inicializar vacío
        taIngredientes.setText(ingredientesPostre); // Actualizar el área de texto de ingredientes
    }

/***********Cargar archivo .txt******************************************************************************************* */

    private void cargarDesdeArchivo(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String nombre = "", tipo = "", categoria = "", ingredientes = "";

            while ((linea = br.readLine()) != null) {
               
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                if (linea.startsWith("Nombre:")) {
                    // Guardar receta previa si existe
                    if (!nombre.isEmpty()) {
                        // Guardar receta completa
                        recetasMap.put(nombre, "Nombre: " + nombre + "\nTipo: " + tipo + "\nCategoría: " + categoria);
                        ingredientesMap.put(nombre, ingredientes);

                        // Agregar nombre al modelo para que aparezca en la lista
                        if (!modeloRecetas.contains(nombre)) {
                            modeloRecetas.addElement(nombre);
                        }
                    }

                    // Comenzar a leer una nueva receta
                    nombre = linea.substring(8).trim();  // Extraemos el nombre
                    tipo = "";  
                    categoria = "";
                    ingredientes = "";
                } else if (linea.startsWith("Tipo:")) {
                    tipo = linea.substring(5).trim();
                } else if (linea.startsWith("Categoría:")) {
                    categoria = linea.substring(11).trim();
                } else {
                    // Asumimos que cualquier línea que no es una de las anteriores son ingredientes
                    if (!ingredientes.isEmpty()) {
                        ingredientes += "\n";  // Añadir salto de línea entre ingredientes
                    }
                    ingredientes += linea;  // Agregar ingrediente a la lista
                }
            }

            // Asegurarnos de guardar la última receta leída
            if (!nombre.isEmpty()) {
                recetasMap.put(nombre, "Nombre: " + nombre + "\nTipo: " + tipo + "\nCategoría: " + categoria);
                ingredientesMap.put(nombre, ingredientes);
                if (!modeloRecetas.contains(nombre)) {
                    modeloRecetas.addElement(nombre);
                }
            }

            JOptionPane.showMessageDialog(this, "Recetas cargadas exitosamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo: " + ex.getMessage());
        }
    }
/************************Guardar Recetas en .txt*****************************************************************/

    private void guardarArchivo(File archivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (String receta : recetasMap.keySet()) {
                String postre = recetasMap.get(receta);
                String ingredientes = ingredientesMap.get(receta);
                bw.write(postre + ";\n" + ingredientes + "\n");
            }
            JOptionPane.showMessageDialog(this, "Archivo generado exitosamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage());
        }
    }

   /***********Main**********************************************************************************************/     
    public static void main(String[] ar) {
        Pasteleria_Creativa pasteleria=new Pasteleria_Creativa();
    }  
}