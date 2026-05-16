# Trabalho 1 - Computacao Paralela (Java)

Projeto com duas solucoes de busca em arquivos `.txt`:

1. Busca sequencial (sem paralelismo)
2. Busca paralela com Threads (duas estrategias)

Tambem inclui gerador de massa de dados e benchmark de Speedup.

## Estrutura

- `src/GenerateTxtDataApp.java`: gera arquivos `.txt` para teste
- `src/SequentialSearchApp.java`: busca sequencial
- `src/ParallelSearchApp.java`: busca paralela
- `src/BenchmarkApp.java`: compara tempos e calcula speedup

## Compilacao

```bash
javac src/*.java
```

## Execucao por menu (recomendado para apresentacao)

```bash
java -cp src MenuApp
```

No menu, voce pode:

- gerar os arquivos `.txt`
- executar busca sequencial
- executar busca paralela
- executar benchmark de speedup
- executar o fluxo completo em sequencia

## 1) Gerar arquivos TXT

Exemplo (40 arquivos, 5000 linhas por arquivo):

```bash
java -cp src GenerateTxtDataApp data/txt 40 5000 Mariana 0.03
```

Parametros:

- diretorio_saida (padrao: `data/txt`)
- quantidade_arquivos (padrao: 40)
- linhas_por_arquivo (padrao: 5000)
- nome_alvo (padrao: `Mariana`)
- taxa_ocorrencia_por_linha (padrao: 0.03)

## 2) Busca sequencial

```bash
java -cp src SequentialSearchApp data/txt Mariana KMP
```

## 3) Busca paralela

```bash
java -cp src ParallelSearchApp data/txt Mariana 8 KMP PER_FILE
```

ou

```bash
java -cp src ParallelSearchApp data/txt Mariana 8 KMP BATCHED
```

Parametros:

- diretorio
- nome
- threads (opcional)
- algoritmo: `NAIVE` ou `KMP` (opcional)
- estrategia: `PER_FILE` ou `BATCHED` (opcional)

## 4) Benchmark e Speedup

```bash
java -cp src BenchmarkApp data/txt Mariana 8
```

Formula usada:

- `Speedup = Tempo_Sequencial / Tempo_Paralelo`

## Observacoes para apresentacao

- O projeto usa 2 algoritmos de pesquisa (`NAIVE` e `KMP`) para pontuacao extra.
- O projeto usa 2 estrategias de Threads (`PER_FILE` e `BATCHED`) para comparar abordagens.
- Para resultados mais estaveis, execute o benchmark mais de uma vez e tire media.
