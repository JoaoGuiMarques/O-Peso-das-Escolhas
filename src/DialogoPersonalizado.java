import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

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

    // ===== fonte pixelada (aumentada para deixar a letra e a janela maiores) =====
    private static final Font FONTE_TITULO  = new Font("Monospaced", Font.BOLD, 22);
    private static final Font FONTE_TEXTO   = new Font("Monospaced", Font.PLAIN, 20);
    private static final Font FONTE_BOTAO   = new Font("Monospaced", Font.BOLD, 17);

    // ===== quantidade de caracteres por linha do texto (define a largura da caixa) =====
    private static final int COLUNAS_TEXTO = 42;

    // ===== tamanho da imagem exibida no dialogo =====
    private static final int LARGURA_IMAGEM = 190;
    private static final int ALTURA_IMAGEM  = 240;

    // ===== resultado =====
    private int escolha = -1;

    // ===== construtor da pergunta com opcoes (sem imagem) =====
    public DialogoPersonalizado(JFrame pai, String pergunta, String[] opcoes) {
        this(pai, pergunta, opcoes, null);
    }

    // ===== construtor da pergunta com opcoes e imagem da situacao =====
    public DialogoPersonalizado(JFrame pai, String pergunta, String[] opcoes, Image imagemSituacao) {
        super(pai, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelPrincipal = criarPainelDeFundo();
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painelPrincipal.setOpaque(false);

        // ===== cabecalho (titulo + linha separadora que acompanha a fonte) =====
        JPanel cabecalho = criarCabecalho();

        // ===== texto da pergunta (dialogo), tamanho cresce com a fonte =====
        JTextArea textoPergunta = new JTextArea(pergunta);
        textoPergunta.setFont(FONTE_TEXTO);
        textoPergunta.setForeground(COR_TEXTO);
        textoPergunta.setBackground(COR_FUNDO);
        textoPergunta.setEditable(false);
        textoPergunta.setFocusable(false);
        textoPergunta.setLineWrap(true);
        textoPergunta.setWrapStyleWord(true);
        textoPergunta.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        textoPergunta.setPreferredSize(new Dimension(420, 180));


        // ===== painel dos botoes =====
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.Y_AXIS));
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // ===== cria todos os botoes primeiro, para depois igualar a largura pelo mais largo =====
        for (int i = 0; i < opcoes.length; i++) {

            final int indice = i;

            BotaoOpcao botao = new BotaoOpcao(opcoes[i]);

            botao.setAlignmentX(Component.CENTER_ALIGNMENT);

            botao.addActionListener(e -> {
                escolha = indice;
                dispose();
            });

            painelBotoes.add(botao);
            painelBotoes.add(Box.createVerticalStrut(10));
        }

        // ===== imagem da situacao (se houver e se carregou corretamente) =====
        JLabel labelImagem = criarLabelImagem(imagemSituacao);

        // ===== montagem =====
        JPanel conteudo = new JPanel(new BorderLayout(15,0));
        conteudo.setOpaque(false);

        if (labelImagem != null)
            conteudo.add(envolverImagemNoTopo(labelImagem), BorderLayout.WEST);

        conteudo.add(textoPergunta, BorderLayout.CENTER);

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(conteudo, BorderLayout.CENTER);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
        pack();
        setMinimumSize(new Dimension(780, 500));
        setLocationRelativeTo(pai);
        pack();
    }

    // ===== construtor para a mensagem simples (sem imagem) =====
    public DialogoPersonalizado(JFrame pai, String mensagem) {
        this(pai, mensagem, (Image) null);
    }

    // ===== construtor para a mensagem simples com imagem =====
    public DialogoPersonalizado(JFrame pai, String mensagem, Image imagem) {
        super(pai, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel painelPrincipal = criarPainelDeFundo();
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        painelPrincipal.setOpaque(false);

        // ===== cabecalho (titulo + linha separadora que acompanha a fonte) =====
        JPanel cabecalho = criarCabecalho();

        // ===== mensagem, tamanho cresce com a fonte =====
        String textoQuebrado = quebrarTexto(mensagem, COLUNAS_TEXTO);
        JTextArea textoMensagem = new JTextArea(textoQuebrado);
        textoMensagem.setFont(FONTE_TEXTO);
        textoMensagem.setForeground(COR_TEXTO);
        textoMensagem.setBackground(COR_FUNDO);
        textoMensagem.setEditable(false);
        textoMensagem.setFocusable(false);
        textoMensagem.setLineWrap(true);
        textoMensagem.setWrapStyleWord(true);
        textoMensagem.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        textoMensagem.setColumns(COLUNAS_TEXTO);
        textoMensagem.setRows(Math.max(2, textoQuebrado.split("\n").length + 1));

        // ===== botao "OK" =====
        JButton botaoOk = new BotaoOpcao("[ OK ]");
        botaoOk.addActionListener(e -> dispose());

        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotao.setOpaque(false);
        painelBotao.add(botaoOk);

        // ===== imagem (se houver) =====
        JLabel labelImagem = criarLabelImagem(imagem);

        JPanel painelTopo = new JPanel(new BorderLayout(15, 0));
        painelTopo.setOpaque(false);
        painelTopo.add(cabecalho, BorderLayout.NORTH);
        if (labelImagem != null) {
            painelTopo.add(envolverImagemNoTopo(labelImagem), BorderLayout.WEST);
        }
        painelTopo.add(textoMensagem, BorderLayout.CENTER);

        painelPrincipal.add(painelTopo, BorderLayout.CENTER);
        painelPrincipal.add(painelBotao, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
        pack();
        setMinimumSize(new Dimension(420, 220));
        setLocationRelativeTo(pai);
    }

    // ===== painel de fundo com bordas estilizadas (reaproveitado pelos dois construtores) =====
    private JPanel criarPainelDeFundo() {
        return new JPanel() {
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
            }
        };
    }

    // ===== cabecalho com titulo + linha separadora dinamica (acompanha a fonte, sem pixel fixo) =====
    private JPanel criarCabecalho() {
        JLabel titulo = new JLabel("▶ O PESO DAS ESCOLHAS");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_TITULO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder());

        // ===== linha separadora: componente real, nao pixel fixo, se posiciona sozinha =====
        JPanel linha = new JPanel();
        linha.setBackground(COR_BORDA);
        linha.setOpaque(true);
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);
        linha.setPreferredSize(new Dimension(10, 2));
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

        JPanel cabecalho = new JPanel();
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.setOpaque(false);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(8));
        cabecalho.add(linha);

        return cabecalho;
    }

    // ===== cria o label com a imagem da situacao/npc, escalada, ou null se nao houver/nao carregou =====
    private JLabel criarLabelImagem(Image imagem) {
        if (imagem == null) return null;

        // ===== garante que a imagem carregou completamente antes de medir/escalar =====
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(imagem, 0);
        try {
            tracker.waitForID(0, 3000);
        } catch (InterruptedException ignored) {
        }

        // ===== se a imagem nao carregou (arquivo nao encontrado, etc.) nao mostra nada =====
        if (imagem.getWidth(null) <= 0 || imagem.getHeight(null) <= 0 || tracker.isErrorID(0)) {
            return null;
        }

        Image imagemEscalada = imagem.getScaledInstance(LARGURA_IMAGEM, ALTURA_IMAGEM, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(imagemEscalada));
        label.setBorder(BorderFactory.createLineBorder(COR_BOTAO_BORDA, 2));
        label.setVerticalAlignment(SwingConstants.TOP);
        return label;
    }

    // ===== envolve a imagem num painel que a ancora no topo, para que sua borda nao seja =====
    // ===== esticada pelo BorderLayout (isso criava um retangulo vazio abaixo da imagem) =====
    private JPanel envolverImagemNoTopo(JLabel labelImagem) {
        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.setOpaque(false);
        envoltorio.add(labelImagem, BorderLayout.NORTH);
        return envoltorio;
    }

    // ===== dimensoes do botao de opcao =====
    private static final int LARGURA_BOTAO             = 420;
    private static final int PADDING_HORIZONTAL_BOTAO  = 24;
    private static final int PADDING_VERTICAL_BOTAO    = 16;
    private static final int ALTURA_MINIMA_BOTAO       = 45;

    // ===== botao com texto quebrado por largura em pixels e desenhado manualmente, =====
    // ===== crescendo de altura sozinho quando o texto da opcao e mais longo =====
    private class BotaoOpcao extends JButton {

        private List<String> linhas;
        private int alturaCalculada;

        BotaoOpcao(String texto) {

            super();
            setText(""); // o texto e desenhado manualmente em paintComponent, ja quebrado em linhas

            setFont(FONTE_BOTAO);
            setForeground(COR_TEXTO);

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            setHorizontalAlignment(SwingConstants.CENTER);
            setAlignmentX(Component.CENTER_ALIGNMENT);

            // ===== quebra o texto respeitando a largura real do botao (nao a quantidade de caracteres) =====
            FontMetrics fm = getFontMetrics(FONTE_BOTAO);
            int larguraDisponivel = LARGURA_BOTAO - PADDING_HORIZONTAL_BOTAO;
            linhas = quebrarTextoBotao(texto, larguraDisponivel, fm);

            int alturaTexto = linhas.size() * fm.getHeight();
            alturaCalculada = Math.max(ALTURA_MINIMA_BOTAO, alturaTexto + PADDING_VERTICAL_BOTAO);
        }

        // ===== quebra o texto em linhas usando largura em pixels (fonte monoespacada ou nao) =====
        private List<String> quebrarTextoBotao(String texto, int larguraMaxPx, FontMetrics fm) {
            List<String> resultado = new ArrayList<>();
            String[] palavras = texto.split(" ");
            StringBuilder linhaAtual = new StringBuilder();

            for (String palavra : palavras) {
                String tentativa = linhaAtual.length() == 0 ? palavra : linhaAtual + " " + palavra;
                if (fm.stringWidth(tentativa) > larguraMaxPx && linhaAtual.length() > 0) {
                    resultado.add(linhaAtual.toString());
                    linhaAtual = new StringBuilder(palavra);
                } else {
                    linhaAtual = new StringBuilder(tentativa);
                }
            }
            if (linhaAtual.length() > 0) {
                resultado.add(linhaAtual.toString());
            }
            return resultado;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(LARGURA_BOTAO, alturaCalculada);
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isRollover())
                g2.setColor(COR_BOTAO_HOVER);
            else
                g2.setColor(COR_BOTAO_FUNDO);

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

            g2.setColor(COR_BOTAO_BORDA);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);

            // ===== desenha o texto ja quebrado, centralizado na horizontal e na vertical =====
            g2.setFont(FONTE_BOTAO);
            g2.setColor(COR_TEXTO);
            FontMetrics fm = g2.getFontMetrics();
            int alturaLinha = fm.getHeight();
            int alturaTotalTexto = alturaLinha * linhas.size();
            int y = (getHeight() - alturaTotalTexto) / 2 + fm.getAscent();

            for (String linha : linhas) {
                int larguraLinha = fm.stringWidth(linha);
                int x = (getWidth() - larguraLinha) / 2;
                g2.drawString(linha, x, y);
                y += alturaLinha;
            }
        }
    }

    // ===== quebrar texto em linhas, respeitando paragrafos ja existentes (\n) =====
    private String quebrarTexto(String texto, int larguraMax) {
        StringBuilder resultadoFinal = new StringBuilder();
        String[] paragrafos = texto.split("\n", -1);

        for (int p = 0; p < paragrafos.length; p++) {
            String paragrafo = paragrafos[p];

            if (!paragrafo.isEmpty()) {
                String[] palavras = paragrafo.split(" ");
                StringBuilder linha = new StringBuilder();

                for (String palavra : palavras) {
                    if (linha.length() > 0 && linha.length() + palavra.length() + 1 > larguraMax) {
                        resultadoFinal.append(linha.toString().trim()).append("\n");
                        linha = new StringBuilder();
                    }
                    linha.append(palavra).append(" ");
                }
                resultadoFinal.append(linha.toString().trim());
            }

            if (p < paragrafos.length - 1) {
                resultadoFinal.append("\n");
            }
        }

        return resultadoFinal.toString();
    }

    // ===== retorno da escolha =====
    public int getEscolha() { return escolha; }

    // ===== metodos estatios substituir JOption =====
    public static int mostrarPergunta(JFrame pai, String pergunta, String[] opcoes) {
        return mostrarPergunta(pai, pergunta, opcoes, null);
    }

    public static int mostrarPergunta(JFrame pai, String pergunta, String[] opcoes, Image imagemSituacao) {
        DialogoPersonalizado dialogo = new DialogoPersonalizado(pai, pergunta, opcoes, imagemSituacao);
        dialogo.setVisible(true);
        return dialogo.getEscolha();
    }

    public static void mostrarMensagem(JFrame pai, String mensagem) {
        mostrarMensagem(pai, mensagem, null);
    }

    public static void mostrarMensagem(JFrame pai, String mensagem, Image imagem) {
        DialogoPersonalizado dialogo = new DialogoPersonalizado(pai, mensagem, imagem);
        dialogo.setVisible(true);
    }
}