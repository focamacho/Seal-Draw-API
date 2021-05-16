package com.focamacho.sealdrawapi.api.lib;

@SuppressWarnings("unused")
public class PaintButton {

    private final String placeholder;
    private String text;
    private String hover;
    private IPaintRunnable action;
    private ChatButton cacheChatButton;

    private PaintButton(String placeholder, String text, String hover, IPaintRunnable action) {
        this.placeholder = placeholder;
        this.text = text;
        this.hover = hover;
        this.action = action;
        updateChatButton();
    }

    /**
     * Cria um novo PaintButton.
     *
     * @return o PaintButton criado.
     */
    public static PaintButton create(String placeholder) {
        return new PaintButton(placeholder, "", "", (paint, player) -> {});
    }

    /**
     * Cria um ChatButton a partir desse
     * PaintButton.
     *
     * @return o ChatButton criado.
     */
    public ChatButton getChatButton() {
        return cacheChatButton;
    }

    /**
     * Atualiza ou cria o ChatButton referente a
     * esse objeto.
     */
    private void updateChatButton() {
        this.cacheChatButton = ChatButton.create().setText(this.text.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setHoverText(this.hover.replace("&", "§").replace("\\n", "\n").replace("\n", "\\n"))
                .setAction(ChatButton.ActionType.RUN_COMMAND, "/sdwa b " + this.placeholder);
    }

    /**
     * Retorna o placeholder utilizado para
     * esse botão.
     *
     * @return o placeholder do botão.
     */
    public String getPlaceholder() {
        return this.placeholder;
    }

    /**
     * Retorna o texto exibido
     * nesse botão.
     *
     * @return o texto do botão.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Define o texto exibido
     * nesse botão.
     *
     * @param text o texto do botão.
     * @return esse objeto.
     */
    public PaintButton setText(String text) {
        this.text = text;
        updateChatButton();
        return this;
    }

    /**
     * Retorna o texto exibido ao
     * passar o mouse nesse botão.
     *
     * @return o texto ao passar o mouse no botão.
     */
    public String getHover() {
        return this.hover;
    }

    /**
     * Define o texto exibido ao
     * passar o mouse nesse botão.
     *
     * @param hover o texto ao passar o mouse no botão.
     * @return esse objeto.
     */
    public PaintButton setHover(String hover) {
        this.hover = hover;
        updateChatButton();
        return this;
    }

    /**
     * Retorna a ação acionada ao
     * clicar nesse botão.
     *
     * @return a ação do botão.
     */
    public IPaintRunnable getAction() {
        return this.action;
    }

    /**
     * Define a ação acionada ao
     * clicar nesse botão.
     *
     * @param action a ação do botão.
     * @return esse objeto.
     */
    public PaintButton setAction(IPaintRunnable action) {
        this.action = action;
        updateChatButton();
        return this;
    }

}
