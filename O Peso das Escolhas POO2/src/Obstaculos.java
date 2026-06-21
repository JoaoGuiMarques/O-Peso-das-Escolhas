import java.awt.*;

public class Obstaculos {

    // ===== MAPA 1 =====
    private static final double[][] obstaculosMapa1 = {
        {0.0,  0.0,  0.26, 0.25}, // Prédios/lojas topo esq
        {0.26, 0.0,  0.74, 0.25}, // Prédios/lojas topo centro
        {0.74, 0.0,  1.0,  0.25}, // Prédios/lojas topo dir
        {0.88, 0.0,  1.0,  0.27}, // Árvore topo dir
        {0.0,  0.42, 0.18, 0.43}, // Bancos esq
        {0.0,  0.34, 0.03, 0.50}, // Lanterna esq
        {0.94, 0.35, 1.0,  0.37}, // Hidrante + lanterna dir
        {0.0,  0.55, 0.30, 0.74}, // Casa
        {0.0,  0.62, 0.33, 0.78}, // Casa2
        {0.64, 0.76, 0.83, 0.82}, // Lanterna baixo
        {0.91, 0.80, 0.92, 0.82}, // Lixo baixo
        {0.68, 0.63, 0.94, 0.75}, // Área verde + árvore
        {0.80, 0.59, 0.90, 0.65}, // Topo da árvore
        {0.81, 0.56, 0.88, 0.65}, // Topo da árvore 2
    };

    // ===== MAPA 2 (Shopping) =====
    private static final double[][] obstaculosMapa2 = {
        {0.0,  0.0,  1.0,  0.08}, // Lojas topo
        {0.0,  0.0,  0.07, 0.14}, // Parede esq topo
        {0.92, 0.0,  1.0,  0.14}, // Parede dir topo
        {0.0,  0.35, 0.12, 0.50}, // Loja esq meio
        {0.87, 0.35, 1.0,  0.50}, // Loja dir meio
        {0.24, 0.38, 0.43, 0.62}, // Escada esq
        {0.43, 0.38, 0.57, 0.62}, // Escada centro
        {0.57, 0.38, 0.74, 0.62}, // Escada dir
        {0.24, 0.51, 0.31, 0.62}, // Placa esq
        {0.65, 0.51, 0.71, 0.62}, // Placa dir
        {0.0,  0.72, 0.20, 1.0},  // Loja esportes
        {0.80, 0.72, 1.0,  1.0},  // Loja beleza
    };

    // ===== MAPA 3 (Rua noturna) =====
    private static final double[][] obstaculosMapa3 = {
        {0.0,  0.0,  1.0,  0.11}, // Rio + grade topo
        {0.0,  0.09, 0.20, 0.38}, // Arvore esq
        {0.15, 0.09, 0.18, 0.22}, // Lanterna esq topo
        {0.65, 0.03, 0.86, 0.35}, // Casa dir topo
        {0.86, 0.03, 1.0,  0.30}, // Arvore dir topo
        {0.61, 0.11, 0.65, 0.24}, // Lanterna dir topo
        {0.13, 0.44, 0.16, 0.55}, // Lanterna esq meio
        {0.0,  0.50, 0.21, 0.85}, // Casa esq baixo
        {0.71, 0.44, 0.75, 0.56}, // Lanterna dir meio
        {0.71, 0.50, 1.0,  0.89}, // Area verde dir
        {0.0,  0.84, 1.0,  1.0},  // Grade baixo
        {0.35, 0.73, 0.38, 0.88}, // Lanterna baixo esq
        {0.59, 0.73, 0.62, 0.88}, // Lanterna baixo dir
        {0.35, 0.84, 0.62, 0.92}, // Portao baixo
    };

    // ===== RETORNA OBSTÁCULOS DO MAPA ATUAL =====
    private double[][] getObstaculos(int mapa) {
        switch (mapa) {
            case 2:  return obstaculosMapa2;
            case 3:  return obstaculosMapa3;
            default: return obstaculosMapa1;
        }
    }

    // ===== VERIFICA COLISÃO E DESFAZ MOVIMENTO =====
    public void verificarColisao(Jogador jogador, int W, int H,
                                  boolean w, boolean s, boolean a, boolean d,
                                  int mapa) {
        Rectangle playerRect = jogador.getHitbox(W, H);

        for (double[] obs : getObstaculos(mapa)) {
            int ox = (int)(W * obs[0]);
            int oy = (int)(H * obs[1]);
            int ow = (int)(W * obs[2]) - ox;
            int oh = (int)(H * obs[3]) - oy;
            Rectangle obsRect = new Rectangle(ox, oy, ow, oh);

            if (playerRect.intersects(obsRect)) {
                int vel = jogador.getVelocidade();
                if (w) jogador.setY(jogador.getY() + vel);
                if (s) jogador.setY(jogador.getY() - vel);
                if (a) jogador.setX(jogador.getX() + vel);
                if (d) jogador.setX(jogador.getX() - vel);
                break;
            }
        }
    }

    // ===== DESENHAR DEBUG =====
    public void desenharDebug(Graphics g, int W, int H, int mapa) {
        g.setColor(new Color(255, 0, 0, 100));
        for (double[] obs : getObstaculos(mapa)) {
            int ox = (int)(W * obs[0]);
            int oy = (int)(H * obs[1]);
            int ow = (int)(W * obs[2]) - ox;
            int oh = (int)(H * obs[3]) - oy;
            g.fillRect(ox, oy, ow, oh);
            g.setColor(Color.RED);
            g.drawRect(ox, oy, ow, oh);
            g.setColor(new Color(255, 0, 0, 100));
        }
    }
}
