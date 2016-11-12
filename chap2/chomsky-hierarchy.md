# Type 0

[形式语法定义](./formal-grammars.md)中定义的语法，无其他限制。

其他级别的规则均是在该定义的规则上加上各种限制所成。


# Type 1

对于 *Type 0* 的语法来说，可以将任意数量（非零）的符号转换为任意数量（可能为零）的符号：

> $$ \begin{smallmatrix} ，N E \to \text{and N} \end{smallmatrix} $$

我们对这一点做出限制形成 *Type 1* 语法。它有两种定义方式，是等价的。

__定义1：*Type 1 monotonic（单调）*__

不包含左侧符号数量大于右侧符号个数的规则。形如 $$ ，N E \to \text{and N} $$ 这样的规则就是被禁止的。

__定义2:*Type 1 context-sensitive（上下文相关）*__

语法的所有的规则都是*上下文相关（context-sensitive）*的。一个规则是上下文相关的是指左侧只有一个非终结符号被其它符号替换了，其它符号（所谓上下文，即其前后的符号序列）保持顺序不变的移动到右侧。例如：

> $$ \begin{smallmatrix} \text{Name Comma Name End} \to \text{Name and Name End} \end{smallmatrix} $$


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

<center id="sentence_rule">
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

你可以将其中的符号分别读作：

- : &nbsp;&nbsp;&nbsp;&nbsp; “被定义为一个”
- ; &nbsp;&nbsp;&nbsp;&nbsp; “，或一个”
- , &nbsp;&nbsp;&nbsp;&nbsp; “后面跟着一个”
- . &nbsp;&nbsp;&nbsp;&nbsp; “，除了这些再无其他了”

这样上面的第二个规则就可以读作：

> sentence 被定义为一个 name，或一个 list 后面跟着一个 “and symbol” 后面跟着一个 “name”，除了这些再无其他了。


## 扩展的 CF 语法（extended context-free grammar, ECF grammar）

常常会通过给常用的生成方式引入特殊的记法让 CF 语法变得更为简洁和易读；这些记法的引入并不会增强 CF 语法的表现力，所有包含引入的记法的规则都可以被转换成普通的 CF 规则。比如考虑规则：

<center>
$$ \begin{smallmatrix}
something & \to & \text{something | something otherthing}
\end{smallmatrix} $$
</center>

这种规则经常会出现，在 *ECF 语法* 中，会使用 $$ \begin{smallmatrix} something^+ \end{smallmatrix} $$ 代表“一或多个 something”。*ECF 语法* 中引入的最常见的记号：

- $$ \begin{smallmatrix} something^+ \end{smallmatrix} $$ ： 表示“一或多个 *something*”。
- $$ \begin{smallmatrix} something^* \end{smallmatrix} $$ ： 表示“零或多个 *something*”。
- $$ \begin{smallmatrix} something^? \end{smallmatrix} $$ ： 表示“零或一个 *something*”。

看看这些符号所做的事，我们可以称它们为*重复运算符*，包含这些运算符的表达式不妨称之为*正则表达式（regular expression）*。好吧，人人都见过正则表达式了吧。规则右侧包含正则表达式的语法——ECF——因此有时也被称作 *右侧是正则表达式的语法（regular right part grammar，RRP grammar）*。

有时候，一些书籍里为了方便会使用 $$ \begin{smallmatrix} something^+4 \end{smallmatrix} $$ 表示“一个或多个，但最多为4个的 something”。或使用 $$ \begin{smallmatrix} something^+, \end{smallmatrix} $$ 表示“一个或多个 *something*，它们被逗号分隔”。

<center>
$$ \begin{smallmatrix}
书籍 & \to & \text{前言 } 章节^+ \text{ 结语}
\end{smallmatrix} $$
</center>

是这个规则的缩写：

<center>
$$ \begin{smallmatrix}
书籍    & \to & \text{前言  } \alpha \text{  结语} \\
\alpha & \to & \text{章节 | 章节  } \alpha
\end{smallmatrix} $$
</center>

它们也是下面这个“迭代”版本的缩写：

<center>
$$ \begin{smallmatrix}
书籍 & \to   & \text{前言 章节 结语} \\
    & |     & \text{前言 章节 章节 结语} \\
    & |     & \text{前言 章节 章节 章节 结语} \\
    & |     & \dots \\
    & \dots &
\end{smallmatrix} $$
</center>

当然，这种版本的表示有无穷多的规则。


# Type 3

此级别的语法只能包含两种类型的规则：

- 非终结符产生零或多个终结符
- 非终结符产生零到多个终结符，后面跟着一个非终结符

Chomsky 对于 Type 3 语法的原始定义中限制能包含的规则是：

- 非终结符产生一个终结符
- 非终结符产生一个终结符，后面跟着一个非终结符

当然，这两种限制方式是等价的。Type 3 语法也被称为 *正则语法（regular grammar, RE grammar）*或*有限状态语法（finite-state grammar, FS grammar）*。正则语法常被用来描述文本的结构，这种应用场景中常把终结符定义为单个字符。我们还是使用 [sentence](#sentence_rule) 那个规则，将 *Tom*、*Dick*、*Harry*、*and* 分别计作 *t*、*d*、*h* 和 *&*：

<center id="tdh&_rule"> \begin{smallmatrix}
Sentence_S & \to & \text{t  | d | h | List} \\
List       & \to & \text{t ListTail | d ListTail | h ListTail} \\
ListTail   & \to & , List | &t | &d | &h
\end{smallmatrix} </center>


# Type 4

这次我们给语法加上的限制就比较终极了：非终结符只能是我们给定那些；且规则的右侧不能含有非终结符。它的起始符（start symbol）可以从我们给定的范围内中去选择，这点反映在了其别称 *有限选择语法（finite-choice grammar, FC grammar）*。

FC 语法并不属于“官方”的 Chomsky 层级。

上面的 *tdh&* 规则是不可能被 FC 语法进行描述。如果我们限制列表（List）的长度，这样 FC 也是可以描述它的，因为我们可以穷举所有可能性：

<center> \begin{smallmatrix}
S_S & \to & \text{[tdh] | [tdh] & [tdh] | [tdh], [tdh] & [tdh]}
\end{smallmatrix} </center>

它包含 $$ 3+3*3+3*3*3=39 $$ 种生成规则。
