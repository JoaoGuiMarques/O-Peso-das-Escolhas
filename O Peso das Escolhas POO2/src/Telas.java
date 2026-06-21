import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Telas {

    // ===== TELA 1 =====
    public static JPanel criarTela1(JButton botaoComecar) {
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
                botaoComecar.setBounds(w / 2 - 110, (int)(h * 0.80), 220, 30);
            }
        });

        return painel;
    }

    // ===== TELA 2 =====
    public static JPanel criarTela2(JButton botaoTela3) {
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
                botaoTela3.setBounds(w / 2 - 130, (int)(h * 0.88), 260, 30);
            }
        });

        return painel;
    }

    // ===== TELA 3 =====
    public static JPanel criarTela3(JButton finalizar) {
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
                finalizar.setBounds(w / 5 - 140, (int)(h * 0.85), 220, 30);
            }
        });

        return painel;
    }
}
