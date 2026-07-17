import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Jogo {

    // ===== mapa atual =====
    private int mapaAtual = 1;

    // ===== seta de saída =====
    private boolean setaVisivel = false;

    // ===== objetos do jogo =====
    private Jogador jogador;
    private Obstaculos obstaculos;

    // ===== npcs por mapa =====
    private NPC[] npcsMapa1;
    private NPC[] npcsMapa2;
    private NPC[] npcsMapa3;

    // ===== pontuacao =====
    private int pontos;

    // ===== resultado por categoria/tipo de violencia: categoria -> {acertos, erros} =====
    private Map<String, int[]> resultadosPorCategoria;
    private static final String[] CATEGORIAS = {"Física", "Psicológica", "Moral", "Patrimonial", "Sexual"};

    // ===== callback disparado quando o jogo termina de vez (mapa 3 concluido) =====
    private Runnable onFimDeJogo;

    // ===== controle das teclas =====
    private boolean wPressionado = false;
    private boolean sPressionado = false;
    private boolean aPressionado = false;
    private boolean dPressionado = false;

    // ===== timer =====
    private Timer timer;

    // ===== referencias =====
    private JFrame janela;
    private JPanel painelJogo;

    // ===== callback para trocar de mapa =====
    private Runnable onTrocarMapa;

    public Jogo(JFrame janela, JPanel painelJogo, Runnable onTrocarMapa, Runnable onFimDeJogo) {
        this.janela       = janela;
        this.painelJogo   = painelJogo;
        this.onTrocarMapa = onTrocarMapa;
        this.onFimDeJogo  = onFimDeJogo;
        this.pontos       = 0;

        // ===== inicializa o resumo por categoria ja com todas zeradas (aparecem mesmo com 0) =====
        resultadosPorCategoria = new LinkedHashMap<>();
        for (String categoria : CATEGORIAS) {
            resultadosPorCategoria.put(categoria, new int[]{0, 0});
        }

        jogador    = new Jogador();
        obstaculos = new Obstaculos();

        // ===== npcs no mapa 1 =====
        npcsMapa1 = new NPC[]{
                new NPC(0.08, 0.30, "img/npc1.png", "img/violenciaFisica.png",
                        "Quando eu fico com muita raiva da minha namorada, às vezes eu a empurro para que ela pare de discutir.",
                        new String[]{"Se foi só um empurrão, não é tão grave.", "Todo casal briga às vezes.", "Empurrar alguém é agressão e não resolve problemas."},
                        2, 0,
                        "Empurrar, apertar ou usar força contra o(a) parceiro(a) já é violência física, mesmo que pareça pouco grave. Esse tipo de atitude tende a se repetir e piorar com o tempo. O ideal é conversar com um adulto de confiança e, se precisar, buscar apoio pelo Disque 180 (Central de Atendimento à Mulher).", "Física"),
                new NPC(0.35, 0.60, "img/npc2.png", "img/violenciaPsicologica.png",
                        "Meu namorado me diz que ninguém vai me querer sem ele, me rebaixa e diz que se não fosse ele eu viveria sozinha.",
                        new String[]{"Talvez ele só falou sem pensar.", "Isso é manipulação, você precisa terminar esse relacionamento.", "Ele só está sendo sincero."},
                        1, 2,
                        "Dizer que 'ninguém vai querer você' ou que a pessoa não sobrevive sem o(a) parceiro(a) é manipulação emocional, um tipo de violência psicológica que tenta destruir a autoestima e a independência da vítima. Ninguém deve se sentir incapaz de viver sem o outro — buscar apoio de amigos, família ou de um psicólogo faz toda a diferença.", "Psicológica"),
                new NPC(0.55, 0.30, "img/npc3.png", "img/violenciaSexual.png",
                        "Na festa um menino me fez beijar ele à força, mesmo eu tendo falado que não queria muitas vezes.",
                        new String[]{"Ele só estava tentando te conquistar.", "Isso é assédio, você não deu consentimento. Vamos denunciar.", "Isso acontece em festas, é bem normal."},
                        1, 0,
                        "Insistir em beijar ou tocar alguém depois de um 'não', mesmo em uma festa, é violência sexual. Consentimento precisa ser dado livremente e pode ser retirado a qualquer momento. Em situações assim, é importante contar para um adulto de confiança e, se necessário, denunciar pelo Disque 100.", "Sexual"),
                new NPC(0.78, 0.30, "img/npc4.png", "img/violenciaMoral.png",
                        "Eu discuti com uma menina da minha sala e fiquei espalhando mentiras sobre ela. Foi engraçado.",
                        new String[]{"Espalhar mentiras prejudica a dignidade das pessoas.", "Isso acontece na escola, é normal.", "Era só fofoca, relaxa."},
                        0, 2,
                        "Espalhar mentiras sobre alguém para prejudicar sua reputação é violência moral — no caso de calúnia ou difamação, pode até ser crime. Mesmo que pareça 'só uma brincadeira', o dano à vida da pessoa é real.", "Moral"),
                new NPC(0.24, 0.82, "img/npc5.png", "img/violenciaPatrimonial.png",
                        "Meu namorado quebra minhas coisas quando fica nervoso. Ele já quebrou meu celular e muitas maquiagens minhas.",
                        new String[]{"Isso é violência patrimonial e deve ser denunciado.", "Foi só um momento de raiva.", "Ele só estava estressado."},
                        0, 2,
                        "Quebrar objetos pessoais do(a) parceiro(a) durante uma discussão é violência patrimonial. Quando isso se repete, não é 'só um momento de raiva' — é uma forma de intimidação e controle.", "Patrimonial"),
        };

        // ===== npcs no mapa 2 =====
        npcsMapa2 = new NPC[]{
                new NPC(0.20, 0.70, "img/npc6.png", "img/violenciaPsicologica.png",
                        "Eu faço minha namorada se sentir culpada quando sai com amigos, pois não gosto quando ela sai sem mim.",
                        new String[]{"Você só está protegendo ela.", "Ciúme é prova de amor.", "Isso é controle emocional e violência psicológica."},
                        2, 1,
                        "Fazer o(a) parceiro(a) se sentir culpada por ter amigos e uma vida social própria é uma forma de controle emocional. Ciúme excessivo não é prova de amor: é uma tentativa de isolar a pessoa de quem pode ajudá-la.", "Psicológica"),
                new NPC(0.47, 0.16, "img/npc7.png", "img/violenciaMoral.png",
                        "Minha ex fica me mandando mensagem pedindo para eu parar de espalhar fofoca dela, mas eu continuo porque ela merece.",
                        new String[]{"Você precisa parar de fazer isso agora!", "Ela não sabe de nada, merece mesmo.", "Isso acontece às vezes, relaxa."},
                        0, 1,
                        "Continuar espalhando algo sobre uma pessoa mesmo depois que ela pediu para parar é violência moral e pode configurar perseguição, dependendo da gravidade. Respeitar o pedido e parar é o mínimo — a atitude certa é se desculpar e interromper imediatamente.", "Moral"),
                new NPC(0.85, 0.52, "img/npc8.png", "img/violenciaPatrimonial.png",
                        "Quando minha namorada me irrita, eu escondo o celular dela.",
                        new String[]{"Depois você devolve, tá de boa.", "Controlar ou esconder objetos pessoais é violência.", "Você só quer chamar atenção dela."},
                        1, 2,
                        "Esconder os pertences de alguém como forma de punição ou controle também é violência patrimonial, mesmo que o objeto seja devolvido depois. Não é uma atitude 'de boa' — é uma forma de exercer poder sobre o outro.", "Patrimonial"),
                new NPC(0.48, 0.45, "img/npc9.png", "img/violenciaFisica.png",
                        "Meu namorado me chama de inútil e me bate quando eu faço algo que ele não gosta.",
                        new String[]{"Você precisa denunciar ele, ele não pode te bater.", "Talvez ele já esteja acostumada.", "Ele só fez sem pensar, além dele estar certo."},
                        0, 2,
                        "Agressão física combinada com humilhações verbais é um padrão grave de violência que tende a piorar com o tempo. Ninguém deve permanecer nessa situação — buscar ajuda pelo Disque 180 ou em uma Delegacia da Mulher é essencial.", "Física"),
                new NPC(0.73, 0.82, "img/npc10.png", "img/violenciaSexual.png",
                        "Ele insiste em tocar em partes do meu corpo quando eu disse que não gostava.",
                        new String[]{"Ele só tá demonstrando carinho.", "Você não consentiu, isso é crime!", "Você deve aceitar isso, ele te ama."},
                        1, 0,
                        "Tocar o corpo de alguém depois que a pessoa já disse que não gosta é violência sexual, mesmo dentro de um relacionamento. O consentimento deve ser respeitado sempre, sem exceção.", "Sexual"),
        };

        // ===== npcs no mapa 3 =====
        npcsMapa3 = new NPC[]{
                new NPC(0.23, 0.42, "img/npc11.png", "img/violenciaSexual.png",
                        "Eu falei que não queria beijar ele, mas ele insistiu mesmo assim.",
                        new String[]{"Talvez ele só estivesse animado.", "Se você não queria, ele deveria respeitar. Vamos denunciar.", "Isso acontece às vezes."},
                        1, 0,
                        "Insistir em um beijo depois de um 'não' é violência sexual, independente da intenção de quem insiste. Respeitar a recusa é obrigatório — quem insiste está desrespeitando a vontade da outra pessoa.", "Sexual"),
                new NPC(0.30, 0.13, "img/npc12.png", "img/violenciaPatrimonial.png",
                        "Eu soube que minha namorada fez algo que eu não queria e escondi o celular dela, quebrei também uns materiais dela.",
                        new String[]{"Violar a privacidade de alguém é errado e pode ser crime.", "Entre casal não pode ter segredo.", "Depende do que tinha no celular."},
                        0, 1,
                        "Mexer no celular de alguém sem permissão fere sua privacidade, e destruir seus pertences é violência patrimonial. Nenhuma dessas atitudes é justificável, mesmo dentro de um relacionamento.", "Patrimonial"),
                new NPC(0.68, 0.67, "img/npc13.png", "img/violenciaPsicologica.png",
                        "Meu namorado controla tudo: o que eu visto, com quem falo e onde vou.",
                        new String[]{"Todo casal tem suas regras.", "Ele só quer o melhor para você.", "Isso é controle e isolamento, formas de violência."},
                        2, 1,
                        "Controlar roupas, amizades e a rotina do(a) parceiro(a) é um tipo de violência psicológica chamado controle coercitivo. Esse comportamento costuma isolar a vítima de sua rede de apoio, tornando mais difícil pedir ajuda.", "Psicológica"),
                new NPC(0.57, 0.20, "img/npc14.png", "img/violenciaMoral.png",
                        "Vi um colega humilhando a namorada dele na frente de todo mundo e todo mundo riu.",
                        new String[]{"Humilhar alguém em público é violência moral. Deveria intervir.", "Não é problema meu me meter.", "Foi só uma brincadeira, não tem problema."},
                        0, 2,
                        "Humilhar alguém na frente dos outros causa dano à dignidade da pessoa e é uma forma de violência moral. Rir ou ficar calado normaliza esse comportamento — apoiar a vítima ou chamar um adulto responsável faz diferença.", "Moral"),
                new NPC(0.92, 0.32, "img/npc15.png", "img/violenciaFisica.png",
                        "Vi um colega empurrando a namorada dele na minha frente.",
                        new String[]{"Isso acontece às vezes.", "Isso é violência física e deve ser denunciado.", "Foi só discussão. Ele precisava impor respeito."},
                        1, 2,
                        "Presenciar uma agressão física exige atitude: procurar ajuda, avisar um adulto responsável ou até acionar o Disque 180. Ficar em silêncio contribui para que o ciclo de violência continue.", "Física"),
        };

        timer = new Timer(16, e -> atualizar());
    }

    // ===== npcs mapa atual =====
    public NPC[] getNpcsAtuais() {
        switch (mapaAtual) {
            case 2:  return npcsMapa2;
            case 3:  return npcsMapa3;
            default: return npcsMapa1;
        }
    }

    // ===== atualização da lógica do jogo =====
    private void atualizar() {
        int W = painelJogo.getWidth();
        int H = painelJogo.getHeight();

        jogador.mover(wPressionado, sPressionado, aPressionado, dPressionado);
        jogador.aplicarLimites(W, H);
        obstaculos.verificarColisao(jogador, W, H, wPressionado, sPressionado, aPressionado, dPressionado, mapaAtual);

        // ===== VERIFICAR SE JOGADOR CHEGOU NA BORDA DIREITA =====
        if (setaVisivel && jogador.getX() + jogador.getLargura(W) >= W - 10) {
            setaVisivel = false;

            double xNascer = mapaAtual == 1 ? 0.042 : 0.034;
            double yNascer = mapaAtual == 1 ? 0.200 : 0.349;
            jogador.resetarPosicao(xNascer, yNascer, W, H);

            mapaAtual++;
            onTrocarMapa.run();
            return;
        }

        for (NPC npc : getNpcsAtuais()) {
            if (!npc.isPerguntaFeita() && jogador.getHitbox(W, H).intersects(npc.getHitbox(W, H))) {
                npc.setPerguntaFeita(true);
                pararTeclas();

                int escolha = DialogoPersonalizado.mostrarPergunta(janela, npc.getPergunta(), npc.getOpcoes(), npc.getImagemViolencia());
                int pontosGanhos = npc.calcularPontos(escolha);
                pontos += pontosGanhos;
                registrarResultadoCategoria(npc.getCategoria(), pontosGanhos > 0);
                DialogoPersonalizado.mostrarMensagem(janela, npc.getFeedback(escolha), npc.getImagemViolencia());

                verificarFimDeMapa();
            }
        }

        painelJogo.repaint();
    }

    // ===== verificar fim do mapa atual =====
    private void verificarFimDeMapa() {
        for (NPC npc : getNpcsAtuais()) {
            if (!npc.isPerguntaFeita()) return;
        }

        if (mapaAtual < 3) {
            String msg = mapaAtual == 1
                    ? "Você ajudou todos aqui!\nCaminhe para a direita para seguir em frente..."
                    : "Incrível! Mais um lugar ajudado!\nCaminhe para a direita para o último lugar...";
            DialogoPersonalizado.mostrarMensagem(janela, msg);
            setaVisivel = true;
        } else {
            // ===== encerra o timer e avisa que os 3 mapas foram concluidos =====
            timer.stop();
            DialogoPersonalizado.mostrarMensagem(janela,
                    "Você concluiu os 3 lugares!\nVamos ver o seu resultado...");

            // ===== a tela final dedicada e quem mostra pontuacao, resumo e feedback =====
            if (onFimDeJogo != null) {
                onFimDeJogo.run();
            } else {
                janela.dispose();
            }
        }
    }

    // ===== registra o acerto/erro de uma categoria (tipo de violencia) =====
    private void registrarResultadoCategoria(String categoria, boolean acertou) {
        int[] contador = resultadosPorCategoria.computeIfAbsent(categoria, k -> new int[]{0, 0});
        if (acertou) contador[0]++; else contador[1]++;
    }

    // ===== parar pressionamento das teclas =====
    public void pararTeclas() {
        wPressionado = false;
        sPressionado = false;
        aPressionado = false;
        dPressionado = false;
    }

    // ===== iniciar timer =====
    public void iniciar() { timer.start(); }

    // ===== getters =====
    public Jogador getJogador()       { return jogador; }
    public NPC[] getNpcs()            { return getNpcsAtuais(); }
    public Obstaculos getObstaculos() { return obstaculos; }
    public int getPontos()            { return pontos; }
    public int getMapaAtual()         { return mapaAtual; }
    public boolean isSetaVisivel()    { return setaVisivel; }
    public Map<String, int[]> getResultadosPorCategoria() { return resultadosPorCategoria; }

    // ===== controle das teclas =====
    public void setW(boolean v) { wPressionado = v; }
    public void setS(boolean v) { sPressionado = v; }
    public void setA(boolean v) { aPressionado = v; }
    public void setD(boolean v) { dPressionado = v; }

    public boolean isAndando() {
        return wPressionado || sPressionado || aPressionado || dPressionado;
    }
}