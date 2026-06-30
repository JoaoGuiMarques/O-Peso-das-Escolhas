import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogoPersonalizado extends JDialog {

    // ===== cores =====
    private static final Color COR_FUNDO        = new Color(20, 10, 35);
    private static final Color COR_BORDA        = new Color(140, 80, 200);
    private static final Color COR_BORDA_INTERNA = new Color(80, 40, 120);
    private static final Color COR_TEXTO        = new Color(230, 210, 255);
    private static final Color COR_BOTAO_FUNDO  = new Color(60, 20, 100);
    private static final Color COR_BOTAO_HOVER  = new Color(120, 50, 180);
    private static final Color COR_BOTAO_BORDA  = new Color(160, 90, 220);
    private static final Color COR_TITULO       = new Color(200, 150, 255);

    // ===== fonte pixelada =====
    private static final Font FONTE_TITULO  = new Font("Monospaced", Font.BOLD, 16);
    private static final Font FONTE_TEXTO   = new Font("Monospaced", Font.PLAIN, 15);
    private static final Font FONTE_BOTAO   = new Font("Monospaced", Font.BOLD, 14);

    // ===== resultado =====
    private int escolha = -1;

    // ===== construtor da pergunta com opcoes =====
    public DialogoPersonalizado(JFrame pai, String pergunta, String[] opcoes) {
        super(pai, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // ===== fundo =====
                g2.setColor(COR_FUNDO);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // ===== borda externa =====
                g2.setColor(COR_BORDA);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(2, 2, getWidth() - 5, getHeight() - 5);

                // ===== borda interna =====
                g2.setColor(COR_BORDA_INTERNA);
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(6, 6, getWidth() - 13, getHeight() - 13);

                // ===== linha do titulo =====
                g2.setColor(COR_BORDA);
                g2.fillRect(6, 35, getWidth() - 13, 2);
            }
        };
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painelPrincipal.setOpaque(false);

        // ===== título =====
        JLabel titulo = new JLabel("▶ O PESO DAS ESCOLHAS");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // ===== texto da pergunta (dialogo) =====
        JTextArea textoPergunta = new JTextArea(quebrarTexto(pergunta, 55));
        textoPergunta.setFont(FONTE_TEXTO);
        textoPergunta.setForeground(COR_TEXTO);
        textoPergunta.setBackground(COR_FUNDO);
        textoPergunta.setEditable(false);
        textoPergunta.setFocusable(false);
        textoPergunta.setLineWrap(true);
        textoPergunta.setWrapStyleWord(true);
        textoPergunta.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // ===== painel dos botoes =====
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.Y_AXIS));
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        for (int i = 0; i < opcoes.length; i++) {
            final int indice = i;
            JButton botao = criarBotaoOpcao((i + 1) + ". " + opcoes[i]);
            botao.addActionListener(e -> {
                escolha = indice;
                dispose();
            });
            painelBotoes.add(botao);
            painelBotoes.add(Box.createVerticalStrut(6));
        }

        // ===== montagem =====
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setOpaque(false);
        painelTopo.add(titulo, BorderLayout.NORTH);
        painelTopo.add(textoPergunta, BorderLayout.CENTER);

        painelPrincipal.add(painelTopo, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
        setSize(600, 320);
        setLocationRelativeTo(pai);
    }

    // ===== construtor para a mensagem simples =====
    public DialogoPersonalizado(JFrame pai, String mensagem) {
        super(pai, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.setColor(COR_FUNDO);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(COR_BORDA);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(2, 2, getWidth() - 5, getHeight() - 5);

                g2.setColor(COR_BORDA_INTERNA);
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(6, 6, getWidth() - 13, getHeight() - 13);

                g2.setColor(COR_BORDA);
                g2.fillRect(6, 35, getWidth() - 13, 2);
            }
        };
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painelPrincipal.setOpaque(false);

        // ===== titulo caixa de texto =====
        JLabel titulo = new JLabel("▶ O PESO DAS ESCOLHAS");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // ===== mensagem =====
        JTextArea textoMensagem = new JTextArea(mensagem);
        textoMensagem.setFont(FONTE_TEXTO);
        textoMensagem.setForeground(COR_TEXTO);
        textoMensagem.setBackground(COR_FUNDO);
        textoMensagem.setEditable(false);
        textoMensagem.setFocusable(false);
        textoMensagem.setLineWrap(true);
        textoMensagem.setWrapStyleWord(true);
        textoMensagem.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // ===== botao "OK" =====
        JButton botaoOk = criarBotaoOpcao("[ OK ]");
        botaoOk.addActionListener(e -> dispose());

        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotao.setOpaque(false);
        painelBotao.add(botaoOk);

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setOpaque(false);
        painelTopo.add(titulo, BorderLayout.NORTH);
        painelTopo.add(textoMensagem, BorderLayout.CENTER);

        painelPrincipal.add(painelTopo, BorderLayout.CENTER);
        painelPrincipal.add(painelBotao, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
        setSize(420, 220);
        setLocationRelativeTo(pai);
    }

    // ===== criacao botao estilizado =====
    private JButton criarBotaoOpcao(String texto) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                if (getModel().isRollover()) {
                    g2.setColor(COR_BOTAO_HOVER);
                } else {
                    g2.setColor(COR_BOTAO_FUNDO);
                }
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(COR_BOTAO_BORDA);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
                super.paintComponent(g);
            }
        };
        botao.setFont(FONTE_BOTAO);
        botao.setForeground(COR_TEXTO);
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(560, 35));
        botao.setPreferredSize(new Dimension(380, 35));
        return botao;
    }

    // ===== quebrar texto em linhas =====
    private String quebrarTexto(String texto, int larguraMax) {
        StringBuilder resultado = new StringBuilder();
        String[] palavras = texto.split(" ");
        StringBuilder linha = new StringBuilder();

        for (String palavra : palavras) {
            if (linha.length() + palavra.length() + 1 > larguraMax) {
                resultado.append(linha.toString().trim()).append("\n");
                linha = new StringBuilder();
            }
            linha.append(palavra).append(" ");
        }
        resultado.append(linha.toString().trim());
        return resultado.toString();
    }

    // ===== retorno da escolha =====
    public int getEscolha() { return escolha; }

    // ===== metodos estatios substituir JOption =====
    public static int mostrarPergunta(JFrame pai, String pergunta, String[] opcoes) {
        DialogoPersonalizado dialogo = new DialogoPersonalizado(pai, pergunta, opcoes);
        dialogo.setVisible(true);
        return dialogo.getEscolha();
    }

    public static void mostrarMensagem(JFrame pai, String mensagem) {
        DialogoPersonalizado dialogo = new DialogoPersonalizado(pai, mensagem);
        dialogo.setVisible(true);
    }
}
