import javax.swing.*;
import java.awt.*;

public class Jogador {

    // ===== POSIÇÃO =====
    private int x;
    private int y;
    private int velocidade;

    // ===== DIREÇÃO E ANIMAÇÃO =====
    private String direcao;
    private int frameAnimacao;
    private int contadorAnimacao;
    private static final int VELOCIDADE_ANIMACAO = 10;

    // ===== SPRITES ESTÁTICOS =====
    private Image spFrente;
    private Image spCostas;
    private Image spDireita;
    private Image spEsquerda;

    // ===== SPRITES ANDANDO =====
    private Image spAndandoFrente;
    private Image spAndandoCostas;
    private Image spAndandoDireita;
    private Image spAndandoEsquerda;

    public Jogador() {
        this.x = 0;
        this.y = 0;
        this.velocidade = 6;
        this.direcao = "frente";
        this.frameAnimacao = 0;
        this.contadorAnimacao = 0;

        spFrente          = new ImageIcon("img/player_frente.png").getImage();
        spCostas          = new ImageIcon("img/player_costas.png").getImage();
        spDireita         = new ImageIcon("img/player_direita.png").getImage();
        spEsquerda        = new ImageIcon("img/player_esquerda.png").getImage();
        spAndandoFrente   = new ImageIcon("img/player_mov_frente.png").getImage();
        spAndandoCostas   = new ImageIcon("img/player_mov_costas.png").getImage();
        spAndandoDireita  = new ImageIcon("img/player_mov_direita.png").getImage();
        spAndandoEsquerda = new ImageIcon("img/player_mov_esquerda.png").getImage();
    }

    // ===== INICIALIZA POSIÇÃO NA PRIMEIRA VEZ =====
    public void inicializarPosicao(int W, int H) {
        if (x == 0 && y == 0) {
            x = (int)(W * 0.50);
            y = (int)(H * 0.65);
        }
    }

    // ===== RESETAR POSIÇÃO (ao trocar de mapa) =====
    public void resetarPosicao() {
        x = 0;
        y = 0;
    }

    // ===== MOVIMENTAÇÃO =====
    public void mover(boolean w, boolean s, boolean a, boolean d) {
        if (w) { y -= velocidade; direcao = "costas"; }
        if (s) { y += velocidade; direcao = "frente"; }
        if (a) { x -= velocidade; direcao = "esquerda"; }
        if (d) { x += velocidade; direcao = "direita"; }
    }

    // ===== LIMITES DA TELA =====
    public void aplicarLimites(int W, int H) {
        int larg = getLargura(W);
        int alt  = getAltura(H);
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + larg > W) x = W - larg;
        if (y + alt  > H) y = H - alt;
    }

    // ===== ATUALIZAR ANIMAÇÃO =====
    public void atualizarAnimacao() {
        contadorAnimacao++;
        if (contadorAnimacao >= VELOCIDADE_ANIMACAO) {
            frameAnimacao = (frameAnimacao + 1) % 2;
            contadorAnimacao = 0;
        }
    }

    // ===== SPRITE ATUAL =====
    public Image getSpriteAtual(boolean estaAndando) {
        if (estaAndando && frameAnimacao == 1) {
            switch (direcao) {
                case "costas":   return spAndandoCostas;
                case "direita":  return spAndandoDireita;
                case "esquerda": return spAndandoEsquerda;
                default:         return spAndandoFrente;
            }
        } else {
            switch (direcao) {
                case "costas":   return spCostas;
                case "direita":  return spDireita;
                case "esquerda": return spEsquerda;
                default:         return spFrente;
            }
        }
    }

    // ===== HITBOX =====
    public Rectangle getHitbox(int W, int H) {
        return new Rectangle(x, y, getLargura(W), getAltura(H));
    }

    // ===== TAMANHO PROPORCIONAL =====
    public int getLargura(int W) { return (int)(W * 0.04); }
    public int getAltura(int H)  { return (int)(H * 0.15); }

    // ===== GETTERS E SETTERS =====
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getVelocidade() { return velocidade; }
}
