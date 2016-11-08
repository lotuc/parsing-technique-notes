# Introduction
Parsing 是通过给定语法构建相关线性表示的过程。

> **原文：** Parsing is the process of structuring a linear representation in accordance with a given grammar.


其中 “linear representation” 指的可能是一个句子、一段程序、一首乐曲，诸如此类。

对于语法来说，通常可以通过它构建出无穷多的线性表示（句子，sentence）。

称这种构建过程为 Parsing 的原因有几个：

1. Obtained structure helps us to process the object further.
2. The grammar in a sense represents our understanding of the observed sentence.
3. Parers can provide the completion of missing information, especially error-repairing parsers.


---

# 书籍概览

- **Chap 2**：句法，语法
- **Chap 3**：Parsing 的原则，Parsing 方法的分类
- **Chap 4**：Non-directional methods（Unger & CYK）
- **Chap 5**：有限状态机（Finite-state automata）的处理
- **Chap 6**：Non-deterministic directional top-down parsers（recursive descent, Definite Clause Grammars）
- **Chap 7**：Non-deterministic directional bottom-up parsers（Earley）
- **Chap 8**：Deterministic methods（top-down：各种形式的 LL）
- **Chap 9**：bottom-up：LR 等，该节还介绍了一种确定／不确定组合方法（combined deterministic/non-deterministic method, Tomita）
- **Chap 10**：错误处理的一些方法
- **Chap 11**：总结比较了一些流行、次流行方法的特点
- **Chap 12**：
