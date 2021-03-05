package com.focamacho.sealdrawapi.api;

import java.util.AbstractMap;

/**
 * Classe que que representa um
 * texto com uma ação clicável no chat.
 */
public class ChatButton {

    private String text = "";
    private String hover = "";
    private AbstractMap.SimpleEntry<ActionType, String> action;

    private ChatButton() {}

    /**
     * Método principal para a criação
     * de um objeto dessa classe.
     * @return um novo ChatButton.
     */
    protected static ChatButton create() {
        return new ChatButton();
    }

    /**
     * Define o texto desse botão.
     * @param text o texto desejado.
     * @return esse objeto.
     */
    public ChatButton setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Retorna o texto desse botão.
     * @return o texto do botão.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Define o texto exibido ao
     * passar o mouse nesse botão.
     * @param text o texto desejado.
     * @return esse objeto.
     */
    public ChatButton setHoverText(String text) {
        this.hover = text;
        return this;
    }

    /**
     * Retorna o texto ao passar o mouse
     * nesse botão.
     * @return o texto ao passar o mouse no botão.
     */
    public String getHoverText() {
        return hover;
    }

    /**
     * Define uma ação para ser efetuada
     * ao clicar no botão.
     * @param action a ação para ser efetuada.
     * @param value o valor para definir na ação.
     * @return esse objeto.
     */
    public ChatButton setAction(ActionType action, String value) {
        this.action = new AbstractMap.SimpleEntry<>(action, value);
        return this;
    }

    /**
     * Retorna a ação ao para ser efetuada
     * ao clicar no botão.
     * @return a ação para ser efetuada.
     */
    public AbstractMap.SimpleEntry<ActionType, String> getAction() {
        return action;
    }

    /**
     * Retorna o JSON gerado a partir
     * desse botão.
     * @return o JSON para o botão.
     */
    public String toJson() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{\"text\":\"")
                .append(text)
                .append("\"");

        if (action != null) {
            stringBuilder.append(",\"clickEvent\":{\"action\":\"")
                    .append(action.getKey().name().toLowerCase())
                    .append("\",\"value\":\"")
                    .append(action.getValue())
                    .append("\"}");
        }

        if (!hover.isEmpty()) {
            if(!stringBuilder.toString().endsWith(",")) stringBuilder.append(",");
            stringBuilder.append("\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"")
                    .append(hover)
                    .append("\"}}");
        }

        return stringBuilder.toString();
    }

    @SuppressWarnings("unused")
    enum ActionType {

        OPEN_URL,
        RUN_COMMAND,
        SUGGEST_COMMAND

    }

}
