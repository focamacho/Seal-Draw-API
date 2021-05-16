# Seal Draw API
A Seal Draw é uma API para Bukkit, BungeeCord e Sponge que permite a criação de Pixel Arts por meio do chat do minecraft.

Ela facilita para que os desenvolvedores possam implementar em seus plugins vários sistemas utilizando Pixel Arts que os próprios jogadores possam fazer dentro do jogo por meio do chat.

Um exemplo de possível sistema utilizando a API, é a criação de emblemas de clãs personalizados.
<img src="https://i.imgur.com/UFBDSfE.png"></img>

## Indíce
Atalhos para certas seções desse documento.
- [Seal Draw API](#Seal-Draw-API)
  * [Requisitos](#Requisitos)
     + [Bukkit](#Bukkit)
     + [BungeeCord](#BungeeCord)
     + [Sponge](#Sponge)
  * [Primeiros Passos](#Primeiros-Passos)
  * [Usando a API](#Usando-a-API)
    + [Como definir botões clicáveis no "editor"](#Como-definir-botões-clicáveis-no-editor)
    + [Como salvar os desenhos](#Como-salvar-os-desenhos)
    + [Exemplo prático](#Exemplo-prático)

## Requisitos
Alguns requisitos são necessários para que seja possível utilizar a Seal Draw API.

Esses requisitos se provaram necessários para evitar que o jogador que está a editar uma pixel art receba mensagens de chat durante o processo, o que faria com que o seu desenho se movesse para cima, atrapalhando a edição.

Aqui está a lista do que é necessário para cada ‘software’:
### Bukkit
- Nenhuma dependência adicional é necessária.
### BungeeCord
- Protocolize [[download](https://www.spigotmc.org/resources/protocolize-protocollib-for-bungeecord-waterfall-aegis.63778/)]
   - É necessário também adicionar o Protocolize como dependência no plugin que você está criando. O motivo disso é para fazê-lo carregar antes do seu plugin.
   - Para isso é só adicionar a linha ``depends: ["protocolize-plugin"]`` no seu `bungee.yml`.
### Sponge
- Nenhuma dependência adicional é necessária.

# Aviso
Ainda não existe nenhuma release da Seal Draw API disponível (mesmo que esse README diga que sim algumas linhas para baixo). Caso queira testá-la você deverá fazê-lo compilando você mesmo o source-code com ``./gradlew clean build``.


## Primeiros passos
Para começar a usar a API, você precisa primeiro adicioná-la no seu projeto. Aqui estão as dependências para Maven e Gradle:

**Maven**
```xml
<repository>
    <id>focamacho-repo</id>
    <name>Focamacho Maven Repository</name>
    <url>https://repo.focamacho.com/repository/releases/</url>
</repository>
```
```xml
<dependency>
    <groupId>com.focamacho</groupId>
    <artifactId>sealdrawapi</artifactId>
    <version>VERSAO</version>
</dependency>
```

**Gradle**
```groovy
repositories {
  maven { url = 'https://repo.focamacho.com/repository/releases/' }
}

dependencies {
    compile 'com.focamacho:sealdrawapi:VERSAO'
}
```

Obs: Troque a palavra `VERSAO` pela versão da API desejada. Verifique a última versão disponível nas [releases](https://github.com/Seal-Island/Seal-Draw-API/releases) do GitHub.

## Usando a API

Para usar a Seal Draw você precisa primeiro criar uma instância da classe `SealDrawAPI`.

Para criar a instância da API você precisará fornecer a instância do seu plugin no construtor da classe. Não crie mais de uma instância da API! Crie ela somente uma vez e use essa instância o tempo todo.

Exemplo:
```java
import com.focamacho.sealdrawapi.SealDrawAPI;

public class ExamplePlugin extends JavaPlugin {

    //Instância da SealDrawAPI
    public static SealDrawAPI api;

    @Override
    public void onEnable() {
        api = new SealDrawAPI(this);
    }
    
}
```

Com a instância da API você terá acesso a criação de novas "telas de desenhos", as quais eu me referencio como "editor" nos docs disponíveis nas classes do projeto.
Para criar uma tela de desenhos é só usar o método `SealDrawAPI#createPaint`, passando junto dela o desenho que você deseja editar.

Para criar um desenho é só criar um objeto da classe `Drawing`, passando as informações de número de linhas e colunas do desenho.

**ATENÇÃO! NÃO ABUSE DO TAMANHO.**

Ao criar desenhos muito grandes você acaba excedendo o limite permitido, fazendo com que, quando um jogador receba a mensagem, ele seja desconectado do servidor por causa do tamanho do pacote que ele recebeu.

É recomendado que você estabeleça um limite de até 16x16 para seus desenhos.

Exemplo:
```java
    //Criar o desenho, de tamanho 16x16
    Drawing desenho = new Drawing(16, 16);

    //Criar uma tela de edição para esse desenho.
    Paint paint = api.createPaint(desenho);
    
    //Abre essa tela para um jogador
    paint.openPaint(player);
```

Com um [Drawing](https://github.com/Seal-Island/Seal-Draw-API/blob/master/sealdrawapi-common/src/main/java/com/focamacho/sealdrawapi/api/Drawing.java) e um objeto da classe [Paint](https://github.com/Seal-Island/Seal-Draw-API/blob/master/sealdrawapi-common/src/main/java/com/focamacho/sealdrawapi/api/Paint.java), você já está pronto para criar seus desenhos.

Confira a documentação disponível nessas duas classes para poder se aprofundar mais.

### Como definir botões clicáveis no "editor"
As vezes é necessário definir alguns botões adicionais para os usuários. Por padrão, a Seal Draw API já define os seguintes botões:

- cancel
- confirm
- clean
- color

Você pode editá-los para que exibam o texto e efetue as ações que você quiser. Exemplo:
```java
   paint.getButton("confirm")
        .setText("&aConfirmar")
        .setHover("&aClique para confirmar o desenho.")
        .setAction((player, paint) -> {
            //Sempre se lembre de fechar o editor ao confirmar.
            paint.closePaint(player);
        
            //Aqui você pode definir o que quiser
   });
```

O botão "color" é um caso a parte. Você somente pode editar o seu texto e não suas ações, isso porque ele possui uma função muito específica que é selecionar a cor que o usuário está utilizando no momento.
Para modificar seu texto é usado o método `Paint#setColorSelectorText`.

Você também pode criar novos botões pelo método `Paint#setButton`.

Todos os botões podem ser definido nas mensagem antes e após o desenho no chat, por meio de seus respectivos placeholders. Exemplo: `%cancel%`, `%confirm%`, `%clean%`.

### Como salvar os desenhos

Você provavelmente vai querer salvar os desenhos que os seus jogadores fizerem. Para isso, você pode convertê-los em uma String, e quando for necessário poderá carregar o desenho novamente a partir dessa String.

Exemplo de como fazer isso quando o jogador clica no botão de confirmar:

```java
    //Criar uma tela de edição
    Paint paint = api.createPaint(new Drawing(16, 16));

    paint.getButton("confirm")
        .setAction((player, paint) -> {
            //Sempre se lembre de fechar o editor ao confirmar.
            paint.closePaint(player);

            //Converte o desenho em uma String, você pode salvar esse valor
            String desenho = paint.getDrawing().toString();
        });
```

E para gerar um desenho a partir da String é só usar:

```java
    Drawing drawing = Drawing.fromString(desenho);
```

### Exemplo prático
Aqui está um exemplo prático de como a API pode ser usada para criar um sistema de emblemas para Clãs.

```java
	//Cria um novo editor e desenho
	Paint paint = api.createPaint(new Drawing(8, 8));
	//Define uma identação de 28 espaços antes do desenho.
	//Isso é feito para centralizá-lo.
	paint.setSpaces(28);

	//Define as mensagens que são exibidas antes do desenho.
	paint.setBeforeMessages(false, 
		"                            &d&lBrasão do Clã",
		"",
		"           &aChegou a hora de criar o brasão do seu clã.",
		"       &aPara isso é só clicar nos quadradinhos abaixo para",
		"               &acolori-los da forma que você quiser!",
		"",
		"                           &eCor selecionada: &%selected%█",
		"                    %colorselector%",
		""
        );
		
	//Define as mensagens que são exibidas após o desenho.
	paint.setAfterMessages(false, 
		"",
		"                 %cancel% %clean% %confirm%",
		""
        );

	//Define a ação ao clicar no botão confirmar.
	paint.getButton("confirm").setAction((player, editor) -> {
		//Fecha o desenho para o jogador. Nunca esqueça desse detalhe.
		editor.closePaint(player);
		
		//Aqui você pode definir todo o resto para criar o clã.
	});
	
	//Define a ação ao clicar no botão cancelar.
	paint.getButton("cancel").setAction((player, editor) -> {
		//Fecha o desenho para o jogador. Nunca esqueça desse detalhe.
		editor.closePaint(player);
		
		//Aqui você pode definir todo o resto para cancelar a criação do clã.
	});
```
Esse é o resultado do código acima:

<img src="https://i.imgur.com/G6mG9Zl.png"></img>