import javax.swing.*;
import java.awt.*;

public class NPC {

    // ===== posicao proporcional =====
    private double xProporcional;
    private double yProporcional;
    private int x;
    private int y;

    // ===== imagem =====
    private Image imagem;

    // ===== pergunta e opcoes de cada pergunta =====
    private String pergunta;
    private String[] opcoes;
    private int opcaoCorreta;
    private int opcaoErrada;

    // ===== controle das perguntas =====
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

    // ===== atualiza posicao baseada no tamanho da tela =====
    public void atualizarPosicao(int W, int H) {
        x = (int)(W * xProporcional);
        y = (int)(H * yProporcional);
    }

    // ===== desenhar npcs =====
    public void desenhar(Graphics g, int W, int H) {
        if (!perguntaFeita) {
            g.drawImage(imagem, x, y, getLargura(W), getAltura(H), null);
        }
    }

    // ===== calcular pontuacao cada resposta  =====
    public int calcularPontos(int escolha) {
        if (escolha == opcaoCorreta) return 10;
        if (escolha == opcaoErrada)  return -10;
        return -10;
    }

    // ===== hitbox =====
    public Rectangle getHitbox(int W, int H) {
        return new Rectangle(x, y, getHitboxLargura(W), getHitboxAltura(H));
    }

    // ===== tamanho da hitbox =====
    public int getHitboxLargura(int W) { return (int)(W * 0.03); }
    public int getHitboxAltura(int H)  { return (int)(H * 0.05); }

    // ===== tamanho proporcional =====
    public int getLargura(int W) { return (int)(W * 0.04); }
    public int getAltura(int H)  { return (int)(H * 0.15); }

    // ===== metodos getters e setters =====
    public boolean isPerguntaFeita() { return perguntaFeita; }
    public void setPerguntaFeita(boolean feita) { this.perguntaFeita = feita; }
    public String getPergunta() { return pergunta; }
    public String[] getOpcoes() { return opcoes; }
    public int getX() { return x; }
    public int getY() { return y; }
}
