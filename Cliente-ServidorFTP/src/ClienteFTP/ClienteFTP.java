package ClienteFTP;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.awt.Dimension;


import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Color;

public class ClienteFTP extends JFrame implements ListSelectionListener, MouseListener, ActionListener {

	private String servidor;
	private String user;
	private String pasw;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;;
	private static JList<String> lista;
	private static FTPClient cliente = new FTPClient();// cliente FTP
	private static FTPFile[] files;
	private static String directorioInicial = "/"; //DIRECTORIO CUANDO ARRANCAMOS (ES LA RAIZ DE LA CARPETA COMPARTIDA)
	private static String directorioActual = directorioInicial;//DIRECTORIO SELECCIONADO EN CADA MOMENTO
	private JButton boton_adelantar;
	private JButton boton_refrescar;
	private boolean login;
	private JButton boton_volver;
	private JButton boton_home;
	private static int contador;
	private static String acumulador_directorios[]= new String[50];
	private JLabel laber_directorio;
	private JButton boton_crear_carpeta;
	private JButton boton_eliminar_carpeta;
	private JButton boton_renombrar_carpeta;
	/**
	 * Create the frame.
	 */
	public ClienteFTP() throws IOException
	{
		acumulador_directorios[0] = "/";
		contador = 0;
		servidor = "127.0.0.1";
		user = "david";
		pasw = "Studium2019;";

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 488);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(165, 42, 42));
		contentPane.setForeground(SystemColor.info);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lista = new JList<String>();
		lista.setForeground(new Color(0, 102, 255));
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(lista);
		scrollPane.setPreferredSize(new Dimension(335, 420));
		scrollPane.setBounds(new Rectangle(5, 65, 335, 420));
		scrollPane.setBounds(10, 47, 335, 384);
		contentPane.add(scrollPane);

		boton_crear_carpeta = new JButton("Crear");
		boton_crear_carpeta.setBounds(355, 88, 102, 23);
		contentPane.add(boton_crear_carpeta);

		boton_eliminar_carpeta = new JButton("Eliminar");
		boton_eliminar_carpeta.setBounds(355, 122, 102, 23);
		contentPane.add(boton_eliminar_carpeta);

		boton_renombrar_carpeta = new JButton("Renombrar");
		boton_renombrar_carpeta.setBounds(355, 156, 102, 23);
		contentPane.add(boton_renombrar_carpeta);

		boton_volver = new JButton("");
		boton_volver.setBounds(10, 9, 30, 30);
		boton_volver.setIcon(new javax.swing.ImageIcon(ClienteFTP.class.getResource("/ClienteFTP/volver.png")));
		contentPane.add(boton_volver);

		boton_adelantar = new JButton("");
		boton_adelantar.setBounds(43, 9, 30, 30);
		boton_adelantar.setIcon(new javax.swing.ImageIcon(ClienteFTP.class.getResource("/ClienteFTP/adelantar.png")));
		contentPane.add(boton_adelantar);


		boton_refrescar = new JButton("");
		boton_refrescar.setBounds(105, 9, 30, 30);
		boton_refrescar.setIcon(new javax.swing.ImageIcon(ClienteFTP.class.getResource("/ClienteFTP/actualizar-removebg-preview.png")));
		contentPane.add(boton_refrescar);

		boton_home = new JButton("");
		boton_home.setIcon(new javax.swing.ImageIcon(ClienteFTP.class.getResource("/ClienteFTP/home (1).png")));
		boton_home.setBounds(145, 9, 30, 30);	
		contentPane.add(boton_home);

		JLabel lblNewLabel = new JLabel("CARPETAS");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(173, 255, 47));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 17));
		lblNewLabel.setBounds(355, 36, 102, 23);
		contentPane.add(lblNewLabel);

		JSeparator separator = new JSeparator();
		separator.setBounds(355, 70, 102, 2);
		contentPane.add(separator);

		JLabel lblFicheros = new JLabel("FICHEROS");
		lblFicheros.setHorizontalAlignment(SwingConstants.CENTER);
		lblFicheros.setForeground(new Color(173, 255, 47));
		lblFicheros.setFont(new Font("Arial", Font.BOLD, 17));
		lblFicheros.setBounds(355, 190, 102, 23);
		contentPane.add(lblFicheros);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(355, 224, 102, 2);
		contentPane.add(separator_1);

		JButton button = new JButton("Renombrar");
		button.setBounds(355, 306, 102, 23);
		contentPane.add(button);

		JButton btnBajar = new JButton("Bajar");
		btnBajar.setBounds(355, 272, 102, 23);
		contentPane.add(btnBajar);

		JButton btnSubir = new JButton("Subir");
		btnSubir.setBounds(355, 238, 102, 23);
		contentPane.add(btnSubir);

		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(355, 340, 102, 23);
		contentPane.add(btnBorrar);

		laber_directorio = new JLabel("");
		laber_directorio.setForeground(new Color(173, 255, 47));
		laber_directorio.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		laber_directorio.setBounds(91, 432, 368, 27);
		contentPane.add(laber_directorio);

		JLabel lblDirectorio = new JLabel("Directorio:");
		lblDirectorio.setForeground(new Color(173, 255, 47));
		lblDirectorio.setBackground(SystemColor.textHighlight);
		lblDirectorio.setFont(new Font("Arial", Font.BOLD, 14));
		lblDirectorio.setBounds(10, 432, 101, 27);
		contentPane.add(lblDirectorio);

		//  --  CONECTAMOS AL SERVIDOR -- 

		//Conexión al servidor
		cliente.connect(servidor); 
		cliente.enterLocalPassiveMode();
		login = cliente.login(user, pasw); //Si el boolean login devuelve true, significa que se ha conectado exitosamente.


		// --- RELLENAMOS LA JLIST POR PRIMERA VEZ---

		//Se establece el directorio de trabajo actual
		cliente.changeWorkingDirectory(directorioInicial);
		//Obteniendo ficheros y directorios del directorio actual
		files = cliente.listFiles();
		llenarLista(files);

		//---------------------------------------------


		// --- AÑADIMOS LOS LISTENER---
		boton_renombrar_carpeta.addActionListener(this);
		boton_eliminar_carpeta.addActionListener(this);
		boton_crear_carpeta.addActionListener(this);
		boton_home.addActionListener(this);
		boton_volver.addActionListener(this);
		boton_adelantar.addActionListener(this);
		boton_refrescar.addActionListener(this);
		lista.addMouseListener(this);
		lista.addListSelectionListener(this);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);


	}

	public static void main(String[] args) throws IOException
	{

		new ClienteFTP();

	}

	public static void llenarLista(FTPFile[] files) 
	{
		if (files == null)
			return;

		//Se crea un objeto DefaultListModel
		DefaultListModel<String> modeloLista = new DefaultListModel<String>();
		modeloLista = new DefaultListModel<String>();

		//Se eliminan los elementos de la lista
		lista.removeAll();

		//Se recorre el array con los ficheros y directorios que se cargaran en la lista
		for (int i = 0; i < files.length; i++) 
		{
			if (!(files[i].getName()).equals(".") && !(files[i].getName()).equals("..")) 
			{
				//Nos saltamos los directorios . y ..
				//Se obtiene el nombre del fichero o directorio
				String f = files[i].getName();
				//Si es directorio se añade al nombre (DIR)
				if (files[i].isDirectory()) f = "(DIR) " + f;
				//Se añaade el nombre del fichero o directorio al listmodel
				modeloLista.addElement(f);
			}
		}

		try 
		{
			//Se asigna el modelo al JList
			//Se muestra en pantalla la lista de ficheros
			lista.setModel(modeloLista);

			System.out.println("El directorioSeleccionado vale: " + directorioActual);
		}
		catch (Exception e ) 
		{
			System.out.println(e.getMessage());
		}
	}


	public boolean comprueba_si_es_directorio(String cadena)
	{
		String[] partes = cadena.split(" ");

		if (partes[0].equals("(DIR)")) 
		{
			System.out.println(partes[0]);
			System.out.println(partes[1]);
			return true;
		}
		else 
		{
			return false;
		}

	}

	public void valueChanged(ListSelectionEvent le) 
	{
		if (le.getValueIsAdjusting()) //CADA VEZ QUE PINCHAMOS O CAMBIA EL VALOR SELECCIONADO EN LA LISTA
		{		
			System.out.println("Fichero Seleccionado: "+lista.getSelectedValue().toString());
		}
	}

	@Override
	public void mouseClicked(MouseEvent me) 
	{
		if (me.getClickCount() == 2) //SI EL USUARIO HACE CLIC DOS VECES
		{

			String elementoSeleccionadoEnLaLista = lista.getSelectedValue().toString();

			if (comprueba_si_es_directorio(elementoSeleccionadoEnLaLista))  //COMPROBAMOS SI LO SELECCIONADO ES DE TIPO(DIR)
			{

				String partes [] = elementoSeleccionadoEnLaLista.split(" ");

				//VA ACUMULANDO LAS RUTAS RECORRIDAS POR EL USUARIO
				directorioActual = directorioActual  + partes[1] + "/";	 // partes[1] SELECIONA EL NOMBRE DE LA CARPETA Y ELIMINA EL PREFIJO (DIR)

				System.out.println("El directorio actuar es: " + directorioActual);
				laber_directorio.setText(directorioActual);

				contador++;

				//LO GUARDAMOS EN UN ARRAY PARA LLEVAR EL SEGUIMIENTO DE LOS DIRECTORIOS RECORRIDOS POR EL USUARIO
				acumulador_directorios[contador] = directorioActual;

				try 
				{

					//SE ESTABLECE EL DIRECTORIO ACTUAL DE TRABAJO
					cliente.changeWorkingDirectory(acumulador_directorios[contador]);
					//OBTENER DIRECTORIOS Y FICHEROS 
					files = cliente.listFiles();
					llenarLista(files);

					//---------------------------------------------

				} catch (Exception e) 
				{
					System.out.println(e.getMessage());
				}
			}
		} 
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent ae) 
	{

		// ---------------BOTONES DE ACCIÓN-----------

		if (ae.getSource().equals(boton_crear_carpeta)) 
		{

			String nombreCarpeta = JOptionPane.showInputDialog(null, "Introduce el nombre del directorio","carpeta");

			if (!(nombreCarpeta==null)) 
			{
				String directorio = directorioActual;

				directorio += nombreCarpeta.trim(); //AÑADE EL NOMBRE DE LA CARPETA QUE ESTÁ CREANDO A LA RUTA

				try 
				{
					if (cliente.makeDirectory(directorio))
					{
						String m = nombreCarpeta.trim()+ " => Se ha creado correctamente ...";
						JOptionPane.showMessageDialog(null, m);

						//REFRESCAR LISTA CON LA NUEVA CARPETA
						files = cliente.listFiles();
						llenarLista(files);
						//---------------------------------------------


					}
					else
						JOptionPane.showMessageDialog(null, nombreCarpeta.trim() + " => No se ha podido crear ...");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}

		}

		// -----------------BOTONES DE CABECERA---------------------

		if (ae.getSource().equals(boton_volver)) 
		{
			try 
			{			
				if (contador > 0) 
				{
					contador--;

					System.out.println(acumulador_directorios[contador]);
					directorioActual= acumulador_directorios[contador];
					laber_directorio.setText(directorioActual);
					//SE ESTABLECE EL DIRECTORIO DE TRABAJO ACTUAL
					cliente.changeWorkingDirectory(acumulador_directorios[contador]);
					//OBTENIENDO FICHEROS Y CARPETAS DEL DIRECTORIO
					files = cliente.listFiles();
					llenarLista(files);
					//---------------------------------------------
				}


			} catch (Exception e) 
			{
				System.out.println(e.getMessage());
			}
		}else 
		{
			if (ae.getSource().equals(boton_adelantar)) 
			{

				try 
				{			

					if (acumulador_directorios[contador+1]!= null) 
					{
						contador++;
						System.out.println(acumulador_directorios[contador]);
						directorioActual= acumulador_directorios[contador];
						laber_directorio.setText(directorioActual);
						//SE ESTABLECE EL DIRECTORIO DE TRABAJO ACTUAL
						cliente.changeWorkingDirectory(acumulador_directorios[contador]);
						//OBTENIENDO FICHEROS Y CARPETAS DEL DIRECTORIO
						files = cliente.listFiles();
						llenarLista(files);
						//---------------------------------------------
					}

				} catch (Exception e) 
				{
					System.out.println(e.getMessage());
				}


			}
			else 
			{
				if (ae.getSource().equals(boton_refrescar)) 
				{

					try 
					{
						//OBTENIENDO FICHEROS Y CARPETAS DEL DIRECTORIO
						files = cliente.listFiles();
						llenarLista(files);
						//---------------------------------------------

					} catch (Exception e) 
					{
						System.out.println(e.getMessage());
					}
				}
				else 
				{
					if (ae.getSource().equals(boton_home)) 
					{	
						try 
						{
							acumulador_directorios = new String[50];//REINICIAMOS
							acumulador_directorios[0] = "/";

							directorioActual = directorioInicial; //EL DIRECTORIO ACTUAL PASA A SER LA CARPETA RAÍZ
							laber_directorio.setText(directorioActual);
							contador=0; //REINICIAMOS EL CONTADOR DE RUTAS					
							cliente.changeWorkingDirectory(directorioInicial);//SE ESTABLECE EL DIRECTORIO ACTUAL DE TRABAJO
							//OBTIENE LOS FICHEROS Y ARCHIVOS DEL DIRECTORIO
							files = cliente.listFiles();
							llenarLista(files);//ACTUALIZA LA LISTA
							//---------------------------------------------
						} catch (Exception e) 
						{
							System.out.println(e.getMessage());
						}	
					}
				}
			}
		}
	}
}

