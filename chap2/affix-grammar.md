Like VW grammars, *affix* grammars establish long-range relation by duplicating information in an early stage; this information is, however, not part of the non-terminal name, but is passed as an independent parameter, an *affix*, which can, for instance, be an integer value.

一般来说，这些 *affix* 在规则中被传递，知道被传入一类特殊的非终结符，*primitive predicate*。它不是用来产生文本，而是包含了一个合法性测试。要使得该句式合法，则其中的合法性测试必须也要合法。*affix* 技术和 VW 中的元概念技术是等价的，但是处理起来简单点。

<center>
$$ \begin{smallmatrix} 
N, M  & ::  & integer.  \\
A, B  & ::  & a; b; c.
\end{smallmatrix} $$<br/><br/>

$$ \begin{smallmatrix} 
text_S + N & :  & list + N + a, list + N + b, list + N + c.
\end{smallmatrix} $$<br/><br/>

$$ \begin{smallmatrix} 
list + N + A  & :  & \text{where is zero + N;} \\
              &    & \text{letter + A, where is decreased + M + N,} \\
              &    & \text{list + M + A.} \\
list + N + A  & :  & \text{where is zero + N;} \\
              &    & \text{letter + A, where is decreased + M + N,} \\
              &    & \text{list + M + A.} \\
list + A  & :  & \text{where is + A + a, a symbol;} \\
          &    & \text{where is + A + b, b symbol;} \\
          &    & \text{where is + A + c, c symbol.} \\
\text{where is zero + N}           & : & {N = 0}.\\
\text{where is decreased + M + N}  & : & {M = N - 1}.\\
\text{where is + A + B}            & : & {A = B}.
\end{smallmatrix} $$<br/><br/>
</center>

对应于 $$ a^nb^nc^n $$ 语言的 VW 语法关于 **N, A** 的定义。
