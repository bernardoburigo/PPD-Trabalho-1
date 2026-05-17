# Pesquisa 1 - Busca Sequencial Simples

## O que implementa

Busca sem paralelismo. O programa percorre todos os arquivos `.txt` do diretorio informado e analisa cada arquivo linha por linha.

## Estrategia de busca

Usa `String.contains`, que representa uma comparacao simples e direta.

## Estrategia de Thread

Nao usa Threads. Todo o processamento acontece na Thread principal.

## Como executar

```bash
javac -d build/pesquisa_1 pesquisa_1/codigo/*.java
java -cp build/pesquisa_1 Pesquisa1App data/txt Mariana
```

## Pontos fortes

- Codigo simples.
- Resultado facil de validar.
- Serve como base para calcular Speed Up.

## Pontos fracos

- Processa apenas um arquivo por vez.
- Pode demorar mais quando existem muitos arquivos ou arquivos grandes.

## Quando usar

Recomendada como versao base do trabalho e como referencia para validar se as buscas paralelas estao corretas.
