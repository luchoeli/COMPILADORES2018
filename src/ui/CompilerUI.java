package ui;
import sintacticSource.*;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.GridLayout;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ScrollPaneConstants;

import lexicalSource.LexicalAnalizer;
import lexicalSource.Token;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;



public class CompilerUI {

	private JFrame frmCompiler;
	private JTextArea textArea;
	private JTextArea lines;
	private JTextArea problems;
	private JTextArea console;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompilerUI window = new CompilerUI();
					window.frmCompiler.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		});
	}

	/**
	 * Create the application.
	 */
	public CompilerUI() {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCompiler = new JFrame();
		frmCompiler.setIconImage(Toolkit.getDefaultToolkit().getImage(CompilerUI.class.getResource("/icons/genericregister_obj.gif")));
		frmCompiler.setTitle("Compiler1.0");
		frmCompiler.setBounds(100, 100, 723, 533);
		frmCompiler.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCompiler.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		frmCompiler.getContentPane().add(panel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// -------------------------------- PANEL PROBLEMS ------------------------------------------
		
		JPanel Problems = new JPanel();
		tabbedPane.addTab("Problems", new ImageIcon(CompilerUI.class.getResource("/icons/errorwarning_tab.gif")), Problems, null);
		
		JScrollPane scrollPane_Problems = new JScrollPane();
		scrollPane_Problems.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		problems = new JTextArea();
		problems.setEditable(false);
		scrollPane_Problems.setViewportView(problems);
		GroupLayout gl_Problems = new GroupLayout(Problems);
		gl_Problems.setHorizontalGroup(
			gl_Problems.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Problems, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		gl_Problems.setVerticalGroup(
			gl_Problems.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Problems.createSequentialGroup()
					.addComponent(scrollPane_Problems, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
					.addGap(0))
		);
		Problems.setLayout(gl_Problems);
		
		// -------------------------------- PANEL CONSOLE ------------------------------------------
		
		JPanel Console = new JPanel();
		tabbedPane.addTab("Console", new ImageIcon(CompilerUI.class.getResource("/icons/console_view.gif")), Console, null);
		
		JScrollPane scrollPane_Console = new JScrollPane();
		scrollPane_Console.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		console = new JTextArea();
		console.setEditable(false);
		scrollPane_Console.setViewportView(console);
		GroupLayout gl_Console = new GroupLayout(Console);
		gl_Console.setHorizontalGroup(
			gl_Console.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Console, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		gl_Console.setVerticalGroup(
			gl_Console.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Console.createSequentialGroup()
					.addComponent(scrollPane_Console, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
					.addGap(0))
		);
		Console.setLayout(gl_Console);
		
		// -------------------------------- PANEL TOKENS ------------------------------------------
		
		JPanel Tokens = new JPanel();
		tabbedPane.addTab("Tokens", null, Tokens, null);
		JScrollPane scrollPane_Tokens = new JScrollPane();
		scrollPane_Tokens.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		String[] colums={"ID","Tipo","Lexema","Linea"};		
		final DefaultTableModel dtm=new DefaultTableModel(null,colums);
		JTable table = new JTable(dtm);
		scrollPane_Tokens.setViewportView(table);
		GroupLayout gl_Tokens = new GroupLayout(Tokens);
		gl_Tokens.setHorizontalGroup(
			gl_Tokens.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Tokens, GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
		);
		gl_Tokens.setVerticalGroup(
			gl_Tokens.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_Tokens, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
		);
		Tokens.setLayout(gl_Tokens);


		
		//-------------------------------------- TEXT AREA -----------------------------------------
		
		JScrollPane scrollPane = new JScrollPane();
		
		textArea = new JTextArea();
		textArea.setEnabled(false);

		//--------------------------------------MENU BAR -------------------------------------------
		JMenuBar menuBar = new JMenuBar();
		frmCompiler.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem triggerOpenFile = new JMenuItem("Open File");
		triggerOpenFile.setIcon(new ImageIcon(CompilerUI.class.getResource("/icons/file_obj.gif")));
		mnFile.add(triggerOpenFile);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmRun = new JMenuItem("Run");
		mntmRun.setIcon(new ImageIcon(CompilerUI.class.getResource("/icons/lrun_obj.gif")));
		mnFile.add(mntmRun);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem triggerExit = new JMenuItem("Exit");
		mnFile.add(triggerExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem triggerHowToUse = new JMenuItem("How to use?");
		triggerHowToUse.setIcon(new ImageIcon(CompilerUI.class.getResource("/icons/help_contents.gif")));
		mnHelp.add(triggerHowToUse);
		
		JMenuItem triggerAbout = new JMenuItem("About");
		mnHelp.add(triggerAbout);
		
		//------------------------------------------- NUMERO DE LINEAS -----------------------------------
		lines = new JTextArea("1");
		 
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);
 
		
		/** LINE NUMBERING
		 * Add Listener al area de escritura (textArea).
		 * Por c/ change, insert y remove en el area de escritura el listener escribe en la column que muestra el nro de linea. 
		 */
		textArea.getDocument().addDocumentListener(new DocumentListener(){
			public String getText(){
				int caretPosition = textArea.getDocument().getLength();
				Element root = textArea.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
					text += i + System.getProperty("line.separator");
				}
				return text;
			}
			@Override
			public void changedUpdate(DocumentEvent de) {
				lines.setText(getText());
			}
 
			@Override
			public void insertUpdate(DocumentEvent de) {
				lines.setText(getText());
			}
 
			@Override
			public void removeUpdate(DocumentEvent de) {
				lines.setText(getText());
			}
 
		});
		scrollPane.setViewportView(textArea);
		scrollPane.setRowHeaderView(lines);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
		//------------------------------------------ BOTONES ---------------------------------------
		/**
		 * ----------- BOTON RUN ---------------
		 */
		final JButton btnRun = new JButton("");
		btnRun.setEnabled(false);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
							
				String program = textArea.getText();
				LexicalAnalizer analizer = new LexicalAnalizer(program);
				int idToken=-1;
				while (idToken!=290){
					idToken=analizer.getToken();
				}
				ArrayList<Token> tokens=analizer.getTokens();
				ArrayList<Integer> linesProblems=analizer.getLinesProblems();
				ArrayList<String> listProblems = analizer.getProblems();
				for(Integer line : linesProblems){
					System.out.println("LINEA "+line);
					drawLine(line-1);
				}
				problems.setText(null);
				for(String problem : listProblems){
					problems.setText(problems.getText() + problem + "\n");
				}
				for(Token token : tokens){
					String[] data=new String[4];
					data[0]=""+token.getId();
					data[1]=token.getType();
					data[2]=token.getLexema();
					data[3]=""+token.getNroLine();
					dtm.addRow(data);
				}
				Parser parser = new Parser();
				parser.Parse();
			}
		});
		btnRun.setIcon(new ImageIcon(CompilerUI.class.getResource("/icons/lrun_obj.gif")));
		
		/**
		 * ----------- BOTON OPEN ---------------
		 */
		final JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setEnabled(true);
				btnRun.setEnabled(true);
				JFileChooser fc = new JFileChooser(new File(""));
				fc.setDialogTitle("Open a File");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Texto", "txt");
				fc.setFileFilter(filter);
				fc.setMultiSelectionEnabled(false);
				int option = fc.showOpenDialog(btnOpen);
				if (option == JFileChooser.APPROVE_OPTION){
					File file =fc.getSelectedFile();
					try {
						BufferedReader br=new BufferedReader(new FileReader(file));
						String text="";
						String line="";
						while ((line=br.readLine())!=null){
							text+=line+"\n";							
						}
						textArea.setText(text);
						if (br!=null){br.close();}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
			}
		});
		btnOpen.setIcon(new ImageIcon(CompilerUI.class.getResource("/icons/file_obj.gif")));
		
		
		/**
		 * ----------- BOTON NEW ---------------
		 */
		JButton btnNewButton = new JButton("New");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setEnabled(true);
				btnRun.setEnabled(true);
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE))
							.addGap(8))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNewButton)
							.addComponent(btnOpen))
						.addComponent(btnRun))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{textArea, btnNewButton, btnOpen, btnRun, tabbedPane, Problems, scrollPane_Problems, problems, Console, scrollPane_Console, console, Tokens, scrollPane_Tokens, table, scrollPane, lines}));
		
	}
	
	/**
	 * Pinta en el area de texto una linea. 
	 * @param numberLine Linea a pintar.
	 */
	private void drawLine(int numberLine){
		Highlighter.HighlightPainter painterTextArea;
		Highlighter.HighlightPainter painterLines;
        try {
        	//PINTA LINEA EN AREA DE TEXTO
			int startLineText = textArea.getLineStartOffset(numberLine);
			int endLineText = textArea.getLineEndOffset(numberLine);
			Color line=new Color(255,120,104);
			painterTextArea = new DefaultHighlighter.DefaultHighlightPainter(line);
			textArea.getHighlighter().addHighlight(startLineText, endLineText, painterTextArea);
			// PINTA LINEA EN LA COLUMNA LINEA
			int startLineLines = lines.getLineStartOffset(numberLine);
			int endLineLines=lines.getLineEndOffset(numberLine);
			painterLines = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
			lines.getHighlighter().addHighlight(startLineLines, endLineLines, painterLines);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
