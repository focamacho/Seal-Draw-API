package com.focamacho.sealdrawapi;

import com.focamacho.sealdrawapi.api.Paint;
import com.focamacho.sealdrawapi.api.Drawing;
import com.focamacho.sealdrawapi.impl.Implementations;

@SuppressWarnings("unused")
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
        Implementations.init(this);
    }

    /**
     * Cria um novo "editor" de imagem
     * com o desenho informado.
     * @param drawing o desenho desejado.
     * @return o novo "editor".
     */
    public Paint createPaint(Drawing drawing) {
        try {
            return (Paint) Implementations.paintConstructor.newInstance(this, drawing);
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
    public Paint getPaint(Object player) {
        return Paint.getPaint(this, player);
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
