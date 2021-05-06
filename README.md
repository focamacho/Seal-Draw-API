# Requisitos
Alguns requisitos são necessários para que seja possível utilizar a Seal Draw API.

Esses requisitos se provaram necessários para evitar que o jogador que está a editar uma pixel art receba mensagens de chat durante o processo, o que faria com que o seu desenho se movesse para cima, atrapalhando a edição.

Aqui está a lista do que é necessário para cada ‘software’:
### Bukkit
 - ProtocolLib [[download](https://www.spigotmc.org/resources/protocollib.1997/)]
   - É necessário também adicionar o ProtocolLib como dependência no plugin que você está criando. O motivo disso é para fazê-lo carregar antes do seu plugin.
   - Para isso é só adicionar a linha ``depend: [ProtocolLib]`` no seu `plugin.yml`.
### BungeeCord
 - Protocolize [[download](https://www.spigotmc.org/resources/protocolize-protocollib-for-bungeecord-waterfall-aegis.63778/)]
   - É necessário também adicionar o Protocolize como dependência no plugin que você está criando. O motivo disso é para fazê-lo carregar antes do seu plugin.
   - Para isso é só adicionar a linha ``depends: ["protocolize-plugin"]`` no seu `bungee.yml`.
### Sponge
 - A versão para Sponge ainda está em desenvolvimento. O chat não é pausado quando um desenho está sendo editado.

# Aviso
Ainda não existe nenhuma release da Seal Draw API disponível (mesmo que esse README diga que sim algumas linhas para baixo). Caso queira testá-la você deverá fazê-lo compilando você mesmo o source-code com ``./gradlew build``.

# Seal Draw API
A Seal Draw é uma API para Bukkit, BungeeCord e Sponge que permite a criação de Pixel Arts por meio do chat do minecraft.

Ela facilita para que os desenvolvedores possam implementar em seus plugins vários sistemas utilizando Pixel Arts que os próprios jogadores possam fazer dentro do jogo por meio do chat.

Um exemplo de possível sistema utilizando a API, é a criação de emblemas de clãs personalizados.
<img src="https://i.imgur.com/UFBDSfE.png"></img>

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
import SealDrawAPI;

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

Para criar um desenho é só criar um objeto da classe `Drawing`, passando as informações de tamanho da linha, e tamanho da coluna do desenho.

**ATENÇÃO! NÃO ABUSE DO TAMANHO.**

Ao criar desenhos muito grandes você acaba excedendo o limite permitido, fazendo com que, quando um jogador receba a mensagem, ele seja desconectado do servidor por causa do tamanho do pacote que ele recebeu.

É recomendado que você estabeleça um limite de até 16x16 para seus desenhos.

Exemplo:
```java
    //Criar o desenho, de tamanho 16x16
    Drawing desenho = new Drawing(16, 16);

    //Criar uma tela de edição para esse desenho.
    AbstractPaint paint = api.createPaint(desenho);
    
    //Abre essa tela para um jogador
    paint.openPaint(player);
```

Com um [Drawing](https://github.com/Seal-Island/Seal-Draw-API/blob/master/sealdrawapi-common/src/main/java/com/focamacho/sealdrawapi/api/Drawing.java) e um objeto da classe [AbstractPaint](https://github.com/Seal-Island/Seal-Draw-API/blob/master/sealdrawapi-common/src/main/java/com/focamacho/sealdrawapi/api/AbstractPaint.java), você já está pronto para criar seus desenhos.

Confira a documentação disponível nessas duas classes para poder se aprofundar mais.

### Como salvar os desenhos

Você provavelmente vai querer salvar os desenhos que os seus jogadores fizerem. Para isso, você pode convertê-los em uma String, e quando for necessário carregar o desenho novamente a partir dessa String.

Exemplo de como fazer isso quando o jogador clica no botão de confirmar:

```java
    //Criar uma tela de edição
    AbstractPaint paint = api.createPaint(desenho);

    //Definir a função do botão de confirmar
    paint.setOnConfirm((player, paint) -> {
            //Finaliza o editor quando confirmado
            paint.closePaint(player);

            //Converte o desenho em uma String
            String desenho = paint.getDrawing().toString();
    });
```

E para gerar um desenho a partir da String é só usar:

```java
    Drawing drawing = Drawing.fromString(desenho);
```