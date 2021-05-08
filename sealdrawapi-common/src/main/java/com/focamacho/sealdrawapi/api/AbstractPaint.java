package com.focamacho.sealdrawapi.api;

import com.focamacho.sealdrawapi.SealDrawAPI;
import com.focamacho.sealdrawapi.api.lib.ChatButton;
import com.focamacho.sealdrawapi.api.lib.IPaintRunnable;

import java.util.*;

/**
 * Classe que representa
 * o "editor" de imagem
 * no chat.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public abstract class AbstractPaint {

    private static final Map<SealDrawAPI, List<AbstractPaint>> allPaints = new HashMap<>();

    private final SealDrawAPI api;

    public Map<Object, Character> players = new HashMap<>();
    protected Drawing drawing;
    private boolean stopChat = true;

    private boolean isBeforeRaw = false;
    private List<String> beforeMessages = new ArrayList<>();

    private boolean isAfterRaw = false;
    private List<String> afterMessages = new ArrayList<>();

    private int spaces = 22;
    private char[] availableColors = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private ChatButton cancelButton = ChatButton.create().setText("§c[Cancelar]").setHoverText("§cClique aqui para cancelar.").setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa b ca");
    private ChatButton cleanButton = ChatButton.create().setText("§f[Limpar]").setHoverText("§fClique aqui para limpar.").setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa b cl");
    private ChatButton confirmButton = ChatButton.create().setText("§a[Confirmar]").setHoverText("§aClique aqui para confirmar.").setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa b co");
    private ChatButton colorButton = ChatButton.create().setText("§%color%█").setHoverText("§%color%Clique aqui para escolher essa cor.").setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa c %color%");

    protected IPaintRunnable onCancel = (player, paint) -> {};
    protected IPaintRunnable onConfirm = (player, paint) -> {};
    protected IPaintRunnable onClean = (player, paint) -> {};
    protected IPaintRunnable onOpen = (player, paint) -> {};
    protected IPaintRunnable onUpdate = (player, paint) -> {};
    protected IPaintRunnable onClose = (player, paint) -> {};

    /**
     * O construtor padrão para a criação
     * de um "editor" de imagem.
     *
     * @param drawing o desenho para editar.
     */
    public AbstractPaint(SealDrawAPI api, Drawing drawing) {
        this.drawing = drawing;
        this.api = api;

        if(!allPaints.containsKey(api)) allPaints.put(api, new ArrayList<>());
        allPaints.get(api).add(this);

        beforeMessages.add("                             §d§lSeal Draw API §%selected%█");
        beforeMessages.add("                      %colorselector%");
        beforeMessages.add("");

        afterMessages.add("                    %cancel% %clean% %confirm%");
    }

    /**
     * Abre esse "editor" de imagem
     * para um jogador.
     *
     * @param player o jogador.
     */
    public void openPaint(Object player) {
        if(!allPaints.get(api).contains(this)) allPaints.get(api).add(this);

        //Verificar se o jogador não possui outro editor
        //aberto. Se sim, fechá-lo.
        AbstractPaint paint = getPaint(api, player);
        if(paint != null) paint.closePaint(player);

        players.put(player, '0');
        onOpen.run(player, this);

        updatePaint();
    }

    /**
     * Fecha esse "editor" de imagem
     * para um jogador.
     *
     * @param player o jogador.
     */
    public void closePaint(Object player) {
        if(players.containsKey(player)) {
            players.remove(player);
            onClose.run(player, this);
        }
        if(players.isEmpty()) allPaints.get(api).remove(this);
    }

    /**
     * Fecha esse "editor" de imagem
     * para todos os jogadores que
     * possuem ele aberto.
     *
     * @param fireRunnable se a ação definida em
     *                     AbstractPaint#setOnClose deve
     *                     ser executada ou não.
     */
    public void closePaintForAll(boolean fireRunnable) {
        Iterator<Map.Entry<Object, Character>> playersIterator = players.entrySet().iterator();
        while(playersIterator.hasNext()) {
            Map.Entry<Object, Character> entry = playersIterator.next();
            if(fireRunnable) onClose.run(entry.getKey(), this);
            playersIterator.remove();
        }
        allPaints.get(api).remove(this);
    }

    /**
     * Atualiza a imagem para os jogadores
     * que possuem o editor aberto.
     */
    public abstract void updatePaint();

    /**
     * Retorna o "editor" de imagem que
     * o usuário está usando no momento.
     * Caso não estiver usando nenhum,
     * o retorno é null.
     *
     * @param player o jogador para consulta.
     * @return a instância dessa classe sendo usada,
     * ou null caso o jogador não esteja com nenhum
     * editor aberto.
     */
    public static AbstractPaint getPaint(SealDrawAPI api, Object player) {
        if(!allPaints.containsKey(api)) allPaints.put(api, new ArrayList<>());
        for (AbstractPaint paint : allPaints.get(api)) {
            if(paint.isEditing(player)) return paint;
        }
        return null;
    }

    /**
     * Retorna o desenho que é
     * editado por esse "editor".
     *
     * @return o desenho.
     */
    public Drawing getDrawing() {
        return this.drawing;
    }

    /**
     * Retorna a cor que o jogador
     * possui selecionada no
     * editor.
     *
     * @param player o jogador desejado.
     * @return a cor selecionada.
     */
    public char getSelectedColor(Object player) {
        return this.players.getOrDefault(player, 'f');
    }

    /**
     * Define a cor que o jogador
     * possui selecionada no
     * editor.
     *
     * @param player o jogador para trocar
     *               a cor.
     * @param color a cor para definir.
     */
    public void setSelectedColor(Object player, char color) {
        for (char availableColor : availableColors) {
            if(availableColor == color) {
                if(players.containsKey(player)) {
                    players.put(player, color);
                }
                break;
            }
        }
    }

    /**
     * Limpa o desenho, deixando a tela
     * toda em branco.
     *
     * @return esse objeto.
     */
    public AbstractPaint clear() {
        for(int row = 0; row < this.drawing.getRows(); row++) {
            for(int column = 0; column < this.drawing.getColumns(); column++) {
                this.drawing.setColor(row, column, 'f');
            }
        }
        return this;
    }

    /**
     * Define a quantia de espaços
     * antes do desenho.
     * Esse método é usado para formatação
     * caso você queira tentar centralizar
     * o seu desenho.
     *
     * @param spaces a quantia de espaços.
     * @return esse objeto.
     */
    public AbstractPaint setSpaces(int spaces) {
        this.spaces = spaces;
        return this;
    }

    /**
     * Define as mensagens que deverão aparecer
     * antes do desenho.
     * Esse método é usado para formatação caso
     * você queria escrever algo logo acima
     * do desenho.
     *
     * @param isRaw se as mensagens estão em formato
     *              "raw" (JSON) ou não.
     * @param messages a mensagens que serão exibidas
     *                antes do desenho.
     * @return esse objeto.
     */
    public AbstractPaint setBeforeMessages(boolean isRaw, String... messages) {
        this.isBeforeRaw = isRaw;
        this.beforeMessages = Arrays.asList(messages);
        return this;
    }

    /**
     * Retorna as mensagens que são enviadas antes
     * do editor de imagem no chat.
     *
     * @return as mensagens enviadas antes do editor.
     */
    public List<String> getBeforeMessages() {
        return this.beforeMessages;
    }

    /**
     * Retorna se as mensagens definidas para serem
     * enviadas antes do editor de imagem no chat
     * estão em formato JSON ou não.
     *
     * @return se as mensagens antes do editor estão em JSON.
     */
    public boolean isBeforeRaw() {
        return this.isBeforeRaw;
    }

    /**
     * Define as mensagens que deverão aparecer
     * após o desenho.
     * Esse método é usado para formatação caso
     * você queria escrever algo logo abaixo
     * do desenho.
     *
     * @param isRaw se as mensagens estão em formato
     *              "raw" (JSON) ou não.
     * @param messages a mensagens que serão exibidas
     *                antes do desenho.
     * @return esse objeto.
     */
    public AbstractPaint setAfterMessage(boolean isRaw, String... messages) {
        this.isAfterRaw = isRaw;
        this.afterMessages = Arrays.asList(messages);
        return this;
    }

    /**
     * Retorna se as mensagens definidas para serem
     * enviadas após o editor de imagem no chat
     * estão em formato JSON ou não.
     *
     * @return se as mensagens após o editor estão em JSON.
     */
    public boolean isAfterRaw() {
        return this.isAfterRaw;
    }

    /**
     * Retorna as mensagens que são enviadas após
     * o editor de imagem no chat.
     *
     * @return as mensagens enviadas após o editor.
     */
    public List<String> getAfterMessages() {
        return this.afterMessages;
    }

    /**
     * Define as cores disponíveis no editor.
     * O padrão são todas as cores do Minecraft,
     * de '0' até '9' e de 'a' até 'f'.
     *
     * @param colors as cores disponíveis.
     * @return esse objeto.
     */
    public AbstractPaint setAvailableColors(char... colors) {
        this.availableColors = colors;
        return this;
    }

    /**
     * Define o texto que é exibido no
     * botão de cancelar.
     *
     * @param text o texto desejado.
     * @param hover o texto desejado ao passar
     *              o mouse no botão.
     * @return esse objeto.
     */
    public AbstractPaint setCancelText(String text, String hover) {
        this.cancelButton = ChatButton.create().setText(text.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
            .setHoverText(hover.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa b ca");
        return this;
    }

    /**
     * Define o texto que é exibido no
     * botão de limpar.
     *
     * @param text o texto desejado.
     * @param hover o texto desejado ao passar
     *              o mouse no botão.
     * @return esse objeto.
     */
    public AbstractPaint setCleanText(String text, String hover) {
        this.cleanButton = ChatButton.create().setText(text.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setHoverText(hover.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa b cl");
        return this;
    }

    /**
     * Define o texto que é exibido no
     * botão de confirmar.
     *
     * @param text o texto desejado.
     * @param hover o texto desejado ao passar
     *              o mouse no botão.
     * @return esse objeto.
     */
    public AbstractPaint setConfirmText(String text, String hover) {
        this.confirmButton = ChatButton.create().setText(text.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setHoverText(hover.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa b co");
        return this;
    }

    /**
     * Define o texto que é exibido no
     * botão de escolha de cor.
     *
     * @param text o texto desejado.
     * @param hover o texto desejado ao passar
     *              o mouse no botão.
     * @return esse objeto.
     */
    public AbstractPaint setColorText(String text, String hover) {
        this.colorButton = ChatButton.create().setText(text.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setHoverText(hover.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa c %color%");
        return this;
    }

    /**
     * Retorna todos os jogadores que
     * possuem esse editor aberto.
     *
     * @return os jogadores.
     */
    public List<Object> getPlayers() {
        return new ArrayList<>(this.players.keySet());
    }

    /**
     * Retorna a mensagem em JSON utilizada
     * pelo editor.
     * Método usado somente internamente.
     *
     * @param player o jogador para quem a mensagem
     *               é direcionada.
     * @return a mensagem em JSON do editor.
     */
    public String getEditorMessage(Object player) {
        StringBuilder stringBuilder = new StringBuilder("[{\"text\":\"");

        if(!beforeMessages.isEmpty()) beforeMessages.forEach(msg -> stringBuilder.append(replacePlaceholders((this.isBeforeRaw ? "\"}," + msg + ",{\"text\":\"\\n" : msg + "\"},{\"text\":\"\\n"), player)));

        //Texto do editor
        for(int row = 0; row < this.drawing.getRows(); row++) {
            //Definir os espaços antes do desenho
            for(int i = 0; i < this.spaces; i++) {
                stringBuilder.append(" ");
            }
            stringBuilder.append("\"},{\"text\":\"");

            for(int column = 0; column < this.drawing.getColumns(); column++) {
                stringBuilder.append("§")
                        .append(this.drawing.getColor(row, column))
                        .append("█\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/sdwa p ")
                        .append(row).append(" ")
                        .append(column).append("\"}},{\"text\":\"");
            }

            stringBuilder.append("\\n\"},{\"text\":\"");
        }

        for(int i = 0; i < afterMessages.size(); i++) {
            String msg = afterMessages.get(i);
            stringBuilder.append(replacePlaceholders((this.isBeforeRaw ? "\"}," + msg + ",{\"text\":\"" + (i + 1 == afterMessages.size() ? "" : "\\n") : msg + "\"},{\"text\":\"" + (i + 1 == afterMessages.size() ? "" : "\\n")), player));
        }

        return stringBuilder.append("\"}]").toString().replace("{\"text\":\"\"},", "");
    }

    /**
     * Retorna a mensagem em JSON utilizada
     * para escolha de cores.
     * Método usado somente internamente.
     *
     * @return a mensagem em JSON do editor.
     */
    protected String getColorSelector() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"},");

        for (char availableColor : availableColors) {
            ChatButton button = ChatButton.create()
                    .setText(colorButton.getText().replace("%color%", "" + availableColor))
                    .setHoverText(colorButton.getHoverText().replace("%color%", "" + availableColor));
            if(colorButton.getAction() != null) button.setAction(colorButton.getAction().getKey(), colorButton.getAction().getValue().replace("%color%", "" + availableColor));
            stringBuilder.append(button.toJson()).append(",");
        }

        stringBuilder.append("{\"text\":\"");
        return stringBuilder.toString();
    }

    /**
     * Retorna se o jogador inserido
     * está ou não usando esse editor.
     *
     * @param player o jogador desejado.
     * @return true se o jogador estiver com
     * esse editor aberto e false caso
     * não esteja.
     */
    public boolean isEditing(Object player) {
        return this.players.containsKey(player);
    }

    /**
     * Define algo para acontecer quando
     * um jogador clica no botão "cancelar"
     * do editor.
     *
     * @param onCancel o IDrawingRunnable para
     *                 ser executado.
     * @return esse objeto.
     */
    public AbstractPaint setOnCancel(IPaintRunnable onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    /**
     * Define algo para acontecer quando
     * um jogador clica no botão "confirmar"
     * do editor.
     *
     * @param onConfirm o IDrawingRunnable para
     *                 ser executado.
     * @return esse objeto.
     */
    public AbstractPaint setOnConfirm(IPaintRunnable onConfirm) {
        this.onConfirm = onConfirm;
        return this;
    }

    /**
     * Define algo para acontecer quando
     * um jogador clica no botão "limpar"
     * do editor.
     *
     * @param onClean o IDrawingRunnable para
     *                 ser executado.
     * @return esse objeto.
     */
    public AbstractPaint setOnClean(IPaintRunnable onClean) {
        this.onClean = onClean;
        return this;
    }

    /**
     * Define algo para acontecer quando
     * um jogador abre um "editor".
     *
     * @param onOpen o IDrawingRunnable para
     *                 ser executado.
     * @return esse objeto.
     */
    public AbstractPaint setOnOpen(IPaintRunnable onOpen) {
        this.onOpen = onOpen;
        return this;
    }

    /**
     * Define algo para acontecer quando
     * o editor é atualizado, e as mensagens
     * são enviadas para os usuários que
     * possuem ele aberto.
     * Isso acontece quando um usuário troca
     * a cor selecionada, ou colore um pixel
     * do desenho.
     *
     * @param onUpdate o IDrawingRunnable para
     *                 ser executado.
     * @return esse objeto.
     */
    public AbstractPaint setOnUpdate(IPaintRunnable onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    /**
     * Define algo para acontecer quando
     * o "editor" é fechado para algum
     * usuário.
     *
     * @param onClose o IDrawingRunnable para
     *                 ser executado.
     * @return esse objeto.
     */
    public AbstractPaint setOnClose(IPaintRunnable onClose) {
        this.onClose = onClose;
        return this;
    }

    /**
     * Retorna a ação definida para ser
     * executada ao clicar no botão de
     * cancelar no editor.
     *
     * @return a ação ao cancelar.
     */
    public IPaintRunnable getOnCancel() {
        return this.onCancel;
    }

    /**
     * Retorna a ação definida para ser
     * executada ao clicar no botão de
     * confirmar no editor.
     *
     * @return a ação ao confirmar.
     */
    public IPaintRunnable getOnConfirm() {
        return this.onConfirm;
    }

    /**
     * Retorna a ação definida para ser
     * executada ao clicar no botão de
     * limpar no editor.
     *
     * @return a ação ao limpar.
     */
    public IPaintRunnable getOnClean() {
        return this.onClean;
    }

    /**
     * Retorna a ação definida para ser
     * executada ao abrir o editor.
     *
     * @return a ação ao abrir.
     */
    public IPaintRunnable getOnOpen() {
        return this.onOpen;
    }

    /**
     * Retorna a ação definida para ser
     * executada ao atualizar o editor.
     *
     * @return a ação ao atualizar.
     */
    public IPaintRunnable getOnUpdate() {
        return this.onUpdate;
    }

    /**
     * Retorna a ação definida para ser
     * executada ao fechar o editor.
     *
     * @return a ação ao fechar.
     */
    public IPaintRunnable getOnClose() {
        return this.onClose;
    }

    /**
     * Define se o chat deve ou não
     * ser pausado para o jogador
     * que está com esse "editor" aberto.
     *
     * @param stop true para pausar o chat, false
     *             para não pausar.
     * @return esse objeto.
     */
    public AbstractPaint setStopChat(boolean stop) {
        this.stopChat = stop;
        return this;
    }

    /**
     * Retorna se o editor deve parar
     * ou não o chat quando estiver
     * aberto.
     *
     * @return se deve ou não parar o chat.
     */
    public boolean isStopChat() {
        return this.stopChat;
    }

    /**
     * Da replace na string para adicionar os
     * botões de confirmar, cancelar, limpar
     * e seletor de cores.
     *
     * @param str a string para substituir os placeholders
     *            pelos botões.
     * @param player o jogador atual.
     * @return a string com os botões adicionados.
     */
    protected String replacePlaceholders(String str, Object player) {
        return str.replace("%selected%", "" + players.getOrDefault(player, '0'))
                .replace("%colorselector%", getColorSelector())
                .replace("%confirm%", "\"}," + confirmButton.toJson() + ",{\"text\":\"")
                .replace("%cancel%", "\"}," + cancelButton.toJson() + ",{\"text\":\"")
                .replace("%clean%", "\"}," + cleanButton.toJson() + ",{\"text\":\"");

    }

}
