import javax.swing.*;
import java.awt.*;

public class NPC {

    // ===== posicao proporcional =====
    private double xProporcional;
    private double yProporcional;
    private int x;
    private int y;

    // ===== imagem (retrato do npc, exibido andando no mapa) =====
    private Image imagem;

    // ===== imagem da situacao-problema (exibida no dialogo da pergunta) =====
    private Image imagemViolencia;

    // ===== pergunta e opcoes de cada pergunta =====
    private String pergunta;
    private String[] opcoes;
    private int opcaoCorreta;
    private int opcaoErrada;

    // ===== explicacao detalhada mostrada como feedback apos a resposta =====
    private String explicacao;

    // ===== categoria/tipo de violencia (usado no resumo da tela final) =====
    private String categoria;

    // ===== controle das perguntas =====
    private boolean perguntaFeita;

    // ===== construtor com imagem de situacao especifica =====
    public NPC(double xP, double yP, String caminhoImagem, String caminhoImagemViolencia,
               String pergunta, String[] opcoes,
               int opcaoCorreta, int opcaoErrada, String explicacao, String categoria) {
        this.xProporcional  = xP;
        this.yProporcional  = yP;
        this.imagem         = new ImageIcon(caminhoImagem).getImage();
        this.imagemViolencia = new ImageIcon(caminhoImagemViolencia).getImage();
        this.pergunta       = pergunta;
        this.opcoes         = opcoes;
        this.opcaoCorreta   = opcaoCorreta;
        this.opcaoErrada    = opcaoErrada;
        this.explicacao     = explicacao;
        this.categoria      = categoria;
        this.perguntaFeita  = false;
    }

    // ===== construtor sem imagem de situacao (usa o retrato do npc no dialogo) =====
    public NPC(double xP, double yP, String caminhoImagem,
               String pergunta, String[] opcoes,
               int opcaoCorreta, int opcaoErrada, String explicacao, String categoria) {
        this(xP, yP, caminhoImagem, caminhoImagem, pergunta, opcoes, opcaoCorreta, opcaoErrada, explicacao, categoria);
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

    // ===== feedback detalhado exibido apos a resposta (em vez de mostrar so a pontuacao) =====
    public String getFeedback(int escolha) {
        StringBuilder sb = new StringBuilder();

        if (escolha == opcaoCorreta) {
            sb.append("Boa escolha!\n\n");
        } else {
            sb.append("Essa não era a melhor escolha.\n\n");
            sb.append("A resposta mais adequada seria:\n\"").append(opcoes[opcaoCorreta]).append("\"\n\n");
        }

        sb.append(explicacao);
        return sb.toString();
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
    public Image getImagem() { return imagem; }
    public Image getImagemViolencia() { return imagemViolencia; }
    public String getCategoria() { return categoria; }
    public int getX() { return x; }
    public int getY() { return y; }
}