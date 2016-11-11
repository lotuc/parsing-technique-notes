# Type 0

[形式语法定义](./formal-grammars.md)中定义的语法，无其他限制。

其他级别的规则均是在该定义的规则上加上各种限制所成。

# Type 1

对于 *Type 0* 的语法来说，可以将任意数量（非零）的符号转换为任意数量（可能为零）的符号：

> $$ ，N E \to \text{and N} $$

我们对这一点做出限制形成 *Type 1* 语法。它有两种定义方式，是等价的。

__定义1：*Type 1 monotonic（单调）*__

不包含左侧符号数量大于右侧符号个数的规则。形如 $$ ，N E \to \text{and N} $$ 这样的规则就是被禁止的。

__定义2:*Type 1 context-sensitive（上下文相关）*__

语法的所有的规则都是*上下文相关（context-sensitive）*的。一个规则是上下文相关的是指左侧只有一个非终结符号被其它符号替换了，其它符号（所谓上下文，即其前后的符号序列）保持顺序不变的移动到右侧。例如：

> $$ \text{Name Comma Name End} \to \text{Name and Name End} $$


Type 1 语言的一个例子是包含相同数量的a、b、c的序列，它们顺序如下：

<center>
$$ \begin{smallmatrix}
\text{a a} \dots a & \text{b b} \dots b & \text{c c} \dots c \\
\smile & \smile & \smile \\
n个a & n个b & n个c
\end{smallmatrix} $$
</center>


# Type 2

*Type 2* 语法被称为上下文无关语法（context-free grammars, CF grammars），它和上下文相关语法的关系就和其名字指出的一样。上下文无关语法和上下文相关语法的区别是左侧符号转换到右侧时不需要考虑上下文。所以，其语法经常在其左侧只包含一个非终结符，例如：

<center>
$$ \begin{smallmatrix}
Sentence_S & \to & \text{Name | List and Name} \\
Name       & \to & \text{tom | dick | harry} \\
List       & \to & \text{Name, List | Name} \\
\end{smallmatrix} $$
</center>

CF 规则只在一种情况下是*非单调的*，即它的右侧为空（这样的规则又被称为 $$ \epsilon\text{-rule} $$），不包含 $$ \epsilon-rule $$ 的语法称为是 $$ \epsilon\text{-free} $$ 的。


CF 语法有几种记法，功能上是等价的，这里记录两种：

- *Backus-Naur Form*
- *van Wijngaarden Form*


## *Backus-Naur Form*

<center>
$$ \begin{smallmatrix}
<name> & ::= & \text{tom | dick | harry} \\
<sentence>_S & ::= & \text{<name> | <list> and <name>} \\
<list> & ::= & \text{<name>, <list> | <name>}
\end{smallmatrix} $$
</center>

## *van Wijngaarden Form*

<center>
$$ \begin{smallmatrix}
name       & : & \text{tom symbol; dick symbol; harry symbol.} \\
sentence_S & : & \text{name; list, and symbol, name.} \\
list       & : & \text{name, comma symbol, list; name.}
\end{smallmatrix} $$
</center>
