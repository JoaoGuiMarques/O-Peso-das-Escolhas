import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TelaFinal {

    // ===== cores (mesmo padrao visual do resto do jogo) =====
    private static final Color COR_FUNDO         = new Color(20, 10, 35);
    private static final Color COR_BORDA         = new Color(140, 80, 200);
    private static final Color COR_BORDA_INTERNA = new Color(80, 40, 120);
    private static final Color COR_TEXTO         = new Color(230, 210, 255);
    private static final Color COR_TITULO        = new Color(200, 150, 255);
    private static final Color COR_ACERTO        = new Color(140, 230, 160);
    private static final Color COR_ERRO          = new Color(230, 110, 110);
    private static final Color COR_CAIXA_INFO    = new Color(45, 20, 70);

    // ===== fontes =====
    private static final Font FONTE_TITULO     = new Font("Monospaced", Font.BOLD, 36);
    private static final Font FONTE_PONTOS     = new Font("Monospaced", Font.BOLD, 28);
    private static final Font FONTE_TEXTO      = new Font("Monospaced", Font.PLAIN, 18);
    private static final Font FONTE_SUBTITULO  = new Font("Monospaced", Font.BOLD, 18);
    private static final Font FONTE_CATEGORIA  = new Font("Monospaced", Font.BOLD, 17);

    // ===== monta a tela final completa =====
    public static JPanel criarTelaFinal(int pontos, Map<String, int[]> resultadosPorCategoria,
                                        Runnable onJogarNovamente, Runnable onSair) {

        // ===== fundo da tela inteira =====
        JPanel painelFundo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(COR_FUNDO);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // ===== cartao central com borda estilizada =====
        JPanel cartao = new JPanel() {
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
            }
        };
        cartao.setOpaque(false);
        cartao.setLayout(new BoxLayout(cartao, BoxLayout.Y_AXIS));
        cartao.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        cartao.setPreferredSize(new Dimension(640, 830));

        // ===== titulo =====
        JLabel titulo = new JLabel("▶ FIM DE JOGO!");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_TITULO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        // ===== pontuacao final =====
        JLabel labelPontos = new JLabel("Pontuação final: " + pontos);
        labelPontos.setFont(FONTE_PONTOS);
        labelPontos.setForeground(COR_TEXTO);
        labelPontos.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPontos.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // ===== feedback educativo, de acordo com a faixa de pontuacao =====
        JTextArea textoFeedback = new JTextArea(gerarFeedback(pontos));
        textoFeedback.setFont(FONTE_TEXTO);
        textoFeedback.setForeground(COR_TEXTO);
        textoFeedback.setBackground(COR_FUNDO);
        textoFeedback.setEditable(false);
        textoFeedback.setFocusable(false);
        textoFeedback.setLineWrap(true);
        textoFeedback.setWrapStyleWord(true);
        textoFeedback.setAlignmentX(Component.CENTER_ALIGNMENT);
        textoFeedback.setMaximumSize(new Dimension(560, 140));
        textoFeedback.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        // ===== titulo do resumo por categoria =====
        JLabel labelResumo = new JLabel("RESUMO POR TIPO DE VIOLÊNCIA");
        labelResumo.setFont(FONTE_SUBTITULO);
        labelResumo.setForeground(COR_TITULO);
        labelResumo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelResumo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // ===== linhas do resumo por categoria =====
        JPanel painelResumo = new JPanel();
        painelResumo.setLayout(new BoxLayout(painelResumo, BoxLayout.Y_AXIS));
        painelResumo.setOpaque(false);
        painelResumo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelResumo.setMaximumSize(new Dimension(560, 200));
        painelResumo.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        for (Map.Entry<String, int[]> entrada : resultadosPorCategoria.entrySet()) {
            painelResumo.add(criarLinhaCategoria(entrada.getKey(), entrada.getValue()));
            painelResumo.add(Box.createVerticalStrut(5));
        }

        // ===== caixa com contatos de ajuda =====
        JPanel caixaContato = new JPanel();
        caixaContato.setLayout(new BoxLayout(caixaContato, BoxLayout.Y_AXIS));
        caixaContato.setBackground(COR_CAIXA_INFO);
        caixaContato.setAlignmentX(Component.CENTER_ALIGNMENT);
        caixaContato.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 2),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        caixaContato.setMaximumSize(new Dimension(560, 150));

        JLabel tituloContato = new JLabel("PRECISA DE AJUDA OU CONHECE ALGUÉM QUE PRECISA?");
        tituloContato.setFont(FONTE_SUBTITULO);
        tituloContato.setForeground(COR_TITULO);
        tituloContato.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel disque100 = new JLabel("Disque 100 — Direitos Humanos (denúncias em geral)");
        disque100.setFont(FONTE_TEXTO);
        disque100.setForeground(COR_TEXTO);
        disque100.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel disque180 = new JLabel("Disque 180 — Central de Atendimento à Mulher");
        disque180.setFont(FONTE_TEXTO);
        disque180.setForeground(COR_TEXTO);
        disque180.setAlignmentX(Component.CENTER_ALIGNMENT);

        caixaContato.add(tituloContato);
        caixaContato.add(Box.createVerticalStrut(8));
        caixaContato.add(disque100);
        caixaContato.add(disque180);

        // ===== botoes finais, reaproveitando o estilo ja usado nas outras telas =====
        JButton botaoJogarNovamente = Telas.criarBotaoEstilizado("JOGAR NOVAMENTE");
        botaoJogarNovamente.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoJogarNovamente.setMaximumSize(new Dimension(280, 42));
        botaoJogarNovamente.setPreferredSize(new Dimension(280, 42));
        botaoJogarNovamente.addActionListener(e -> onJogarNovamente.run());

        JButton botaoSair = Telas.criarBotaoEstilizado("SAIR DO JOGO");
        botaoSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSair.setMaximumSize(new Dimension(280, 42));
        botaoSair.setPreferredSize(new Dimension(280, 42));
        botaoSair.addActionListener(e -> onSair.run());

        // ===== montagem do cartao =====
        cartao.add(titulo);
        cartao.add(labelPontos);
        cartao.add(textoFeedback);
        cartao.add(labelResumo);
        cartao.add(painelResumo);
        cartao.add(caixaContato);
        cartao.add(Box.createVerticalStrut(20));
        cartao.add(botaoJogarNovamente);
        cartao.add(Box.createVerticalStrut(8));
        cartao.add(botaoSair);

        painelFundo.add(cartao);

        return painelFundo;
    }

    // ===== monta uma linha "Categoria .......... X acerto(s) / Y erro(s)" =====
    private static JPanel criarLinhaCategoria(String categoria, int[] contador) {
        int acertos = contador[0];
        int erros   = contador[1];

        JPanel linha = new JPanel(new BorderLayout());
        linha.setOpaque(false);
        linha.setMaximumSize(new Dimension(560, 30));
        linha.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nome = new JLabel(categoria);
        nome.setFont(FONTE_CATEGORIA);
        nome.setForeground(COR_TEXTO);

        JLabel resultado = new JLabel(acertos + " acerto(s)  /  " + erros + " erro(s)");
        resultado.setFont(FONTE_TEXTO);
        resultado.setHorizontalAlignment(SwingConstants.RIGHT);
        if (erros == 0 && acertos > 0) {
            resultado.setForeground(COR_ACERTO);
        } else if (acertos == 0 && erros > 0) {
            resultado.setForeground(COR_ERRO);
        } else {
            resultado.setForeground(COR_TEXTO);
        }

        linha.add(nome, BorderLayout.WEST);
        linha.add(resultado, BorderLayout.EAST);

        return linha;
    }

    // ===== feedback educativo final, de acordo com a faixa de pontuacao =====
    private static String gerarFeedback(int pontos) {
        if (pontos < 80) {
            return "Você ainda confunde algumas atitudes de controle, humilhação ou agressão com coisas " +
                    "\"normais\" de um relacionamento ou amizade. Vale a pena revisar as dicas do jogo e " +
                    "conversar sobre o assunto com um adulto de confiança — reconhecer esses sinais é o " +
                    "primeiro passo pra se proteger e proteger quem você conhece.";
        } else if (pontos <= 120) {
            return "Você já reconhece boa parte das situações de violência, mas ainda hesitou em alguns " +
                    "casos. Continue prestando atenção aos sinais de controle, agressão e desrespeito — " +
                    "quanto mais cedo isso for reconhecido, mais fácil é buscar ajuda.";
        } else {
            return "Parabéns! Você identificou muito bem as diferentes formas de violência e soube " +
                    "responder com empatia e informação. Compartilhar esse conhecimento pode ajudar " +
                    "outras pessoas a reconhecerem situações parecidas e buscarem apoio.";
        }
    }
}