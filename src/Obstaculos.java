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
            {0.19,  0.05,  0.28,  0.1},  // Arvore topo esq2
            {0.12,  0.07,  0.17,  0.08},  // Banco topo esq
            {0.38,  0.01,  0.53,  0.06},  // Pergola topo
            {0.31,  0.01,  0.33,  0.02},  // Lanterna topo esq
            {0.58,  0.01,  0.59,  0.02},  // Lanterna topo dir
            {0.62,  0.05,  0.69,  0.13},  // Arvore topo dir1 (só tronco)
            {0.70,  0.05,  0.77,  0.14},  // Arvore topo dir2 (só tronco)
            {0.75,  0.09,  0.83,  0.19},  // Sorvetes
            {0.82,  0.18,  0.85,  0.21},  // Lixo sorvetes
            {0.85,  0.05,  0.92,  0.13},  // Arvore topo dir3 (só tronco)
            {0.92,  0.05,  0.99,  0.13},  // Arvore topo dir4 (só tronco)
            {0.88,  0.18,  1.0,   0.21},  // Banco topo dir
            {0.01,  0.30,  0.03,  0.37},  // Lanterna esq meio
            {0.02,  0.4,  0.21,  0.53},  // Playground
            {0.06,  0.6,  0.15,  0.70},  // Arbusto playground esq
            {0.23,  0.50,  0.25,  0.51},  // Lanterna playground
            {0.43,  0.40,  0.55,  0.42},  // Fonte meio
            {0.69,  0.36,  0.86,  0.44},  // Lago (só a borda sólida)
            {0.71,  0.43,  0.84,  0.46},  // Lago baixo
            {0.75,  0.49,  0.80,  0.50},  // Banco lago
            {0.71,  0.48,  0.72,  0.49},  // Lanterna dir meio
            {0.96,  0.32,  0.97,  0.33},  // Lanterna lago
            {0.04,  0.72,  0.16,  0.74},  // Arbusto baixo esq1
            {0.02,  0.82,  0.04,  0.83},  // Lanterna baixo esq
            {0.68,  0.70,  0.77,  0.75},  // Arvore baixo dir1
            {0.90,  0.64,  0.99,  0.68},  // Arvore baixo dir3
            {0.27,  0.72,  0.32,  0.73},  // Banco baixo centro
            {0.37,  0.74,  0.38,  0.75},  // Lanterna baixo centro1
            {0.57,  0.76,  0.58,  0.77},  // Lanterna baixo centro2
    };

    // ===== mapa 3 =====
    private static final double[][] obstaculosMapa3 = {
            {0.0,  0.0,  1.0,  0.01}, // Grade/rio topo
            {0.20,  0.0,  0.21,  0.03}, // Poste esq superior
            {0.63,  0.0,  0.65,  0.03}, // Poste dir superior
            {0.02, 0.09, 0.18, 0.23}, // Arvore esq topo
            {0.66, 0.03, 0.78, 0.22}, // Casa dir topo
            {0.84, 0.06, 0.93, 0.20}, // Arvore dir topo (termina antes do NPC em 0.92)
            {0.02, 0.52, 0.19, 0.57}, // Telhado casa esq baixo
            {0.02, 0.56, 0.20, 0.82}, // Corpo casa esq baixo
            {0.02, 0.58, 0.27, 0.82}, // Canteiro casa esq baixo (saliência frontal)
            {0.77, 0.52, 1.0, 0.86}, // Area verde dir
            {0.0,  0.86, 1.0,  1.0},  // Grade baixo
            {0.41, 0.81, 0.44, 0.85}, // Lanterna baixo esq (poste central esq)
            {0.57, 0.81, 0.59, 0.85}, // Lanterna baixo dir (poste central dir)
            {0.38, 0.84, 0.57, 0.91}, // Portao baixo
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