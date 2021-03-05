package com.focamacho.sealdrawapi.api;

/**
 * Classe que representa um
 * desenho.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Drawing {

    private final int rows;
    private final int columns;
    private final char[][] drawing;

    /**
     * Construtor para criar um novo
     * desenho em branco.
     * @param rows número de linhas
     *             do desenho.
     * @param columns número de colunas do
     *                desenho.
     */
    public Drawing(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.drawing = getDefault(rows, columns, 'f');
    }

    /**
     * Construtor para criar um novo
     * desenho em branco com uma cor
     * de fundo.
     * @param rows número de linhas
     *             do desenho.
     * @param columns número de colunas do
     *                desenho.
     * @param color cor de fundo.
     */
    public Drawing(int rows, int columns, char color) {
        this.rows = rows;
        this.columns = columns;
        this.drawing = getDefault(rows, columns, color);
    }

    /**
     * Cria um desenho a partir de um String.
     * Feito para ser usado em conjunto com
     * o método Drawing#toString(), permitindo salvar
     * os desenhos em uma String.
     * @param drawing o desenho em string, gerado
     *                pelo método toString.
     * @return o desenho.
     */
    public static Drawing fromString(String drawing) {
        String[] split = drawing.split("\n");

        int rows = split.length;
        int columns = split.length > 0 ? split[0].length() : 0;

        Drawing dw = new Drawing(rows, columns);
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                dw.setColor(row, column, split[row].charAt(column));
            }
        }

        return dw;
    }

    /**
     * Converte o desenho em uma String,
     * permitindo que ele seja salvo em
     * um banco de dados, por exemplo.
     * @return o desenho em string.
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int row = 0; row < this.rows; row++) {
            if(row > 0) stringBuilder.append("\n");
            for(int column = 0; column < this.columns; column++) {
                stringBuilder.append(this.drawing[row][column]);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Retorna o número de linhas
     * que esse desenho possui.
     * @return número de linhas do desenho.
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Retorna o número de colunas
     * que esse desenho possui.
     * @return número de colunas do desenho.
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Cria um desenho em branco.
     * @param rows número de linhas
     *             do desenho.
     * @param columns número de colunas
     *                do desenho.
     * @param color cor de fundo.
     */
    private char[][] getDefault(int rows, int columns, char color) {
        char[][] drawing = new char[rows][columns];
        for(int row = 0; row < drawing.length; row++) {
            char[] columnsA = drawing[row];
            for(int column = 0; column < columnsA.length; column++) {
                drawing[row][column] = color;
            }
        }
        return drawing;
    }

    /**
     * Define a cor de um dos pixels
     * do desenho.
     * @param row a linha em que o pixel
     *            se encontra.
     * @param column a coluna em que o pixel
     *               se encontra.
     * @param color a cor desejada.
     * @return esse "editor".
     */
    public Drawing setColor(int row, int column, char color) {
        this.drawing[row][column] = color;
        return this;
    }

    /**
     * Retorna a cor de um dos pixels
     * do desenho.
     * @param row a linha em que o pixel
     *            se encontra.
     * @param column a coluna em que o pixel
     *               se encontra.
     * @return a cor do pixel.
     */
    public char getColor(int row, int column) {
        return this.drawing[row][column];
    }

    /**
     * Retorna o desenho em String.
     * A String retornada é o desenho
     * pronto, feito para ser enviado em
     * uma mensagem de chat.
     * Caso você queira uma String para salvar
     * o desenho no banco de dados, use o método
     * Drawing#toString().
     * @return o desenho.
     */
    public String getDrawing() {
        StringBuilder stringBuilder = new StringBuilder();
        for (char[] columns : drawing) {
            for (int column = 0; column < columns.length; column++) {
                stringBuilder.append("§").append(columns[column]).append("█");
                if (column + 1 == columns.length) stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Retorna uma nova cópia
     * desse desenho.
     * @return o novo desenho.
     */
    public Drawing copy() {
        Drawing newDrawing = new Drawing(this.rows, this.columns);
        for(int row = 0; row < this.rows; row++) {
            for(int column = 0; column < this.columns; column++) {
                newDrawing.setColor(row, column, this.drawing[row][column]);
            }
        }
        return newDrawing;
    }

}
