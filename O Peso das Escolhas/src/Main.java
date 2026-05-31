import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    //PERSONAGEM PRINCIPAL
    static int playerX = 100;
    static int playerY = 0;
    static int velocidade = 15;

    //POSIÇÕES DOS NPCS
    static int[] npcX = {0, 0, 0, 0, 0};
    static int[] npcY = {0, 0, 0, 0, 0};

    //CONTROLE DAS PERGUNTAS
    static boolean[] perguntaFeita = {false, false, false, false, false};

    //PONTUAÇÃO
    static int pontos = 0;

    public static void main(String[] args) {

        //JANELA ÚNICA
        JFrame janela = new JFrame("O Peso das Escolhas");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //CARDLAYOUT PARA TROCAR TELAS
        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        //TELA 1
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

        //TELA 2
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

        //TELA 3
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

        //TELA 4 (JOGO)
        JPanel painel4 = new JPanel() {
            Image fundo   = new ImageIcon("img/fundoPrincipal.jpeg").getImage();
            Image jogador = new ImageIcon("img/personagem.png").getImage();
            Image npc1    = new ImageIcon("img/npc1.png").getImage();
            Image npc2    = new ImageIcon("img/npc2.png").getImage();
            Image npc3    = new ImageIcon("img/npc3.png").getImage();
            Image npc4    = new ImageIcon("img/npc4.png").getImage();
            Image npc5    = new ImageIcon("img/npc5.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                //POSIÇÕES DINÂMICAS
                int chao = (int)(getHeight() * 0.64);
                playerY = chao;
                for (int i = 0; i < npcY.length; i++) npcY[i] = chao;

                int larguraTela = getWidth();
                npcX[0] = (int)(larguraTela * 0.20);
                npcX[1] = (int)(larguraTela * 0.35);
                npcX[2] = (int)(larguraTela * 0.50);
                npcX[3] = (int)(larguraTela * 0.65);
                npcX[4] = (int)(larguraTela * 0.80);

                //TAMANHO PROPORCIONAL
                int altura  = (int)(getHeight() * 0.20);
                int largura = (int)(getWidth()  * 0.05);
                int alturaJogador  = (int)(getHeight() * 0.20);
                int larguraJogador = (int)(getWidth()  * 0.04);

                g.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);
                g.drawImage(jogador, playerX, playerY, larguraJogador, alturaJogador, this);
                if (!perguntaFeita[0]) g.drawImage(npc1, npcX[0], npcY[0], largura, altura, this);
                if (!perguntaFeita[1]) g.drawImage(npc2, npcX[1], npcY[1], largura, altura, this);
                if (!perguntaFeita[2]) g.drawImage(npc3, npcX[2], npcY[2], largura, altura, this);
                if (!perguntaFeita[3]) g.drawImage(npc4, npcX[3], npcY[3], largura, altura, this);
                if (!perguntaFeita[4]) g.drawImage(npc5, npcX[4], npcY[4], largura, altura, this);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Pontos: " + pontos, 30, 50);
            }
        };
        painel4.setLayout(null);

        //ADICIONAR TELAS AO CONTAINER
        container.add(painel1, "tela1");
        container.add(painel2, "tela2");
        container.add(painel3, "tela3");
        container.add(painel4, "tela4");

        //AÇÕES DOS BOTÕES
        botaoComecar.addActionListener(e -> cardLayout.show(container, "tela2"));
        botaoTela3.addActionListener(e  -> cardLayout.show(container, "tela3"));
        finalizar.addActionListener(e   -> {
            cardLayout.show(container, "tela4");
            janela.requestFocus();
        });

        //MOVIMENTAÇÃO
        janela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int tecla = e.getKeyCode();

                if (tecla == KeyEvent.VK_RIGHT) playerX += velocidade;
                if (tecla == KeyEvent.VK_LEFT)  playerX -= velocidade;
                if (playerX < 0) playerX = 0;

                //VERIFICAR COLISÕES
                for (int i = 0; i < npcX.length; i++) {
                    Rectangle player   = new Rectangle(playerX, playerY, 50, 180);
                    Rectangle npcAtual = new Rectangle(npcX[i], npcY[i], 50, 180);

                    if (player.intersects(npcAtual) && !perguntaFeita[i]) {
                        perguntaFeita[i] = true;

                        String pergunta = "";
                        String[] opcoes = {};

                        if (i == 0) {
                            pergunta = "Quando eu fico com muita raiva da minha namorada, às vezes eu a empurro para que ela pare de discutir.";
                            opcoes = new String[]{"Se foi só um empurrão, não é tão grave.", "Todo casal briga as vezes.", "Empurrar alguém é agressão e não resolve problemas."};
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
                                janela, pergunta, "Escolha",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null, opcoes, opcoes[0]
                        );

                        if (i == 0) pontos += (escolha == 2) ? 10 : (escolha == 0) ? -10 : 0;
                        if (i == 1) pontos += (escolha == 1) ? 10 : (escolha == 2) ? -10 : 0;
                        if (i == 2) pontos += (escolha == 1) ? 10 : (escolha == 0) ? -10 : 0;
                        if (i == 3) pontos += (escolha == 0) ? 10 : (escolha == 2) ? -10 : 0;
                        if (i == 4) pontos += (escolha == 0) ? 10 : (escolha == 1) ? 0  : -10;

                        JOptionPane.showMessageDialog(
                                janela, "Pontuação atual: " + pontos
                        );

                        //VERIFICAR FIM DO JOGO
                        boolean terminou = true;
                        for (boolean respondeu : perguntaFeita) {
                            if (!respondeu) {
                                terminou = false;
                                break;
                            }
                        }

                        //FINALIZAR
                        if (terminou) {
                            String feedback;

                            if (pontos < 30) {
                                feedback = "Você precisa estudar mais sobre o assunto.";
                            } else if (pontos <= 40) {
                                feedback = "Você está no caminho certo!";
                            } else {
                                feedback = "Parabéns, você entende muito do assunto!";
                            }

                            JOptionPane.showMessageDialog(janela,
                                    "FIM DE JOGO!\n\n" +
                                            "Pontuação final: " + pontos + "\n\n" +
                                            feedback
                            );
                            janela.dispose();
                        }
                    }
                }
                painel4.repaint();
            }
        });

        //CONFIGURAR JANELA
        janela.add(container);
        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        janela.setVisible(true);
    }
}