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

        // ===== telas 1, 2 e 3 =====
        JPanel painel1 = Telas.criarTela1(botaoComecar);
        JPanel painel2 = Telas.criarTela2(botaoTela3);
        JPanel painel3 = Telas.criarTela3(finalizar);

        // ===== fundos dos mapas =====
        Image[] fundos = {
                new ImageIcon("img/fundoPrincipal.jpeg").getImage(),
                new ImageIcon("img/fundoMapa2.jpeg").getImage(),
                new ImageIcon("img/fundoMapa3.jpeg").getImage()
        };

        // ===== controle do piscar da seta =====
        boolean[] setaPiscando = {true};
        int[] contadorPiscar   = {0};

        Jogo[] jogoRef = new Jogo[1];

        // ===== referencias mutaveis para permitir "jogar novamente" e mostrar a tela final =====
        Runnable[] onFimDeJogoRef  = new Runnable[1];
        Runnable[] reiniciarJogoRef = new Runnable[1];

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

                // ===== desenhar npcs =====
                for (NPC npc : npcs) npc.desenhar(g, W, H);

                // ===== desenhar jogador =====
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



                // ===== SETA PISCANDO NA BORDA DIREITA =====
                if (jogo.isSetaVisivel()) {
                    contadorPiscar[0]++;
                    if (contadorPiscar[0] >= 20) {
                        setaPiscando[0] = !setaPiscando[0];
                        contadorPiscar[0] = 0;
                    }

                    if (setaPiscando[0]) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        int cx = W - 40;
                        int cy = H / 2;
                        int tam = 30;

                        // fundo semitransparente da seta
                        g2.setColor(new Color(0, 0, 0, 120));
                        g2.fillRoundRect(cx - tam - 8, cy - tam - 8, tam * 2 + 16, tam * 2 + 16, 12, 12);

                        // seta →
                        g2.setColor(new Color(255, 220, 50));
                        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(cx - tam, cy, cx + tam, cy);           // haste
                        g2.drawLine(cx + tam, cy, cx + tam - 14, cy - 14); // ponta cima
                        g2.drawLine(cx + tam, cy, cx + tam - 14, cy + 14); // ponta baixo

                        // texto abaixo da seta
                        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
                        g2.setColor(new Color(255, 220, 50));
                        String texto = "PRÓXIMO";
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(texto, cx - fm.stringWidth(texto) / 2, cy + tam + 22);
                    }
                }

                // ===== pontuação =====
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Pontos: " + jogo.getPontos(), 30, 50);

                // ===== fase =====
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Fase " + jogo.getMapaAtual() + " de 3", 30, 85);
            }
        };
        painelJogo.setLayout(null);

        // ===== referencia pro painel da tela final atual, pra poder substituir sem acumular =====
        JPanel[] telaFinalRef = new JPanel[1];

        // ===== callback chamado quando o jogo termina de vez (mapa 3 concluido) =====
        onFimDeJogoRef[0] = () -> {
            if (telaFinalRef[0] != null) {
                container.remove(telaFinalRef[0]);
            }
            JPanel telaFinal = TelaFinal.criarTelaFinal(
                    jogoRef[0].getPontos(),
                    jogoRef[0].getResultadosPorCategoria(),
                    () -> reiniciarJogoRef[0].run(),
                    () -> { janela.dispose(); System.exit(0); }
            );
            telaFinalRef[0] = telaFinal;
            container.add(telaFinal, "telaFinal");
            cardLayout.show(container, "telaFinal");
        };

        // ===== callback chamado pelo botao "JOGAR NOVAMENTE" da tela final =====
        reiniciarJogoRef[0] = () -> {
            Jogo novoJogo = new Jogo(janela, painelJogo, () -> painelJogo.repaint(), onFimDeJogoRef[0]);
            jogoRef[0] = novoJogo;
            cardLayout.show(container, "tela4");
            novoJogo.iniciar();
            janela.requestFocus();
        };

        // ===== criar jogo =====
        Jogo jogo = new Jogo(janela, painelJogo, () -> painelJogo.repaint(), onFimDeJogoRef[0]);
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
            jogoRef[0].iniciar();
            janela.requestFocus();
        });

        // ===== teclado (sempre usa o jogo atual, mesmo apos reiniciar) =====
        janela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP:    jogoRef[0].setW(true); break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  jogoRef[0].setS(true); break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  jogoRef[0].setA(true); break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: jogoRef[0].setD(true); break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP:    jogoRef[0].setW(false); break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  jogoRef[0].setS(false); break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  jogoRef[0].setA(false); break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: jogoRef[0].setD(false); break;
                }
            }
        });

        // ===== configurar janela =====
        janela.add(container);
        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        janela.setVisible(true);
    }
}