按计算机科学这门学科中的常用讲法，“语法（grammar）”用于“描述（describe）”一门“语言（language）”。

自然语言很有意思也异常复杂，在 CS 学科中我们从很抽象的视角使用了从其沿用过来的名词。一门语言会有句子，句子会有特定结构，我们不必关心句子所谓交流上的意义，但是由句子的结构衍生出来的信息，我们不妨称其为句子的*意义（meaning）*。句子包含单词（word），CS 中对应的是*记号（token）*，每个都包含一点信息，构成句子时对句子的意义做出贡献。 递归去看，word 也有自己的结构，实际上它也是一门语言的句子。


---

<table>
<tr>
<td colspan="2"></td>
<td colspan="2">Chomsky (monotonic) hierarchy</td>
<td>non-monotonic hierarchy</td>
</tr>
<tr>
<td rowspan="2">global production effects</td>
<td>Type 0</td>
<td>无限制的 Phrase Structure（PS）语法</td>
<td>不包含 $$ \epsilon\text{-规则} $$ 的单调语法</td>
<td>无限制的 Phrase Structure（PS）语法</td>
</tr>
<tr>
<td>Type 1</td>
<td>上下文相关语法</td>
<td>不包含 $$ \epsilon\text{-规则} $$ 的单调语法</td>
<td>规则单调上下文相关语法</td>
</tr>
<tr>
<td rowspan="2">local production effects</td>
<td>Type 2</td>
<td colspan="2">上下文无关的 $$ \epsilon\text{-free} $$ 的语法</td>
<td>上下文无关语法</td>
</tr>
<tr>
<td>Type 3</td>
<td colspan="2">正则 $$ \epsilon\text{-free} $$ 语法</td>
<td>正则语法</td>
</tr>
<tr>
<td rowspan="2">no production</td>
<td>Type 4</td>
<td colspan="3">有限选择语法</td>
</tr>
</table>
