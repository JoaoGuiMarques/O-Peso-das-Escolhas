import java.awt.*;

public class Obstaculos {

    // ===== mapa 1 =====
    private static final double[][] obstaculosMapa1 = {
            {0.0,  0.0,  0.26, 0.25}, // Prédios/lojas topo esq
            {0.26, 0.0,  0.74, 0.25}, // Prédios/lojas topo centro
            {0.74, 0.0,  1.0,  0.25}, // Prédios/lojas topo dir
            {0.88, 0.0,  1.0,  0.27}, // Árvore topo dir
            {0.94, 0.35, 1.0,  0.37}, // Hidrante + lanterna dir
            {0.0,  0.55, 0.30, 0.74}, // Casa
            {0.0,  0.62, 0.33, 0.78}, // Casa2
            {0.64, 0.76, 0.83, 0.82}, // Lanterna baixo
            {0.91, 0.80, 0.92, 0.82}, // Lixo baixo
            {0.68, 0.63, 0.94, 0.75}, // Área verde + árvore
            {0.80, 0.59, 0.90, 0.65}, // Topo da árvore
            {0.81, 0.56, 0.88, 0.65}, // Topo da árvore 2
    };

    // ===== mapa 2 =====
    private static final double[][] obstaculosMapa2 = {
            {0.02,  0.06,  0.11,  0.07},  // Arvore topo esq
            {0.19,  0.05,  0.27,  0.1},  // Arvore topo esq2
            {0.12,  0.07,  0.16,  0.08},  // Banco topo esq
            {0.39,  0.01,  0.54,  0.06},  // Pergola topo
            {0.33,  0.01,  0.35,  0.02},  // Lanterna topo esq
            {0.59,  0.01,  0.60,  0.02},  // Lanterna topo dir
            {0.67,  0.04,  0.75,  0.08},  // Arvore topo dir1
            {0.85,  0.03,  0.92,  0.06},  // Arvore topo dir2
            {0.78,  0.08,  0.82,   0.09},  // Banco topo dir
            {0.02,  0.40,  0.21,  0.53},  // Playground
            {0.07,  0.67,  0.14,  0.72},  // Arbusto playground esq
            {0.05,  0.72,  0.16,  0.74},  // Arbusto baixo esq1
            {0.44,  0.40,  0.55,  0.42},  // Fonte meio
            {0.77,  0.44,  0.90,  0.47},  // Lago (horizontal)
            {0.79,  0.39,  0.88,  0.44},  // Lago (vertical)
            {0.05,  0.72,  0.15,  0.74},  // Arbusto baixo esq1
            {0.90,  0.66,  0.99,  0.72},  // Arvore baixo dir
    };

    // ===== mapa 3 =====
    private static final double[][] obstaculosMapa3 = {
           // {0.0,  0.0,  1.0,  0.01}, // Grade/rio topo
            {0.21,  0.0,  0.22,  0.02}, // Poste esq superior
            {0.63,  0.0,  0.65,  0.03}, // Poste dir superior
            {0.02, 0.09, 0.18, 0.23}, // Arvore esq topo
            {0.66, 0.03, 0.76, 0.22}, // Casa dir1
            {0.76, 0.03, 0.87, 0.19}, // Casa dir2
            {0.90, 0.06, 0.98, 0.15}, // Arvore dir topo
            {0.02, 0.51, 0.19, 0.57}, // Telhado casa esq baixo
            {0.02, 0.56, 0.20, 0.82}, // Corpo casa esq baixo
            {0.02, 0.57, 0.27, 0.82}, // Canteiro casa esq baixo (saliência frontal)
            {0.77, 0.51, 1.0, 0.86}, // Area verde dir
            {0.0,  0.85, 1.0,  1.0},  // Grade baixo
            {0.42, 0.81, 0.44, 0.85}, // Lanterna baixo esq (poste central esq)
            {0.58, 0.81, 0.59, 0.85}, // Lanterna baixo dir (poste central dir)
            {0.44, 0.84, 0.57, 0.91}, // Portao baixo
    };

    // ===== retorna obstaculos do mapa atual =====
    private double[][] getObstaculos(int mapa) {
        switch (mapa) {
            case 2:  return obstaculosMapa2;
            case 3:  return obstaculosMapa3;
            default: return obstaculosMapa1;
        }
    }

    // ===== verifica colisao e desfaz movimento =====
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

    // ===== desenhar os obstaculos (debug) =====
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