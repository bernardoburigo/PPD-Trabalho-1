# Pesquisa 2 - Uma Thread por Arquivo

## O que implementa

Busca paralela em que cada arquivo `.txt` e processado por uma Thread diferente.

## Estrategia de busca

Usa `String.contains` em cada linha.

## Estrategia de Thread

Para cada arquivo encontrado, o programa cria uma `Thread`. Cada Thread le seu proprio arquivo e adiciona os resultados em estruturas seguras para concorrencia.

## Como executar

```bash
javac -d build/pesquisa_2 pesquisa_2/codigo/*.java
java -cp build/pesquisa_2 Pesquisa2App data/txt Mariana
```

## Pontos fortes

- Demonstra paralelismo de forma direta.
- Pode melhorar o tempo quando ha varios arquivos.
- Cada Thread trabalha de forma independente.

## Pontos fracos

- Criar uma Thread por arquivo pode consumir muita memoria.
- Em diretorios com milhares de arquivos, o excesso de Threads pode piorar o desempenho.

## Quando usar

Boa para fins didaticos e para diretorios com quantidade moderada de arquivos.
