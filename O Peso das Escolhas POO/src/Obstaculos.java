import java.awt.*;

public class Obstaculos {

    // ===== LISTA DE OBSTÁCULOS (x1, y1, x2, y2) proporcionais =====
    private static final double[][] obstaculosP = {
        {0.0,  0.0,  0.26, 0.25}, // Prédios/lojas topo esq
        {0.26, 0.0,  0.74, 0.25}, // Prédios/lojas topo centro
        {0.74, 0.0,  1.0,  0.25}, // Prédios/lojas topo dir
        {0.88, 0.0,  1.0,  0.27}, // Árvore topo dir
        {0.0,  0.42, 0.18, 0.43}, // Bancos esq
        {0.0,  0.34, 0.03, 0.50}, // Lanterna esq
        {0.94, 0.35, 1.0,  0.37}, // Hidrante + lanterna dir
        {0.0,  0.55, 0.33, 0.74}, // Casa
        {0.0,  0.55, 0.21, 0.78}, // Casa2
        {0.64, 0.76, 0.83, 0.82}, // Lanterna baixo
        {0.91, 0.80, 0.92, 0.82}, // Lixo baixo
        {0.68, 0.63, 0.94, 0.75}, // Área verde + árvore
        {0.80, 0.59, 0.90, 0.65}, // Topo da árvore
        {0.81, 0.56, 0.88, 0.65}, // Topo da árvore 2
    };

    // ===== VERIFICA COLISÃO E DESFAZ MOVIMENTO =====
    public void verificarColisao(Jogador jogador, int W, int H,
                                  boolean w, boolean s, boolean a, boolean d) {
        Rectangle playerRect = jogador.getHitbox(W, H);

        for (double[] obs : obstaculosP) {
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
    public void desenharDebug(Graphics g, int W, int H) {
        g.setColor(new Color(255, 0, 0, 100));
        for (double[] obs : obstaculosP) {
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
