import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    // ===== CONTROLE DE TECLAS =====
    static boolean wPressionado = false;
    static boolean sPressionado = false;
    static boolean aPressionado = false;
    static boolean dPressionado = false;

    // ===== PONTUAÇÃO =====
    static int pontos = 0;

    public static void main(String[] args) {

        // ===== OBJETOS DO JOGO =====
        Jogador jogador = new Jogador();
        Obstaculos obstaculos = new Obstaculos();

        // ===== NPCS =====
        NPC[] npcs = {
            new NPC(0.22, 0.33, "img/npc1.png",
                "Quando eu fico com muita raiva da minha namorada, às vezes eu a empurro para que ela pare de discutir.",
                new String[]{"Se foi só um empurrão, não é tão grave.", "Todo casal briga às vezes.", "Empurrar alguém é agressão e não resolve problemas."},
                2, 0),

            new NPC(0.35, 0.60, "img/npc2.png",
                "Meu namorado me diz que ninguém vai me querer sem ele, me rebaixa e diz que se não fosse ele eu viveria sozinha.",
                new String[]{"Talvez ele só falou sem pensar.", "Isso é manipulação, você precisa terminar esse relacionamento.", "Ele só está sendo sincero."},
                1, 2),

            new NPC(0.55, 0.30, "img/npc3.png",
                "Na festa um menino me fez beijar ele à força, mesmo eu tendo falado que não queria muitas vezes.",
                new String[]{"Ele só estava tentando te conquistar.", "Isso é assédio, você não deu consentimento. Vamos denunciar.", "Isso acontece em festas, é bem normal."},
                1, 0),

            new NPC(0.78, 0.30, "img/npc4.png",
                "Eu discuti com uma menina da minha sala e fiquei espalhando mentiras sobre ela. Foi engraçado.",
                new String[]{"Espalhar mentiras prejudica a dignidade das pessoas.", "Isso acontece na escola, é normal.", "Era só fofoca, relaxa."},
                0, 2),

            new NPC(0.24, 0.82, "img/npc5.png",
                "Meu namorado quando quer mostrar respeito me bate e segura meu braço muito forte, mas eu não acho isso respeito.",
                new String[]{"Isso não é respeito, é violência. Você precisa terminar e denunciar.", "Talvez tenha sido um exagero dos dois.", "Ele só tentou mostrar autoridade."},
                0, 2),
        };

        // ===== JANELA ÚNICA =====
        JFrame janela = new JFrame("O Peso das Escolhas");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== CARDLAYOUT =====
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        // ===== BOTÕES =====
        JButton botaoComecar = new JButton("COMEÇAR O JOGO");
        JButton botaoTela3   = new JButton("VOU TE AJUDAR COM CERTEZA!");
        JButton finalizar    = new JButton("VAMOS PARA O JOGO!");

        // ===== TELAS 1, 2 e 3 =====
        JPanel painel1 = Telas.criarTela1(botaoComecar);
        JPanel painel2 = Telas.criarTela2(botaoTela3);
        JPanel painel3 = Telas.criarTela3(finalizar);

        // ===== TELA 4 (JOGO) =====
        Image fundoJogo = new ImageIcon("img/fundoPrincipal.jpeg").getImage();

        JPanel painel4 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int W = getWidth();
                int H = getHeight();

                // ===== POSIÇÃO INICIAL DO JOGADOR =====
                jogador.inicializarPosicao(W, H);

                // ===== ATUALIZAR POSIÇÕES DOS NPCS =====
                for (NPC npc : npcs) npc.atualizarPosicao(W, H);

                // ===== ANIMAÇÃO =====
                jogador.atualizarAnimacao();

                // ===== SPRITE ATUAL =====
                boolean estaAndando = wPressionado || sPressionado || aPressionado || dPressionado;
                Image spriteAtual = jogador.getSpriteAtual(estaAndando);

                // ===== DESENHAR FUNDO =====
                g.drawImage(fundoJogo, 0, 0, W, H, this);

                // ===== DESENHAR NPCS =====
                for (NPC npc : npcs) npc.desenhar(g, W, H);

                // ===== DESENHAR JOGADOR =====
                g.drawImage(spriteAtual, jogador.getX(), jogador.getY(),
                        jogador.getLargura(W), jogador.getAltura(H), this);



                // ===== PONTUAÇÃO =====
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Pontos: " + pontos, 30, 50);
            }
        };
        painel4.setLayout(null);

        // ===== TIMER =====
        Timer timer = new Timer(16, e -> {

            int W = painel4.getWidth();
            int H = painel4.getHeight();

            // ===== MOVER =====
            jogador.mover(wPressionado, sPressionado, aPressionado, dPressionado);
            jogador.aplicarLimites(W, H);
            obstaculos.verificarColisao(jogador, W, H, wPressionado, sPressionado, aPressionado, dPressionado);

            // ===== COLISÃO COM NPCS =====
            for (NPC npc : npcs) {
                if (!npc.isPerguntaFeita() && jogador.getHitbox(W, H).intersects(npc.getHitbox(W, H))) {
                    npc.setPerguntaFeita(true);

                    wPressionado = false;
                    sPressionado = false;
                    aPressionado = false;
                    dPressionado = false;

                    int escolha = DialogoPersonalizado.mostrarPergunta(janela, npc.getPergunta(), npc.getOpcoes());

                    pontos += npc.calcularPontos(escolha);
                    DialogoPersonalizado.mostrarMensagem(janela, "Pontuação atual: " + pontos);

                    // ===== VERIFICAR FIM DO JOGO =====
                    boolean terminou = true;
                    for (NPC n : npcs) {
                        if (!n.isPerguntaFeita()) { terminou = false; break; }
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
                        DialogoPersonalizado.mostrarMensagem(janela, "FIM DE JOGO!\n\nPontuação final: " + pontos + "\n\n" + feedback);
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
        botaoTela3.addActionListener(e   -> cardLayout.show(container, "tela3"));
        finalizar.addActionListener(e    -> {
            cardLayout.show(container, "tela4");
            timer.start();
            janela.requestFocus();
        });

        // ===== TECLADO =====
        janela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP:    wPressionado = true;  break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  sPressionado = true;  break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  aPressionado = true;  break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: dPressionado = true;  break;
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
