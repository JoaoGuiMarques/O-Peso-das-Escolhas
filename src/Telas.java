import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Telas {

    // ===== cores do padrao dialogo =====
    private static final Color COR_FUNDO        = new Color(20, 10, 35);
    private static final Color COR_BORDA        = new Color(140, 80, 200);
    private static final Color COR_BOTAO_FUNDO  = new Color(60, 20, 100);
    private static final Color COR_BOTAO_HOVER  = new Color(120, 50, 180);
    private static final Color COR_BOTAO_BORDA  = new Color(160, 90, 220);
    private static final Color COR_TEXTO        = new Color(230, 210, 255);

    // ===== fonte pixelada =====
    private static final Font FONTE_BOTAO = new Font("Monospaced", Font.BOLD, 14);

    // ===== botoes iniciais estilizados =====
    public static JButton criarBotaoEstilizado(String texto) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // fundo
                if (getModel().isRollover()) {
                    g2.setColor(COR_BOTAO_HOVER);
                } else {
                    g2.setColor(COR_BOTAO_FUNDO);
                }
                g2.fillRect(0, 0, getWidth(), getHeight());

                // borda externa roxa
                g2.setColor(COR_BORDA);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);

                // borda interna mais suave
                g2.setColor(COR_BOTAO_BORDA);
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(4, 4, getWidth() - 9, getHeight() - 9);

                // texto
                g2.setFont(getFont());
                g2.setColor(COR_TEXTO);
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), textX, textY);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // não pintar borda padrão — feita no paintComponent
            }
        };

        botao.setFont(FONTE_BOTAO);
        botao.setForeground(COR_TEXTO);
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setOpaque(false);

        return botao;
    }

    // ===== tela 1 =====
    public static JPanel criarTela1(JButton botaoComecar) {
        aplicarEstilo(botaoComecar, "COMEÇAR O JOGO");

        JPanel painel = new JPanel() {
            Image fundo = new ImageIcon("img/background.jpeg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel.setLayout(null);
        painel.add(botaoComecar);

        painel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = painel.getWidth();
                int h = painel.getHeight();
                botaoComecar.setBounds(w / 2 - 140, (int)(h * 0.80), 280, 42);
            }
        });

        return painel;
    }

    // ===== tela 2 =====
    public static JPanel criarTela2(JButton botaoTela3) {
        aplicarEstilo(botaoTela3, "VOU TE AJUDAR COM CERTEZA!");

        JPanel painel = new JPanel() {
            Image fundo = new ImageIcon("img/apresentacao.jpeg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel.setLayout(null);
        painel.add(botaoTela3);

        painel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = painel.getWidth();
                int h = painel.getHeight();
                botaoTela3.setBounds(w / 2 - 185, (int)(h * 0.88), 370, 42);
            }
        });

        return painel;
    }

    // ===== tela 3 =====
    public static JPanel criarTela3(JButton finalizar) {
        aplicarEstilo(finalizar, "VAMOS PARA O JOGO!");

        JPanel painel = new JPanel() {
            Image fundo = new ImageIcon("img/dicas.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fundo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painel.setLayout(null);
        painel.add(finalizar);

        painel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = painel.getWidth();
                int h = painel.getHeight();
                finalizar.setBounds((int)(w * 0.19) - 140, (int)(h * 0.85), 280, 42);
            }
        });

        return painel;
    }

    // ===== aplicar estilo e texto ao botao existente =====
    private static void aplicarEstilo(JButton botao, String novoTexto) {
        botao.setText(novoTexto);
        botao.setFont(FONTE_BOTAO);
        botao.setForeground(COR_TEXTO);
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setOpaque(false);
        botao.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                AbstractButton b = (AbstractButton) c;

                // fundo
                if (b.getModel().isRollover()) {
                    g2.setColor(COR_BOTAO_HOVER);
                } else {
                    g2.setColor(COR_BOTAO_FUNDO);
                }
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());

                // borda externa
                g2.setColor(COR_BORDA);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(1, 1, c.getWidth() - 3, c.getHeight() - 3);

                // borda interna
                g2.setColor(COR_BOTAO_BORDA);
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(4, 4, c.getWidth() - 9, c.getHeight() - 9);

                // texto centralizado
                g2.setFont(b.getFont());
                g2.setColor(COR_TEXTO);
                FontMetrics fm = g2.getFontMetrics();
                int textX = (c.getWidth() - fm.stringWidth(b.getText())) / 2;
                int textY = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(b.getText(), textX, textY);
            }
        });
    }
}