import javax.swing.*;
import java.awt.*;

public class NPC {

    // ===== POSIÇÃO PROPORCIONAL =====
    private double xProporcional;
    private double yProporcional;
    private int x;
    private int y;

    // ===== IMAGEM =====
    private Image imagem;

    // ===== PERGUNTA E OPÇÕES =====
    private String pergunta;
    private String[] opcoes;
    private int opcaoCorreta;
    private int opcaoErrada;

    // ===== CONTROLE =====
    private boolean perguntaFeita;

    public NPC(double xP, double yP, String caminhoImagem,
               String pergunta, String[] opcoes,
               int opcaoCorreta, int opcaoErrada) {
        this.xProporcional = xP;
        this.yProporcional = yP;
        this.imagem        = new ImageIcon(caminhoImagem).getImage();
        this.pergunta      = pergunta;
        this.opcoes        = opcoes;
        this.opcaoCorreta  = opcaoCorreta;
        this.opcaoErrada   = opcaoErrada;
        this.perguntaFeita = false;
    }

    // ===== ATUALIZA POSIÇÃO BASEADO NO TAMANHO DA TELA =====
    public void atualizarPosicao(int W, int H) {
        x = (int)(W * xProporcional);
        y = (int)(H * yProporcional);
    }

    // ===== DESENHAR =====
    public void desenhar(Graphics g, int W, int H) {
        if (!perguntaFeita) {
            g.drawImage(imagem, x, y, getLargura(W), getAltura(H), null);
        }
    }

    // ===== CALCULAR PONTUAÇÃO =====
    public int calcularPontos(int escolha) {
        if (escolha == opcaoCorreta) return 10;
        if (escolha == opcaoErrada)  return -10;
        return 0;
    }

    // ===== HITBOX =====
    public Rectangle getHitbox(int W, int H) {
        return new Rectangle(x, y, getHitboxLargura(W), getHitboxAltura(H));
    }

    // ===== TAMANHO DA HITBOX =====
    public int getHitboxLargura(int W) { return (int)(W * 0.03); }
    public int getHitboxAltura(int H)  { return (int)(H * 0.05); }

    // ===== TAMANHO PROPORCIONAL =====
    public int getLargura(int W) { return (int)(W * 0.04); }
    public int getAltura(int H)  { return (int)(H * 0.15); }

    // ===== GETTERS E SETTERS =====
    public boolean isPerguntaFeita() { return perguntaFeita; }
    public void setPerguntaFeita(boolean feita) { this.perguntaFeita = feita; }
    public String getPergunta() { return pergunta; }
    public String[] getOpcoes() { return opcoes; }
    public int getX() { return x; }
    public int getY() { return y; }
}
