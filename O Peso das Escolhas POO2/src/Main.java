import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public static void main(String[] args) {

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

        // ===== FUNDOS DOS 3 MAPAS =====
        Image[] fundos = {
            new ImageIcon("img/fundoPrincipal.jpeg").getImage(), // mapa 1
            new ImageIcon("img/fundoMapa2.jpeg").getImage(),     // mapa 2
            new ImageIcon("img/fundoMapa3.jpeg").getImage()      // mapa 3
        };

        // ===== PAINEL DO JOGO =====
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

                // ===== FUNDO DO MAPA ATUAL =====
                int idxFundo = jogo.getMapaAtual() - 1;
                g.drawImage(fundos[idxFundo], 0, 0, W, H, this);

                // ===== DESENHAR NPCS =====
                for (NPC npc : npcs) npc.desenhar(g, W, H);

                // ===== DESENHAR JOGADOR =====
                g.drawImage(spriteAtual, jogador.getX(), jogador.getY(),
                        jogador.getLargura(W), jogador.getAltura(H), this);

                /*

                // ===== DEBUG: MOSTRAR OBSTÁCULOS =====
                jogo.getObstaculos().desenharDebug(g, W, H, jogo.getMapaAtual());

                // ===== DEBUG: HITBOX DO JOGADOR =====
                g.setColor(Color.YELLOW);
                g.drawRect(jogador.getX(), jogador.getY(), jogador.getLargura(W), jogador.getAltura(H));

                // ===== DEBUG: HITBOX DOS NPCS =====
                g.setColor(Color.CYAN);
                for (NPC npc : npcs) {
                    if (!npc.isPerguntaFeita()) {
                        g.drawRect(npc.getX(), npc.getY(), npc.getHitboxLargura(W), npc.getHitboxAltura(H));
                    }
                }

                 */

                // ===== PONTUAÇÃO =====
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Pontos: " + jogo.getPontos(), 30, 50);

                // ===== INDICADOR DE MAPA =====
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Fase " + jogo.getMapaAtual() + " de 3", 30, 85);
            }
        };
        painelJogo.setLayout(null);
        painelRef[0] = painelJogo;

        // ===== CRIAR JOGO =====
        Jogo jogo = new Jogo(janela, painelJogo, () -> painelJogo.repaint());
        jogoRef[0] = jogo;

        // ===== ADICIONAR TELAS AO CONTAINER =====
        container.add(painel1,    "tela1");
        container.add(painel2,    "tela2");
        container.add(painel3,    "tela3");
        container.add(painelJogo, "tela4");

        // ===== AÇÕES DOS BOTÕES =====
        botaoComecar.addActionListener(e -> cardLayout.show(container, "tela2"));
        botaoTela3.addActionListener(e   -> cardLayout.show(container, "tela3"));
        finalizar.addActionListener(e    -> {
            cardLayout.show(container, "tela4");
            jogo.iniciar();
            janela.requestFocus();
        });

        // ===== TECLADO =====
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

        // ===== CONFIGURAR JANELA =====
        janela.add(container);
        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        janela.setVisible(true);
    }
}
