package classe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class BlocoDeNotas {

    private static JFrame frame;
    private static JTextArea textArea;

    public static void main(String[] args) {
        frame = new JFrame("Bloco de Notas");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Notes by NixCodeDev");

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
        frame.getContentPane().add(scrollPane);

        /*Barra de Menus*/
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        /*Menu Arquivo*/
        JMenu arquivoMenu = new JMenu("Arquivo");
        menuBar.add(arquivoMenu);
        
        /*Ítens do Menu Arquivo*/
        JMenuItem novoItem = new JMenuItem("Novo");
        novoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        arquivoMenu.add(novoItem);
        
        JMenuItem abrirItem = new JMenuItem("Abrir", new ImageIcon("caminho/do/icone/open.png"));
        abrirItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        arquivoMenu.add(abrirItem);

        JMenuItem salvarItem = new JMenuItem("Salvar", new ImageIcon("caminho/do/icone/save.png"));
        salvarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        arquivoMenu.add(salvarItem);

        JMenuItem fecharArquivoItem = new JMenuItem("Fechar Arquivo");
        fecharArquivoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        arquivoMenu.add(fecharArquivoItem);

        JMenuItem fecharProgramaItem = new JMenuItem("Fechar");
        fecharProgramaItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        arquivoMenu.add(fecharProgramaItem);

        /*Menu Editar*/
        JMenu editarMenu = new JMenu("Editar");
        menuBar.add(editarMenu);
        
        /*Ítens do Menu Editar*/
        JMenuItem copiarItem = new JMenuItem("Copiar");
        copiarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        editarMenu.add(copiarItem);

        JMenuItem colarItem = new JMenuItem("Colar");
        colarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        editarMenu.add(colarItem);

        JMenuItem localizarItem = new JMenuItem("Localizar");
        localizarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        editarMenu.add(localizarItem);

        novoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Deseja salvar o arquivo atual antes de criar um novo?",
                        "Salvar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    salvarArquivo();
                    criarNovoArquivo();
                } else if (option == JOptionPane.NO_OPTION) {
                    criarNovoArquivo();
                }
            }
        });

        salvarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarArquivo();
            }
        });

        abrirItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Deseja salvar o arquivo atual antes de abrir um novo?",
                        "Salvar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    salvarArquivo();
                    abrirArquivo();
                } else if (option == JOptionPane.NO_OPTION) {
                    abrirArquivo();
                }
            }
        });

        fecharArquivoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Deseja salvar o arquivo antes de fechá-lo?",
                        "Salvar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    salvarArquivo();
                    limparTextArea();
                } else if (option == JOptionPane.NO_OPTION) {
                    limparTextArea();
                }
            }
        });

        fecharProgramaItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fecharPrograma();
            }
        });

        copiarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.copy();
            }
        });

        colarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.paste();
            }
        });

        localizarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchString = JOptionPane.showInputDialog(frame, "Digite o texto a ser localizado:");
                String text = textArea.getText();

                int index = text.indexOf(searchString);
                if (index >= 0) {
                    textArea.requestFocus();
                    textArea.select(index, index + searchString.length());
                } else {
                    JOptionPane.showMessageDialog(frame, "Texto não encontrado.", "Localizar",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    private static void salvarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();

            try {
                FileWriter fileWriter = new FileWriter(filePath);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(textArea.getText());

                bufferedWriter.close();
                fileWriter.close();

                JOptionPane.showMessageDialog(frame, "Arquivo salvo com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Ocorreu um erro ao salvar o arquivo.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void abrirArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();

            try {
                FileReader fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                StringBuilder content = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                bufferedReader.close();
                fileReader.close();

                textArea.setText(content.toString());
                JOptionPane.showMessageDialog(frame, "Arquivo aberto com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Ocorreu um erro ao abrir o arquivo.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void criarNovoArquivo() {
        textArea.setText("");
    }

    private static void limparTextArea() {
        textArea.setText("");
    }

    private static void fecharPrograma() {
        int option = JOptionPane.showConfirmDialog(frame, "Deseja salvar o arquivo antes de fechar o programa?",
                "Salvar", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            salvarArquivo();
            System.exit(0);
        } else if (option == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
}
