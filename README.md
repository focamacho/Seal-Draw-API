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

Para usar a Seal Draw você precisa primeiro criar uma instância da classe `com.focamacho.sealdrawapi.SealDrawAPI`.

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

Para criar um desenho é só criar um objeto da classe `com.focamacho.sealdrawapi.api.Drawing`, passando as informações de tamanho da linha, e tamanho da coluna do desenho.

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