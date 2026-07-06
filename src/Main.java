import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        // ===== janela unica =====
        JFrame janela = new JFrame("O Peso das Escolhas");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== cardlayout =====
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        // ===== botoes =====
        JButton botaoComecar = new JButton("COMEÇAR O JOGO");
        JButton botaoTela3   = new JButton("VOU TE AJUDAR COM CERTEZA!");
        JButton finalizar    = new JButton("VAMOS PARA O JOGO!");

        // ===== telas 1, 2 e 3 (metodos) =====
        JPanel painel1 = Telas.criarTela1(botaoComecar);
        JPanel painel2 = Telas.criarTela2(botaoTela3);
        JPanel painel3 = Telas.criarTela3(finalizar);

        // ===== fundos dos mapas 1, 2 e 3 =====
        Image[] fundos = {
                new ImageIcon("img/fundoPrincipal.jpeg").getImage(), // mapa 1
                new ImageIcon("img/fundoMapa2.jpeg").getImage(),     // mapa 2
                new ImageIcon("img/fundoMapa3.jpeg").getImage()      // mapa 3
        };

        // ===== painel do jogo =====
        JPanel[] painelRef = new JPanel[1];
        Jogo[] jogoRef     = new Jogo[1];

        JPanel painelJogo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Jogo jogo = jogoRef[0];
                if (jogo == null) return;

                int W = getWidth();
                int H = getHeight();

                Jogador jogador = jogo.getJogador();
                NPC[] npcs      = jogo.getNpcs();

                jogador.inicializarPosicao(W, H);
                for (NPC npc : npcs) npc.atualizarPosicao(W, H);
                jogador.atualizarAnimacao();

                Image spriteAtual = jogador.getSpriteAtual(jogo.isAndando());

                // ===== fundo do mapa atual =====
                int idxFundo = jogo.getMapaAtual() - 1;
                g.drawImage(fundos[idxFundo], 0, 0, W, H, this);

                // ===== desenhar npcs (mostrar) =====
                for (NPC npc : npcs) npc.desenhar(g, W, H);

                // ===== desenhar jogador principal =====
                g.drawImage(spriteAtual, jogador.getX(), jogador.getY(),
                        jogador.getLargura(W), jogador.getAltura(H), this);
                /*

                // COORDENADAS DO MOUSE
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, this);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 18));

                g.drawString("Mouse: " + p.x + ", " + p.y, 30, 120);
                g.drawString(
                        String.format("(%.3f, %.3f)",
                                p.x / (double) getWidth(),
                                p.y / (double) getHeight()),
                        30, 145
                );

                 */

                /*
                // ===== mostrar obstáculos (impedir passagem) =====
                jogo.getObstaculos().desenharDebug(g, W, H, jogo.getMapaAtual());


                // ===== mostrar hitbox dos jogadores =====
                g.setColor(Color.YELLOW);
                g.drawRect(jogador.getX(), jogador.getY(), jogador.getLargura(W), jogador.getAltura(H));

                // ===== mostrar hitbox npcs =====
                g.setColor(Color.CYAN);
                for (NPC npc : npcs) {
                    if (!npc.isPerguntaFeita()) {
                        g.drawRect(npc.getX(), npc.getY(), npc.getHitboxLargura(W), npc.getHitboxAltura(H));
                    }
                }

                 */



                // ===== pontuação canto superior esquerdo =====
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Pontos: " + jogo.getPontos(), 30, 50);

                // ===== fase canto superior esquerdo =====
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Fase " + jogo.getMapaAtual() + " de 3", 30, 85);
            }
        };
        painelJogo.setLayout(null);
        painelRef[0] = painelJogo;

        // ===== criando o jogo =====
        Jogo jogo = new Jogo(janela, painelJogo, () -> painelJogo.repaint());
        jogoRef[0] = jogo;

        // ===== adicionar telas ao container =====
        container.add(painel1,    "tela1");
        container.add(painel2,    "tela2");
        container.add(painel3,    "tela3");
        container.add(painelJogo, "tela4");

        // ===== ações dos botoes =====
        botaoComecar.addActionListener(e -> cardLayout.show(container, "tela2"));
        botaoTela3.addActionListener(e   -> cardLayout.show(container, "tela3"));
        finalizar.addActionListener(e    -> {
            cardLayout.show(container, "tela4");
            jogo.iniciar();
            janela.requestFocus();
        });

        // ===== uso do teclado =====
        janela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP:    jogo.setW(true); break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  jogo.setS(true); break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  jogo.setA(true); break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: jogo.setD(true); break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP:    jogo.setW(false); break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  jogo.setS(false); break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  jogo.setA(false); break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: jogo.setD(false); break;
                }
            }
        });

        // ===== configurar janela =====
        janela.add(container);
        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        janela.setVisible(true);
    }
}
