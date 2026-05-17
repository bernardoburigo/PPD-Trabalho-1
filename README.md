# Trabalho 1 - Busca em Arquivos TXT com Threads

Projeto em Java para comparar busca sequencial e busca paralela em arquivos `.txt`.

O sistema percorre um diretorio recursivamente, encontra arquivos `.txt`, analisa linha por linha e exibe todas as ocorrencias do texto buscado com arquivo, numero da linha e conteudo.

## Estrutura

```text
pesquisa_1/  Busca sequencial simples, sem Threads
pesquisa_2/  Uma Thread por arquivo
pesquisa_3/  Pool fixo de Threads
pesquisa_4/  Divisao dos arquivos em grupos
pesquisa_5/  KMP otimizado com pool fixo de Threads
apresentacao/roteiro_apresentacao_grupo_4.txt
data/txt/    Massa de dados usada nos testes
src/         Codigo original preservado como referencia
```

## Requisitos

- Java 21 ou superior.
- Terminal na raiz do projeto.

## Como compilar

Compile cada pesquisa separadamente para evitar conflito entre classes auxiliares de mesmo nome:

```bash
javac -d build/pesquisa_1 pesquisa_1/codigo/*.java
javac -d build/pesquisa_2 pesquisa_2/codigo/*.java
javac -d build/pesquisa_3 pesquisa_3/codigo/*.java
javac -d build/pesquisa_4 pesquisa_4/codigo/*.java
javac -d build/pesquisa_5 pesquisa_5/codigo/*.java
```

No PowerShell, os mesmos comandos funcionam.

## Como executar

O primeiro argumento e o diretorio. O segundo argumento e o texto buscado.

```bash
java -cp build/pesquisa_1 Pesquisa1App data/txt Mariana
java -cp build/pesquisa_2 Pesquisa2App data/txt Mariana
java -cp build/pesquisa_3 Pesquisa3App data/txt Mariana 8
java -cp build/pesquisa_4 Pesquisa4App data/txt Mariana 8
java -cp build/pesquisa_5 Pesquisa5App data/txt Mariana 8
```

Se nenhum argumento for informado, os programas usam `data/txt` e `Mariana`.

## Metricas exibidas

Cada pesquisa mostra:

- quantidade de arquivos encontrados;
- quantidade de arquivos `.txt` processados;
- quantidade total de linhas analisadas;
- quantidade de ocorrencias encontradas;
- tempo de execucao;
- lista das ocorrencias encontradas.

Cada ocorrencia segue o formato:

```text
Arquivo: caminho_do_arquivo | Linha: numero | Conteudo: linha_encontrada
```

## Speed Up

O Speed Up compara o tempo da versao sequencial com o tempo de uma versao paralela:

```text
Speed Up = Tempo Sequencial / Tempo Paralelo
```

Exemplo: se a pesquisa sequencial demorou 100 ms e a paralela demorou 50 ms:

```text
Speed Up = 100 / 50 = 2
```

Quando o Speed Up e maior que 1, a versao paralela foi mais rapida. Quanto maior o valor, maior foi o ganho de desempenho.

## Comparacao esperada

- `pesquisa_1`: mais simples e previsivel, mas tende a ser mais lenta em muitos arquivos.
- `pesquisa_2`: paraleliza agressivamente, mas pode criar Threads demais.
- `pesquisa_3`: usa pool fixo, controlando melhor CPU e memoria.
- `pesquisa_4`: reduz overhead ao dar grupos de arquivos para cada Thread.
- `pesquisa_5`: combina Threads com algoritmo KMP, mostrando ganho por paralelismo e por algoritmo.

## Testes sugeridos

Use `data/txt` para o teste principal:

```bash
java -cp build/pesquisa_1 Pesquisa1App data/txt Mariana
java -cp build/pesquisa_3 Pesquisa3App data/txt Mariana 8
```

Compare se as pesquisas retornam a mesma quantidade de ocorrencias e linhas analisadas.

Tambem e recomendado criar arquivos pequenos para validar:

- arquivo com uma ocorrencia;
- arquivo com varias ocorrencias;
- arquivo sem ocorrencia;
- arquivo vazio;
- arquivos em subpastas;
- busca com maiusculas e minusculas.

Observacao: a busca implementada e case-sensitive. Portanto, `Mariana` e `mariana` sao tratados como textos diferentes.
