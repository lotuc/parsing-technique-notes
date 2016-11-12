前面介绍了语法的各层级：

- phrase structure
- context-sensitive（上下文相关）
- context-free （上下文无关）
- regular （正则语法）
- finite-choice （有限选择）

这些类型的边界定义清晰，其中有些边界更为重要。有两条边界尤为特别：上下文相关和上下文无关语法的边界，正则语法（有限状态语法）和有限选择语法的边界。后者是规则是否 productive 或 non-productive 的边界，还比较 trivial 的；但是前者则是影响深远。

CS 和 CF 之间的边界是关于全局相关性和局部独立性的边界。一旦一个非终结符通过 CF 语法中的句式被生成，对该非终结符的进一步发展**不需要**考虑到它的左右的邻居（即其上下文）。CF 语法的这种局部独立性使得其不能表达特定的一些长距的关系。然而很有意思的是，CF 这种关系可以用来表示输入文本的一些基本属性，比如程序代码中变量的使用。用 CF 语法描述这些输入时，

对于有些关系无法表示时，一种常用的解决办法就是先确定一个 CF 语法用于解析文本，解析后再去检查该语法不能表示的关系，当然这需要对于输入的属性做出完整精确的描述。

我们可以使用 CS 语法解决上面无法使用 CF 语法表示的关系（上下文相关），但是会有一个实践上的问题：它能很好的表示这种关系，但是对阅读者不友好。

<center>
$$ \begin{smallmatrix}
S_S         & \to & \text{a b c | a S bc_pack} \\
b bc_pack c & \to & \text{b b c c} \\
c bc_pack   & \to & bc_pack c
\end{smallmatrix} $$

<br/>
$$ a^nb^nc^n $$的单调（Monotonic）语法
</center>

The cause of all this mesery is that CS and PS grammars derive their power to enforce global relationships from "just slightly more than local dependency". Theoretically, just looking at the neighbours can be proved to be enough to express any global relation, but the enforcement of a long-range relation through this mechanism causes information to flow through the sentential form over long distance.


## VW 语法

说 CF 语法不能表示长距关系是部队的——它们能表示有限数量的长距关系。比如我们有一门语言，包含**开始**，**中间**，**结尾**，且分别有三种类型的**开始**和*结尾**，那么下面的 CF 语法可用于表示这门语言且包含**开始**与**结局的关系**：

<center>
$$ \begin{smallmatrix}
text_S & \to & \text{开始0 中间 结尾0} \\
       & |   & \text{开始1 中间 结尾1} \\
       & |   & \text{开始2 中间 结尾2}
\end{smallmatrix} $$
</center>

我们可以将**开始0**和**结尾0**看作 **(** 和 **)**，**开始1**和**结尾1**看作 **[** 和 **]**，**开始2**和**结尾2**看作 **{** 和 **}**；该 CF 语法会确保括号正确的闭合。

通过将 CF 语法变得更大，我们便能表示更长距的关系；*如果让其变得无限大，我们可以表示任意的长距关系，实现完全的上下文相关*。这已经触及到 VW 语法的核心观念。

所以我们要的其实是用于描述无穷多字符串集的无穷多 CF 语法形式的规则，这种规则使用一种语法描述，这种语法你可以称之为“two-level grammar”。

以 $$a^nb^nc^n$$ 这门语言为例，我们可以通过下面这种无穷大的 CF 语言 L 描述：

<center>
\begin{smallmatrix}
text_S & : & \text{a symbol, b symbol, c symbol;} \\
       &   & \text{a symbol, a symbol, b symbol, b symbol, c symbol, c symbol;} \\
       & \dots & \dots
\end{smallmatrix}
</center>

现在尝试构建一个用于生成上面语法的语法。首先引入无穷多个非终结符：

<center>
$$ \begin{smallmatrix}
text_S & : & \text{ai, bi, ci;} \\
       &   & \text{aii, bii, cii;} \\
       &   & \text{aiii, biii, ciii;} \\
       & \dots & \dots
\end{smallmatrix} $$
</center>

加上三组关于这些非终结符的规则：

<center>
$$ \begin{smallmatrix}
ai    & : & \text{a symbol.} \\
aii   & : & \text{a symbol, ai.} \\
aiii  & : & \text{a symbol, aii.} \\
\dots &   & \dots \\
\end{smallmatrix} $$ <br/><br/>

$$ \begin{smallmatrix}
bi    & : & \text{b symbol.} \\
bii   & : & \text{b symbol, bi.} \\
biii  & : & \text{b symbol, bii.} \\
\dots &   & \dots
\end{smallmatrix} $$ <br/><br/>

$$ \begin{smallmatrix}
ci    & : & \text{c symbol.} \\
cii   & : & \text{c symbol, ci.} \\
ciii  & : & \text{c symbol, cii.} \\
\dots &   & \dots
\end{smallmatrix} $$
</center>

这里 *i* 作为 a, b, c 的计数。下面引入一种特殊的称为元概念（metanotion）的东西。它用于生成语法中的名字而不是语言中的句子。比如这里我们想要记录对于元概念 **N** 的 i 次重复，可以使用一个 CF 生成规则生成（元规则，metarule）：

<center>
$$ \begin{smallmatrix}
N & :: & \text{i ; i N .}
\end{smallmatrix} $$
</center>

注意我们使用了与普通规则（:）稍微不同的符号（::）的记号来区分愿规则，且其成员使用空格而不是逗号分隔。元概念 **N** 产生 i, ii, iii。使用该 **N** 简化上面那三组规则：

<center>
$$ \begin{smallmatrix}
text_S & : & \text{a N, b N, c N.} \\
\end{smallmatrix} $$<br/><br/>

$$ \begin{smallmatrix}
a i   & : & \text{a symbol.} \\
a i N & : & \text{a symbol, a N.} \\
\end{smallmatrix} $$<br/><br/>

$$ \begin{smallmatrix}
b i   & : & \text{b symbol.} \\
b i N & : & \text{b symbol, b N.} \\
\end{smallmatrix} $$<br/><br/>

$$ \begin{smallmatrix}
c i   & : & \text{c symcol.} \\
c i N & : & \text{c symcol, c N.} \\
\end{smallmatrix} $$
</center>

使用这种概念，进一步简化：

<center>
$$ \begin{smallmatrix}
N & : & \text{i ; i N .} \\
A & : & \text{a ; b ; c .} \\
\end{smallmatrix} $$<br/><br/>

$$ \begin{smallmatrix}
text_S & : & \text{a N, b N, c N.} \\
A i   & : & \text{A symbol.} \\
A i N & : & \text{A symbol, A N.} \\
\end{smallmatrix} $$<br/><br/>
</center>

实际上这就是 $$a^nb^nc^n$$ 的 VW 语法版本。对于 VW 语法的详细解释可以搜索以下作者：

- *Craig Cleaveland* 和 *Uzgalis* 给我们展示了 VW 的诸多应用
- *Snitzoff* 证明了 VW 语法与 PS（Phrase Structure）语法有相同的表现力
- Van Wijngaarden 证明了元语法只需要为正则语法就可以了（当然使用允许使用 CF 语法，会产生比较简单的语法）
- Greibach

## VW 语法的 BNF 记法

<center>
$$ \begin{smallmatrix}
N      & \to & \text{i | i N} \\
A      & \to & \text{a | b | c}
\end{smallmatrix} $$ <br/><br/>

$$ \begin{smallmatrix}
<text>_S & : & \text{<aN> <bN> <cN>} \\
<Ai>     & : & \text{A} \\
<AiN>    & : & \text{A <AN>}
\end{smallmatrix} $$
</center>
