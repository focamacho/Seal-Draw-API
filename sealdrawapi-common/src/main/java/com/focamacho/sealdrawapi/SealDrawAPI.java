package com.focamacho.sealdrawapi;

import com.focamacho.sealdrawapi.api.AbstractPaint;
import com.focamacho.sealdrawapi.api.Drawing;
import com.focamacho.sealdrawapi.impl.Implementations;

public class SealDrawAPI {

    private final Object plugin;

    /**
     * Construtor principal da API.
     * @param plugin a instância do plugin
     *               que está criando esse
     *               construtor.
     */
    public SealDrawAPI(Object plugin) {
        this.plugin = plugin;
    }

    /**
     * Cria um novo "editor" de imagem
     * com o desenho informado.
     */
    public AbstractPaint createPaint(Drawing drawing) {
        try {
            return (AbstractPaint) Implementations.paintConstructor.newInstance(drawing);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Retorna o "editor" de imagem que
     * o usuário está usando no momento.
     * Caso não estiver usando nenhum,
     * o retorno é null.
     * @param player o jogador para consulta.
     * @return a instância dessa classe sendo usada,
     * ou null caso o jogador não esteja com nenhum
     * editor aberto.
     */
    public AbstractPaint getPaint(Object player) {
        return AbstractPaint.getPaint(player);
    }

    /**
     * Retorna a instância do plugin que
     * criou esse objeto da API.
     * @return o plugin.
     */
    public Object getPlugin() {
        return this.plugin;
    }

}
