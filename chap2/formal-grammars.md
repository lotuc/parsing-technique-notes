生成语法是一个四元组 $$ (V_N, V_T, R, S) $$，它们满足：

1. $$ V_N $$ 是一个有限符号集
2. $$ V_N \bigcap V_T = \phi $$
3. $$ R $$ 是二元组的集合，其中二元组 $$ (P, Q) $$ 满足
  - $$ P \in (V_N \bigcup V_T) $$
  - $$ Q \in (V_N \bigcup V_T)^* $$
4. $$ S \in V_N $$


> A generative grammar is a 4-tuple $$ (V_N, V_T, R, S) $$ such that
>
> 1. $$ V_N $$ are finite sets of symbols
> 2. $$ V_N \bigcap V_T = \phi $$
> 3. $$ R $$ is a set of pairs $$ (P, Q) $$ such that
>   - $$ P \in (V_N \bigcup V_T) $$
>   - $$ Q \in (V_N \bigcup V_T)^* $$
> 4. $$ S \in V_N $$

一个例子：

> $$ V_N = \{Name, Sentence, List, End\} $$
>
> $$ V_T = \{tom, dick, harry, and\} $$
>
> $$ R = \{(Name, tom), (Name, dick), (Name, harry), $$ <br/>
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
> $$ (Sentence, Name), (Sentence, List End), $$ <br/>
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
> $$ (List, Name), (List, List，Name), (，Name End, and Name)\} $$
>
> $$ S = Sentence $$

通俗一点就是
$$ \begin{matrix}
Sentence_S & \to & \text{Name | List End} \\
Name       & \to & \text{tom | dick | harry} \\
List       & \to & \text{Name | List，Name} \\
，Name End & \to & \text{and Name}
\end{matrix} $$

