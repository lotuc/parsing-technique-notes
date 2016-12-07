根据语法解析字符串意味着重新构造生成树（该字符串是怎么通过语法生成的）

恢复出生成树这种要求并不是一种自然而然的，毕竟，语法只是一组字符串精炼的描述（condensed description）。
如对于一个语言，输入的字符串要么属于它要么不；其内部结构或者生成路径并不被考虑。然而在 **实际应用中，语法
中会存在语义；特定语法规则表示特定语义** 。

## 各种歧义

称一个拥有多个生成树的句子为有歧义的。

需要区分的是 **真歧义（essential ambiguity）** 和 **假歧义（spurious ambiguity）** 。

> 如果一个有歧义的句子所有的生成树语义相同，那么它是假歧义

例如

<center>
$$\begin{smallmatrix}
R1  & Sum_S & \to & Digit \\
R2  & Sum   & \to & Sum + Sum \\
RD0 & Digit & \to & 0 \\
\dots \\
RD9 & Digit & \to & 9 \\
\end{smallmatrix}$$
</center>

对于 `1+2+3` 就有两种生成方式，`(1+2)+3`，和 `1+(2+3)`，考虑规则 $$Sum \to Sum + Sum$$ 的语义作为
两个数字之和，这两种生成方式最终语义相同，但是如果这个规则表示的是两数之差，则这两个生成方式语义就不同了。

## 解析树（Parse Tree）的线性化

```
             R2
         ____|____
        /    |    \
       R1    |     R2
       |     |    __|__
       |     |   /  |  \
       |     |  R1  |  R1
       |     |   |  |   |
      RD1    |  RD2 |  RD3
       1     +   2  +   3
```

主要有三种方式：前序（prefix），后序（postfix），中序（infix）。

先序：从根节点开始，对于每个节点，先列出根节点，然后从左到右按先序方式列举每个子节点，这种方式得到 left-most  派生式。

left-most: R2, R1, RD1, R2, R1, RD2, R1, RD3

后序 --> right-most

中序 --> left-corner （一般左侧一个，left-most 和 right-most 可以看作 left-corner 的特殊化）

## 解析句子的两种方法

句子和语法基本的关系是：

1. 语法描述了句子是如何生成的。
2. 重新构造这种关系的过程称之为parsing

**实际parsing技术只有两种：其他方法只是他们的细化和润色**

- 自上而下（Top-down）
- 自下而上（Bottom-up）


