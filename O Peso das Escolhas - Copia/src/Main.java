import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    // ===== DIREÇÃO DO PERSONAGEM =====
    static String direcao = "frente"; // frente, costas, direita, esquerda
    static boolean andando = false;

    // ===== ANIMAÇÃO =====
    static int frameAnimacao = 0;
    static int contadorAnimacao = 0;
    static final int VELOCIDADE_ANIMACAO = 10; // frames entre troca de sprite

    // ===== PERSONAGEM PRINCIPAL =====
    static int playerX = 0; // será calculado dinamicamente
    static int playerY = 0;
    static int velocidade = 5;

    // ===== POSIÇÕES DOS NPCS (proporcionais) =====
    static double[] npcXp = {0.22, 0.35, 0.55, 0.78, 0.24};
    static double[] npcYp = {0.33, 0.60, 0.30, 0.30, 0.82};
    static int[] npcX = {0, 0, 0, 0, 0};
    static int[] npcY = {0, 0, 0, 0, 0};

    // ===== OBSTÁCULOS =====
    static double[][] obstaculosP = {
            {0.0,  0.0,  0.26, 0.25}, // Prédios/lojas topo esq
            {0.26, 0.0,  0.74, 0.25}, // Prédios/lojas topo centro
            {0.74, 0.0,  1.0,  0.25}, // Prédios/lojas topo dir
            {0.88, 0.0, 1.0,  0.27}, // Árvore topo dir
            {0.0,  0.42, 0.18, 0.43}, // Bancos + lanterna esq
            {0.94, 0.35, 1.0,  0.37}, // Hidrante + lanterna dir
            {0.0,  0.55, 0.33, 0.79}, // Casa
            {0.64, 0.76, 0.83, 0.82}, // Lanterna baixo
            {0.91, 0.80, 0.92, 0.82}, // Lixo baixo
            {0.68, 0.63, 0.94,  0.75}, // Área verde + árvore
            {0.80, 0.59, 0.90,  0.65}, // Topo da árvore
            {0.81, 0.56, 0.88,  0.65}, // Topo da árvore
    };

    // ===== CONTROLE DAS PERGUNTAS =====
    static boolean[] perguntaFeita = {false, false, false, false, false};

    // ===== PONTUAÇÃO =====
    static int pontos = 0;

    // ===== CONTROLE DE TECLAS =====
    static boolean wPressionado = false;
    static boolean sPressionado = false;
    static boolean aPressionado = false;
    static boolean dPressionado = false;

    public static void main(String[] args) {

        // ===== JANELA ÚNICA =====
        JFrame janela = new JFrame("O Peso das Escolhas");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== CARDLAYOUT PARA TROCAR TELAS =====
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        // ===== TELA 1 =====
        JPanel painel1 = new JPanel() {
            Image fundo = new ImageIcon("img/background.jpeg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel1.setLayout(null);

        JButton botaoComecar = new JButton("COMEÇAR O JOGO");
        painel1.add(botaoComecar);

        painel1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = painel1.getWidth();
                int h = painel1.getHeight();
                botaoComecar.setBounds(w / 2 - 110, (int)(h * 0.80), 220, 30);
            }
        });

        // ===== TELA 2 =====
        JPanel painel2 = new JPanel() {
            Image fundo = new ImageIcon("img/apresentacao.jpeg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel2.setLayout(null);

        JButton botaoTela3 = new JButton("VOU TE AJUDAR COM CERTEZA!");
        painel2.add(botaoTela3);

        painel2.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = painel2.getWidth();
                int h = painel2.getHeight();
                botaoTela3.setBounds(w / 2 - 130, (int)(h * 0.88), 260, 30);
            }
        });

        // ===== TELA 3 =====
        JPanel painel3 = new JPanel() {
            Image fundo = new ImageIcon("img/dicas.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel3.setLayout(null);

        JButton finalizar = new JButton("VAMOS PARA O JOGO!");
        painel3.add(finalizar);

        painel3.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = painel3.getWidth();
                int h = painel3.getHeight();
                finalizar.setBounds(w / 5 - 140, (int)(h * 0.85), 220, 30);
            }
        });

        // ===== TELA 4 (JOGO) =====
        JPanel painel4 = new JPanel() {

            // ===== FUNDO =====
            Image fundo = new ImageIcon("img/fundoPrincipal.jpeg").getImage();

            // ===== SPRITES ESTÁTICOS =====
            Image playerFrente   = new ImageIcon("img/player_frente.png").getImage();
            Image playerCostas   = new ImageIcon("img/player_costas.png").getImage();
            Image playerDireita  = new ImageIcon("img/player_direita.png").getImage();
            Image playerEsquerda = new ImageIcon("img/player_esquerda.png").getImage();

            // ===== SPRITES ANDANDO =====
            Image playerAndandoFrente   = new ImageIcon("img/player_mov_frente.png").getImage();
            Image playerAndandoCostas   = new ImageIcon("img/player_mov_costas.png").getImage();
            Image playerAndandoDireita  = new ImageIcon("img/player_mov_direita.png").getImage();
            Image playerAndandoEsquerda = new ImageIcon("img/player_mov_esquerda.png").getImage();

            // ===== NPCS =====
            Image npc1 = new ImageIcon("img/npc1.png").getImage();
            Image npc2 = new ImageIcon("img/npc2.png").getImage();
            Image npc3 = new ImageIcon("img/npc3.png").getImage();
            Image npc4 = new ImageIcon("img/npc4.png").getImage();
            Image npc5 = new ImageIcon("img/npc5.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int W = getWidth();
                int H = getHeight();

                // ===== POSIÇÃO INICIAL DO JOGADOR (só na primeira vez) =====
                if (playerX == 0 && playerY == 0) {
                    playerX = (int)(W * 0.50);
                    playerY = (int)(H * 0.65);
                }

                // ===== POSIÇÕES DOS NPCS =====
                for (int i = 0; i < 5; i++) {
                    npcX[i] = (int)(W * npcXp[i]);
                    npcY[i] = (int)(H * npcYp[i]);
                }

                // ===== TAMANHO PROPORCIONAL =====
                int alturaPersonagem  = (int)(H * 0.15);
                int larguraPersonagem = (int)(W * 0.04);
                int alturaNpc  = (int)(H * 0.15);
                int larguraNpc = (int)(W * 0.04);

                // ===== ANIMAÇÃO =====
                contadorAnimacao++;
                if (contadorAnimacao >= VELOCIDADE_ANIMACAO) {
                    frameAnimacao = (frameAnimacao + 1) % 2;
                    contadorAnimacao = 0;
                }

                // ===== SPRITE ATUAL DO JOGADOR =====
                Image spriteAtual;
                boolean estaAndando = wPressionado || sPressionado || aPressionado || dPressionado;

                if (estaAndando && frameAnimacao == 1) {
                    // sprite andando
                    switch (direcao) {
                        case "costas":   spriteAtual = playerAndandoCostas;   break;
                        case "direita":  spriteAtual = playerAndandoDireita;  break;
                        case "esquerda": spriteAtual = playerAndandoEsquerda; break;
                        default:         spriteAtual = playerAndandoFrente;   break;
                    }
                } else {
                    // sprite estático
                    switch (direcao) {
                        case "costas":   spriteAtual = playerCostas;   break;
                        case "direita":  spriteAtual = playerDireita;  break;
                        case "esquerda": spriteAtual = playerEsquerda; break;
                        default:         spriteAtual = playerFrente;   break;
                    }
                }

                // ===== DESENHAR FUNDO =====
                g.drawImage(fundo, 0, 0, W, H, this);

                // ===== DESENHAR NPCS =====
                if (!perguntaFeita[0]) g.drawImage(npc1, npcX[0], npcY[0], larguraNpc, alturaNpc, this);
                if (!perguntaFeita[1]) g.drawImage(npc2, npcX[1], npcY[1], larguraNpc, alturaNpc, this);
                if (!perguntaFeita[2]) g.drawImage(npc3, npcX[2], npcY[2], larguraNpc, alturaNpc, this);
                if (!perguntaFeita[3]) g.drawImage(npc4, npcX[3], npcY[3], larguraNpc, alturaNpc, this);
                if (!perguntaFeita[4]) g.drawImage(npc5, npcX[4], npcY[4], larguraNpc, alturaNpc, this);

                // ===== DESENHAR JOGADOR =====
                g.drawImage(spriteAtual, playerX, playerY, larguraPersonagem, alturaPersonagem, this);

                // ===== DEBUG: MOSTRAR OBSTÁCULOS =====
                g.setColor(new Color(255, 0, 0, 100));
                for (double[] obs : obstaculosP) {
                    int ox = (int)(W * obs[0]);
                    int oy = (int)(H * obs[1]);
                    int ow = (int)(W * obs[2]) - ox;
                    int oh = (int)(H * obs[3]) - oy;
                    g.fillRect(ox, oy, ow, oh);
                    g.setColor(Color.RED);
                    g.drawRect(ox, oy, ow, oh);
                    g.setColor(new Color(255, 0, 0, 100));
                }

                // ===== DEBUG: HITBOX DO JOGADOR =====
                g.setColor(Color.YELLOW);
                g.drawRect(playerX, playerY, larguraPersonagem, alturaPersonagem);

                // ===== DEBUG: HITBOX DOS NPCS =====
                g.setColor(Color.CYAN);
                for (int i = 0; i < 5; i++) {
                    if (!perguntaFeita[i]) {
                        g.drawRect(npcX[i], npcY[i], (int)(W * 0.03), (int)(H * 0.05));
                    }
                }

                // ===== PONTUAÇÃO =====
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Pontos: " + pontos, 30, 50);

                // ===== PONTUAÇÃO =====
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Pontos: " + pontos, 30, 50);
            }
        };
        painel4.setLayout(null);

        // ===== TIMER DE ANIMAÇÃO =====
        Timer timer = new Timer(16, e -> { // ~60fps

            int W = painel4.getWidth();
            int H = painel4.getHeight();

            // ===== MOVER O JOGADOR =====
            if (wPressionado) { playerY -= velocidade; direcao = "costas"; }
            if (sPressionado) { playerY += velocidade; direcao = "frente"; }
            if (aPressionado) { playerX -= velocidade; direcao = "esquerda"; }
            if (dPressionado) { playerX += velocidade; direcao = "direita"; }

            // ===== LIMITES DA TELA =====
            int alturaPersonagem  = (int)(H * 0.15);
            int larguraPersonagem = (int)(W * 0.04);
            if (playerX < 0) playerX = 0;
            if (playerY < 0) playerY = 0;
            if (playerX + larguraPersonagem > W) playerX = W - larguraPersonagem;
            if (playerY + alturaPersonagem  > H) playerY = H - alturaPersonagem;

            // ===== COLISÃO COM OBSTÁCULOS =====
            Rectangle playerRect = new Rectangle(playerX, playerY, larguraPersonagem, alturaPersonagem);
            for (double[] obs : obstaculosP) {
                int ox = (int)(W * obs[0]);
                int oy = (int)(H * obs[1]);
                int ow = (int)(W * obs[2]) - ox;
                int oh = (int)(H * obs[3]) - oy;
                Rectangle obsRect = new Rectangle(ox, oy, ow, oh);

                if (playerRect.intersects(obsRect)) {
                    if (wPressionado) playerY += velocidade;
                    if (sPressionado) playerY -= velocidade;
                    if (aPressionado) playerX += velocidade;
                    if (dPressionado) playerX -= velocidade;
                    break;
                }
            }

            // ===== VERIFICAR COLISÕES =====
            for (int i = 0; i < npcX.length; i++) {
                Rectangle player   = new Rectangle(playerX, playerY, larguraPersonagem, alturaPersonagem);
                Rectangle npcAtual = new Rectangle(npcX[i], npcY[i], (int)(W * 0.03), (int)(H * 0.05));

                if (player.intersects(npcAtual) && !perguntaFeita[i]) {
                    perguntaFeita[i] = true;

                    // ===== RESETAR TECLAS PARA O BONECO NÃO ANDAR SOZINHO =====
                    wPressionado = false;
                    sPressionado = false;
                    aPressionado = false;
                    dPressionado = false;

                    String pergunta = "";
                    String[] opcoes = {};

                    if (i == 0) {
                        pergunta = "Quando eu fico com muita raiva da minha namorada, às vezes eu a empurro para que ela pare de discutir.";
                        opcoes = new String[]{"Se foi só um empurrão, não é tão grave.", "Todo casal briga às vezes.", "Empurrar alguém é agressão e não resolve problemas."};
                    }
                    if (i == 1) {
                        pergunta = "Meu namorado me diz que ninguém vai me querer sem ele, me rebaixa e diz que se não fosse ele eu viveria sozinha.";
                        opcoes = new String[]{"Talvez ele só falou sem pensar.", "Isso é manipulação, você precisa terminar esse relacionamento.", "Ele só está sendo sincero."};
                    }
                    if (i == 2) {
                        pergunta = "Na festa um menino me fez beijar ele à força, mesmo eu tendo falado que não queria muitas vezes.";
                        opcoes = new String[]{"Ele só estava tentando te conquistar.", "Isso é assédio, você não deu consentimento. Vamos denunciar.", "Isso acontece em festas, é bem normal."};
                    }
                    if (i == 3) {
                        pergunta = "Eu discuti com uma menina da minha sala e fiquei espalhando mentiras sobre ela. Foi engraçado.";
                        opcoes = new String[]{"Espalhar mentiras prejudica a dignidade das pessoas.", "Isso acontece na escola, é normal.", "Era só fofoca, relaxa."};
                    }
                    if (i == 4) {
                        pergunta = "Meu namorado quando quer mostrar respeito me bate e segura meu braço muito forte, mas eu não acho isso respeito.";
                        opcoes = new String[]{"Isso não é respeito, é violência. Você precisa terminar e denunciar.", "Talvez tenha sido um exagero dos dois.", "Ele só tentou mostrar autoridade."};
                    }

                    int escolha = JOptionPane.showOptionDialog(
                            painel4, pergunta, "Escolha",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null, opcoes, opcoes[0]
                    );

                    if (i == 0) pontos += (escolha == 2) ? 10 : (escolha == 0) ? -10 : 0;
                    if (i == 1) pontos += (escolha == 1) ? 10 : (escolha == 2) ? -10 : 0;
                    if (i == 2) pontos += (escolha == 1) ? 10 : (escolha == 0) ? -10 : 0;
                    if (i == 3) pontos += (escolha == 0) ? 10 : (escolha == 2) ? -10 : 0;
                    if (i == 4) pontos += (escolha == 0) ? 10 : (escolha == 1) ? 0  : -10;

                    JOptionPane.showMessageDialog(painel4, "Pontuação atual: " + pontos);

                    // ===== VERIFICAR FIM DO JOGO =====
                    boolean terminou = true;
                    for (boolean respondeu : perguntaFeita) {
                        if (!respondeu) { terminou = false; break; }
                    }

                    if (terminou) {
                        String feedback;
                        if (pontos < 30) {
                            feedback = "Você precisa estudar mais sobre o assunto.";
                        } else if (pontos <= 40) {
                            feedback = "Você está no caminho certo!";
                        } else {
                            feedback = "Parabéns, você entende muito do assunto!";
                        }
                        JOptionPane.showMessageDialog(painel4,
                                "FIM DE JOGO!\n\nPontuação final: " + pontos + "\n\n" + feedback
                        );
                        janela.dispose();
                    }
                }
            }

            painel4.repaint();
        });

        // ===== ADICIONAR TELAS AO CONTAINER =====
        container.add(painel1, "tela1");
        container.add(painel2, "tela2");
        container.add(painel3, "tela3");
        container.add(painel4, "tela4");

        // ===== AÇÕES DOS BOTÕES =====
        botaoComecar.addActionListener(e -> cardLayout.show(container, "tela2"));
        botaoTela3.addActionListener(e  -> cardLayout.show(container, "tela3"));
        finalizar.addActionListener(e   -> {
            cardLayout.show(container, "tela4");
            timer.start();
            janela.requestFocus();
        });

        // ===== TECLADO =====
        janela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP:    wPressionado = true; break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  sPressionado = true; break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  aPressionado = true; break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: dPressionado = true; break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP:    wPressionado = false; break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  sPressionado = false; break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  aPressionado = false; break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: dPressionado = false; break;
                }
            }
        });

        // ===== CONFIGURAR JANELA =====
        janela.add(container);
        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        janela.setVisible(true);
    }
}